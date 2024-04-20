package ru.otus.osms.repo.pg

import com.benasher44.uuid.uuid4
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import ru.otus.osms.common.helpers.asOsmsError
import ru.otus.osms.common.models.*
import ru.otus.osms.common.repo.*
import ru.otus.osms.repo.Osms
import ru.otus.osms.repo.pg.mappers.toOsmsBooking
import ru.otus.osms.repo.pg.mappers.toOsmsBookings
import java.sql.DriverManager
import java.time.LocalDateTime


class RepoBookingSQL(
    properties: SqlProperties,
    val randomUuid: () -> String = { uuid4().toString() },
) : IBookingRepository {
    private val bookingTable = Osms.OSMS.BOOKING
    private val branchTable = Osms.OSMS.BRANCH
    private val floorTable = Osms.OSMS.FLOOR
    private val officeTable = Osms.OSMS.OFFICE
    private val permissionTable = Osms.OSMS.PERMISSION
    private val usersTable = Osms.OSMS.USERS
    private val refBookingUserPermission = Osms.OSMS.REF_BOOKING_USER_PERMISSION

    private val jooqContext = DSL.using(
        DriverManager.getConnection(properties.url, properties.user, properties.password),
        SQLDialect.POSTGRES
    )

    private fun getPermissionByName(name: String): Int? {
        val record = jooqContext.selectFrom(permissionTable)
            .where(permissionTable.NAME.eq(name.uppercase()))
            .fetchAny() ?: throw RuntimeException("DB error: select statement returned empty result")

        return record[permissionTable.ID]
    }
    
    private fun create(booking: OsmsBooking): OsmsBooking? {
        val bookingUid = randomUuid()

        jooqContext.insertInto(
            bookingTable,
            bookingTable.BOOKING_UID,
            bookingTable.USER_UID,
            bookingTable.WORKPLACE_UID,
            bookingTable.BRANCH_UID,
            bookingTable.FLOOR_UID,
            bookingTable.OFFICE_UID,
            bookingTable.DESCRIPTION,
            bookingTable.STARTTIME,
            bookingTable.ENDTIME,
        ).values(
            bookingUid,
            booking.userUid.asString(),
            booking.workspaceUid.asString(),
            booking.branch.branchUid.asString(),
            booking.floor.floorUid.asString(),
            booking.office.officeUid.asString(),
            booking.description,
            LocalDateTime.parse(booking.startTime),
            LocalDateTime.parse(booking.endTime),
        ).execute()
        
        val readPermission = getPermissionByName(OsmsBookingPermissions.READ.name)
        val updatePermissions = getPermissionByName(OsmsBookingPermissions.UPDATE.name)
        val deletePermissions = getPermissionByName(OsmsBookingPermissions.DELETE.name)
        
        jooqContext.insertInto(refBookingUserPermission,
            refBookingUserPermission.BOOKING_UID,
            refBookingUserPermission.USER_UID,
            refBookingUserPermission.PERMISSION_ID
        ).values(
            bookingUid,
            booking.userUid.asString(),
            readPermission,
        ).values(
            bookingUid,
            booking.userUid.asString(),
            updatePermissions,
        ).values(
            bookingUid,
            booking.userUid.asString(),
            deletePermissions,
        ).execute()

        return read(OsmsBookingUid(bookingUid))
    }

    private fun read(bookingUid: OsmsBookingUid): OsmsBooking? {
        val record = jooqContext.selectFrom(
            bookingTable
                .join(branchTable).on(bookingTable.BRANCH_UID.eq(branchTable.BRANCH_UID))
                .join(floorTable).on(bookingTable.FLOOR_UID.eq(floorTable.FLOOR_UID))
                .join(officeTable).on(bookingTable.OFFICE_UID.eq(officeTable.OFFICE_UID))
                .join(usersTable).on(bookingTable.USER_UID.eq(usersTable.USER_UID))
                .join(refBookingUserPermission)
                .on(refBookingUserPermission.USER_UID.eq(bookingTable.USER_UID))
                .and(bookingTable.BOOKING_UID.eq(refBookingUserPermission.BOOKING_UID))
                .join(permissionTable).on(refBookingUserPermission.PERMISSION_ID.eq(permissionTable.ID))
        )
            .where(bookingTable.BOOKING_UID.eq(bookingUid.asString()))
            .fetch()

        return record.toOsmsBooking(bookingTable, branchTable, floorTable, officeTable, permissionTable)
    }

    private fun update(
        bookingUid: OsmsBookingUid,
        lock: OsmsBookingLock,
        block: (OsmsBooking) -> DbBookingResponse
    ): DbBookingResponse {
        val current = read(bookingUid)

        return when {
            current == null -> DbBookingResponse.errorNotFound
            current.lock != lock -> DbBookingResponse.errorConcurrent(lock, current)
            else -> block(current)
        }
    }

    private fun <T> transactionWrapper(block: () -> T, handle: (Exception) -> T): T {
        jooqContext.startTransaction()
        return try {
            val result = block()
            jooqContext.commit()
            result
        } catch (e: Exception) {
            jooqContext.rollback()
            handle(e)
        }
    }

    private fun transactionWrapper(block: () -> DbBookingResponse): DbBookingResponse =
        transactionWrapper(block) {
            DbBookingResponse.error(
                it.asOsmsError("db", "postgresql", message = it.message ?: "Unexpected error")
            )
        }

    override suspend fun createBooking(request: DbBookingRequest): DbBookingResponse = transactionWrapper {
        create(request.booking)?.let { DbBookingResponse.success(it) } ?: DbBookingResponse.errorNotFound
    }

    override suspend fun readBooking(request: DbBookingUidRequest): DbBookingResponse = transactionWrapper {
        read(request.bookingUid)?.let { DbBookingResponse.success(it) } ?: DbBookingResponse.errorNotFound
    }

    override suspend fun updateBooking(request: DbBookingRequest): DbBookingResponse =
        transactionWrapper {
            update(request.booking.bookingUid, request.booking.lock) {
                val bookingUpdate = request.booking.copy(lock = OsmsBookingLock(randomUuid()))

                jooqContext.update(bookingTable)
                    .set(bookingTable.USER_UID, bookingUpdate.userUid.asString())
                    .set(bookingTable.WORKPLACE_UID, bookingUpdate.workspaceUid.asString())
                    .set(bookingTable.BRANCH_UID, bookingUpdate.branch.branchUid.asString())
                    .set(bookingTable.FLOOR_UID, bookingUpdate.floor.floorUid.asString())
                    .set(bookingTable.OFFICE_UID, bookingUpdate.office.officeUid.asString())
                    .set(bookingTable.DESCRIPTION, bookingUpdate.description)
                    .set(bookingTable.STARTTIME, LocalDateTime.parse(bookingUpdate.startTime))
                    .set(bookingTable.ENDTIME, LocalDateTime.parse(bookingUpdate.endTime))
                    .set(bookingTable.LOCK, bookingUpdate.lock.asString())
                    .where(bookingTable.BOOKING_UID.eq(request.booking.bookingUid.asString()))
                    .execute()

                read(request.booking.bookingUid)?.let { DbBookingResponse.success(it) } ?: DbBookingResponse.errorNotFound
            }
        }

    override suspend fun deleteBooking(request: DbBookingUidRequest): DbBookingResponse =
        transactionWrapper {
            update(request.bookingUid, request.lock) {
                jooqContext.delete(refBookingUserPermission)
                    .where(refBookingUserPermission.BOOKING_UID.eq(request.bookingUid.asString()))
                    .execute()

                jooqContext.delete(bookingTable)
                    .where(bookingTable.BOOKING_UID.eq(request.bookingUid.asString()))
                    .execute()

                DbBookingResponse.success(it)
            }
        }

    override suspend fun searchBooking(request: DbBookingFilterRequest): DbBookingsResponse =
        transactionWrapper(
            {
                val isUser = request.userUid != OsmsUserUid.NONE
                val isBranch = request.branch != OsmsBranch.NONE
                val isFloor = request.floor != OsmsFloor.NONE
                val isOffice = request.office != OsmsOffice.NONE
                val isWorkplace = request.workspaceUid != OsmsWorkspaceUid.NONE
                val isStartTime = request.startTime != ""
                val isEndTime = request.endTime != ""

                val select = jooqContext.selectFrom(bookingTable
                    .join(branchTable).on(bookingTable.BRANCH_UID.eq(branchTable.BRANCH_UID))
                    .join(floorTable).on(bookingTable.FLOOR_UID.eq(floorTable.FLOOR_UID))
                    .join(officeTable).on(bookingTable.OFFICE_UID.eq(officeTable.OFFICE_UID))
                    .join(usersTable).on(bookingTable.USER_UID.eq(usersTable.USER_UID))
                )

                if (isUser) select.where(bookingTable.USER_UID.eq(request.userUid.asString()))
                if (isBranch) select.where(bookingTable.BRANCH_UID.eq(request.branch.branchUid.asString()))
                if (isFloor) select.where(bookingTable.FLOOR_UID.eq(request.floor.floorUid.asString()))
                if (isOffice) select.where(bookingTable.OFFICE_UID.eq(request.office.officeUid.asString()))
                if (isWorkplace) select.where(bookingTable.WORKPLACE_UID.eq(request.workspaceUid.asString()))
                if (isStartTime) select.where(bookingTable.STARTTIME.between(LocalDateTime.parse(request.startTime), LocalDateTime.MAX))
                if (isEndTime) select.where(bookingTable.ENDTIME.between(LocalDateTime.parse(request.endTime), LocalDateTime.MAX))

                val records = select.fetch()

                DbBookingsResponse.success(records.toOsmsBookings(bookingTable, branchTable, floorTable, officeTable))
            },
            {
                DbBookingsResponse.error(it.asOsmsError())
            }
        )

}
