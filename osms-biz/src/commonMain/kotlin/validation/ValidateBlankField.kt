package ru.otus.osms.biz.validation

import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.helpers.errorValidation
import ru.otus.osms.common.helpers.fail
import ru.otus.osms.common.models.OsmsBranch
import ru.otus.osms.common.models.OsmsFloor
import ru.otus.osms.common.models.OsmsOffice
import ru.otus.osms.cor.ICorChainDsl
import ru.otus.osms.cor.worker

fun ICorChainDsl<OsmsContext>.validateUserNotBlank(title: String) = worker {
    this.title = title

    on { bookingValidating.userUid.asString().isBlank() }
    handle {
        fail(
            errorValidation(
                field = "userUid",
                violationCode = "notFound",
                description = "field must not be blank"
            )
        )
    }
}

fun ICorChainDsl<OsmsContext>.validateWorkplaceNotBlank(title: String) = worker {
    this.title = title

    on { bookingValidating.workspaceUid.asString().isBlank() }
    handle {
        fail(
            errorValidation(
                field = "workspaceUid",
                violationCode = "notFound",
                description = "field must not be blank"
            )
        )
    }
}

fun ICorChainDsl<OsmsContext>.validateBookingNotBlank(title: String) = worker {
    this.title = title

    on { bookingValidating.bookingUid.asString().isBlank() }
    handle {
        fail(
            errorValidation(
                field = "bookingUid",
                violationCode = "notFound",
                description = "field must not be blank"
            )
        )
    }
}

fun ICorChainDsl<OsmsContext>.validateBranchUidNotBlank(title: String) = worker {
    this.title = title

    on { bookingValidating.branch.branchUid.asString().isBlank() }
    handle {
        fail(
            errorValidation(
                field = "branchUid",
                violationCode = "notFound",
                description = "field must not be blank"
            )
        )
    }
}

fun ICorChainDsl<OsmsContext>.validateBranchNotBlank(title: String) = worker {
    this.title = title

    on { bookingValidating.branch == OsmsBranch.NONE }
    handle {
        fail(
            errorValidation(
                field = "branch",
                violationCode = "notFound",
                description = "field must not be blank"
            )
        )
    }
}

fun ICorChainDsl<OsmsContext>.validateOfficeUidNotBlank(title: String) = worker {
    this.title = title

    on { bookingValidating.office.officeUid.asString().isBlank() }
    handle {
        fail(
            errorValidation(
                field = "officeUid",
                violationCode = "notFound",
                description = "field must not be blank"
            )
        )
    }
}

fun ICorChainDsl<OsmsContext>.validateOfficeNotBlank(title: String) = worker {
    this.title = title

    on { bookingValidating.office == OsmsOffice.NONE }
    handle {
        fail(
            errorValidation(
                field = "office",
                violationCode = "notFound",
                description = "field must not be blank"
            )
        )
    }
}

fun ICorChainDsl<OsmsContext>.validateFloorUidNotBlank(title: String) = worker {
    this.title = title

    on { bookingValidating.floor.floorUid.asString().isBlank() }
    handle {
        fail(
            errorValidation(
                field = "floorUid",
                violationCode = "notFound",
                description = "field must not be blank"
            )
        )
    }
}

fun ICorChainDsl<OsmsContext>.validateFloorNotBlank(title: String) = worker {
    this.title = title

    on { bookingValidating.floor == OsmsFloor.NONE }
    handle {
        fail(
            errorValidation(
                field = "floor",
                violationCode = "notFound",
                description = "field must not be blank"
            )
        )
    }
}

fun ICorChainDsl<OsmsContext>.validateStartTimeNotBlank(title: String) = worker {
    this.title = title

    on { bookingValidating.startTime.isBlank() }
    handle {
        fail(
            errorValidation(
                field = "startTime",
                violationCode = "notFound",
                description = "field must not be blank"
            )
        )
    }
}

fun ICorChainDsl<OsmsContext>.validateEndTimeNotBlank(title: String) = worker {
    this.title = title

    on { bookingValidating.endTime.isBlank() }
    handle {
        fail(
            errorValidation(
                field = "endTime",
                violationCode = "notFound",
                description = "field must not be blank"
            )
        )
    }
}
