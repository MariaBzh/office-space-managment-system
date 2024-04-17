import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromContext() {
        val context = OsmsContext(
            requestUid = OsmsRequestUid(REQUEST_UID),
            command = OsmsCommand.CREATE,
            bookingResponse = booking,
            errors = mutableListOf(
                composeError()
            ),
            state = OsmsState.RUNNING,
        )

        val log = context.toLog("test-uid")
        val error = log.errors?.firstOrNull()

        assertEquals(LOG_UID, log.logUid)
        assertEquals(LOG_SOURCE, log.source)
        assertEquals(REQUEST_UID, log.booking?.requestUid)

        assertEquals(DEFAULT_ERROR_MESSAGE, error?.message)
        assertEquals("ERROR", error?.level)
    }

    companion object {
        private const val LOG_UID = "test-uid"
        private const val LOG_SOURCE = "OSMS"
        private const val REQUEST_UID = "qwerty123"

        private const val DEFAULT_ERROR_CODE = "test"
        private const val DEFAULT_ERROR_GROUP = "test group"
        private const val DEFAULT_ERROR_FIELD = "test field"
        private const val DEFAULT_ERROR_MESSAGE = "test message"

        private val booking = OsmsBooking(
            userUid = OsmsUserUid("f947b0f7-5df1-4580-8848-88e245b219a2"),
            workspaceUid = OsmsWorkspaceUid("3d6acb16-6e60-42ca-8fd3-a7492fa21caa"),
            branch = OsmsBranch(
                branchUid = OsmsBranchUid("2cc01441-4d6e-48de-a56d-cacc0e32f5a9"),
                name = "Московский",
            ),
            floor = OsmsFloor(
                floorUid = OsmsFloorUid("542b2680-5fd3-4772-b89b-9a0b6b58a41c"),
                level = "1A",
            ),
            office = OsmsOffice(
                officeUid = OsmsOfficeUid("497c568c-8e6b-45ff-9b94-4840c5d05e2b"),
                name = "Марс"
            ),
            startTime = "2023-12-21T10:00:00.000",
            endTime = "2023-12-21T16:00:00.000",
            permissions = mutableSetOf(
                OsmsBookingPermissions.READ,
                OsmsBookingPermissions.UPDATE,
                OsmsBookingPermissions.DELETE,
            ),
        )

        private fun composeError(
            code: String = DEFAULT_ERROR_CODE,
            group: String = DEFAULT_ERROR_GROUP,
            field: String = DEFAULT_ERROR_FIELD,
            message: String = DEFAULT_ERROR_MESSAGE,
        ) = OsmsError(
            code = code,
            group = group,
            field = field,
            message = message,
        )
    }
}