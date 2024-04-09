package ru.otus.osms.db

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import models.PermissionEntity
import ru.otus.osms.common.models.*
import ru.otus.osms.common.repo.*
import ru.otus.osms.db.models.BookingEntity
import ru.otus.osms.db.models.BranchEntity
import ru.otus.osms.db.models.FloorEntity
import ru.otus.osms.db.models.OfficeEntity
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class BookingRepoInMemory(
    initObjects: Collection<OsmsBooking> = emptyList(),
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : IBookingRepository {

    private val cache = Cache.Builder<String, BookingEntity>()
        .expireAfterWrite(ttl)
        .build()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(booking: OsmsBooking) {
        val entity = BookingEntity(booking)

        cache.put(entity.bookingUid!!, entity)
    }

    override suspend fun createBooking(request: DbBookingRequest): DbBookingResponse {
        val key = randomUuid()
        val booking = request.booking.copy(bookingUid = OsmsBookingUid(key))

        save(booking)

        return DbBookingResponse(
            data = booking,
            isSuccess = true,
        )
    }

    override suspend fun readBooking(request: DbBookingUidRequest): DbBookingResponse {
        val key = request.bookingUid.takeIf { it != OsmsBookingUid.NONE }?.asString() ?: return resultErrorEmptyUid

        return cache.get(key)
            ?.let {
                DbBookingResponse(
                    data = it.toInternal(),
                    isSuccess = true,
                )
            } ?: resultErrorNotFound
    }

    override suspend fun updateBooking(request: DbBookingRequest): DbBookingResponse {
        val key = request.booking.bookingUid.takeIf { it != OsmsBookingUid.NONE }?.asString() ?: return resultErrorEmptyUid
        val newBooking = request.booking.copy()
        val entity = BookingEntity(newBooking)

        return when (cache.get(key)) {
            null -> resultErrorNotFound
            else -> {
                cache.put(key, entity)

                DbBookingResponse(
                    data = newBooking,
                    isSuccess = true,
                )
            }
        }
    }

    override suspend fun deleteBooking(request: DbBookingUidRequest): DbBookingResponse {
        val key = request.bookingUid.takeIf { it != OsmsBookingUid.NONE }?.asString() ?: return resultErrorEmptyUid

        return when (val oldBooking = cache.get(key)) {
            null -> resultErrorNotFound
            else -> {
                cache.invalidate(key)
                DbBookingResponse(
                    data = oldBooking.toInternal(),
                    isSuccess = true,
                )
            }
        }
    }

    override suspend fun searchBooking(request: DbBookingFilterRequest): DbBookingsResponse {
        val result = cache.asMap().asSequence()
            .filter { entry ->
                request.userUid.takeIf { it != OsmsUserUid.NONE }?.let {
                    it.asString() == entry.value.userUid
                } ?: true
            }
            .filter { entry ->
                request.workspaceUid.takeIf { it != OsmsWorkspaceUid.NONE }?.let {
                    it.asString() == entry.value.workplaceUid
                } ?: true
            }
            .filter { entry ->
                request.branch.takeIf {
                    it != OsmsBranch.NONE &&
                    it.branchUid != OsmsBranchUid.NONE
                }?.let {
                    it == entry.value.branch?.toInternal()
                } ?: true
            }
            .filter { entry ->
                request.floor.takeIf {
                    it != OsmsFloor.NONE &&
                    it.floorUid != OsmsFloorUid.NONE
                }?.let {
                    it == entry.value.floor?.toInternal()
                } ?: true
            }
            .filter { entry ->
                request.office.takeIf {
                    it != OsmsOffice.NONE &&
                    it.officeUid != OsmsOfficeUid.NONE
                }?.let {
                    it == entry.value.office?.toInternal()
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()

        return DbBookingsResponse(
            data = result,
            isSuccess = true
        )
    }

    companion object {
        val resultErrorEmptyUid = DbBookingResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                OsmsError(
                    code = "id-empty",
                    group = "validation",
                    field = "id",
                    message = "Uid must not be null or blank"
                )
            )
        )
        val resultErrorNotFound = DbBookingResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                OsmsError(
                    code = "not-found",
                    field = "id",
                    message = "Not Found"
                )
            )
        )
    }
}
