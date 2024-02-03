import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.helpers.addError
import ru.otus.osms.common.models.OsmsCommand
import ru.otus.osms.common.models.OsmsWorkMode
import ru.otus.osms.common.stubs.OsmsStub
import ru.otus.osms.stubs.OsmsBookingStub

@Suppress("RedundantSuspendModifier")
class OsmsBookingProcessor {
    suspend fun exec(ctx: OsmsContext) {
        require(ctx.workMode == OsmsWorkMode.STUB) {
            "Currently working only in STUB mode."
        }

        when (ctx.stubCase) {
            OsmsStub.SUCCESS -> {
                when (ctx.command) {
                    OsmsCommand.CREATE -> { ctx.bookingResponse = OsmsBookingStub.get() }
                    OsmsCommand.UPDATE -> { ctx.bookingResponse = OsmsBookingStub.get(
                        ctx.bookingRequest.bookingUid.asString(),
                        ctx.bookingRequest.startTime,
                        ctx.bookingRequest.endTime,
                    ) }
                    OsmsCommand.SEARCH -> { ctx.bookingsResponse.addAll(OsmsBookingStub.prepareSearchList()) }
                    else -> {
                        ctx.bookingResponse = OsmsBookingStub.get(ctx.bookingRequest.bookingUid.asString())
                    }
                }
            }
            OsmsStub.NOT_FOUND -> { ctx.addError(OsmsBookingStub.getError(OsmsStub.NOT_FOUND.name)) }
            OsmsStub.BAD_TIME -> { ctx.addError(OsmsBookingStub.getError(OsmsStub.BAD_TIME.name)) }
            OsmsStub.BAD_UID -> { ctx.addError(OsmsBookingStub.getError(OsmsStub.BAD_UID.name)) }
            else -> {
                ctx.bookingResponse = OsmsBookingStub.get(ctx.bookingRequest.bookingUid.asString())
            }
        }
    }
}