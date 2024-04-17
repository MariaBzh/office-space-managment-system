package ru.otus.osms.biz.workers

import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.*
import ru.otus.osms.common.stubs.OsmsStub
import ru.otus.osms.cor.ICorChainDsl
import ru.otus.osms.cor.worker
import ru.otus.osms.stubs.OsmsBookingStub

fun ICorChainDsl<OsmsContext>.stubReadSuccess(title: String) = worker {
    this.title = title
    on { stubCase == OsmsStub.SUCCESS && state == OsmsState.RUNNING }
    handle {
        state = OsmsState.FINISHING
        val stub = OsmsBookingStub.prepareResult {
            bookingRequest.bookingUid.takeIf { it != OsmsBookingUid.NONE }?.also { this.bookingUid }
            bookingRequest.userUid.takeIf { it != OsmsUserUid.NONE }?.also { this.userUid }
            bookingRequest.branch.takeIf {
                it.branchUid != OsmsBranchUid.NONE &&
                        it.name.isNotBlank()
            }?.also { this.branch }
            bookingRequest.floor.takeIf {
                it.floorUid != OsmsFloorUid.NONE &&
                        it.level.isNotBlank()
            }?.also { this.floor }
            bookingRequest.office.takeIf {
                it.officeUid != OsmsOfficeUid.NONE &&
                        it.name.isNotBlank()
            }?.also { this.office }
            bookingRequest.workspaceUid.takeIf { it != OsmsWorkspaceUid.NONE }?.also { this.workspaceUid }
            bookingRequest.description.takeIf { it.isNotBlank() }?.also { this.description }
            bookingRequest.startTime.takeIf { it.isNotBlank() }?.also { this.startTime }
            bookingRequest.endTime.takeIf { it.isNotBlank() }?.also { this.endTime }
            bookingRequest.permissions.takeIf { it.isNotEmpty() }?.also { this.permissions }
        }
        bookingResponse = stub
    }
}
