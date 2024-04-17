package ru.otus.osms.biz.validation

import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.helpers.errorValidation
import ru.otus.osms.common.helpers.fail
import ru.otus.osms.cor.ICorChainDsl
import ru.otus.osms.cor.worker

fun ICorChainDsl<OsmsContext>.validateUserUid(title: String) = worker {
    this.title = title

    val regExp = Regex(UID_REGEX)

    on { bookingValidating.userUid.asString().isNotBlank() && !bookingValidating.userUid.asString().contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "userUid",
                violationCode = "badUid",
                description = "field must contain [1-36] characters: letters, digits and '.'"
            )
        )
    }
}

fun ICorChainDsl<OsmsContext>.validateWorkplaceUid(title: String) = worker {
    this.title = title

    val regExp = Regex(UID_REGEX)

    on { bookingValidating.workspaceUid.asString().isNotBlank() && !bookingValidating.workspaceUid.asString().contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "workplaceUid",
                violationCode = "badUid",
                description = "field must contain [1-36] characters: letters, digits and '.'"
            )
        )
    }
}

fun ICorChainDsl<OsmsContext>.validateBookingUid(title: String) = worker {
    this.title = title

    val regExp = Regex(UID_REGEX)

    on { bookingValidating.bookingUid.asString().isNotBlank() && !bookingValidating.bookingUid.asString().contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "bookingUid",
                violationCode = "badUid",
                description = "field must contain [1-36] characters: letters, digits and '.'"
            )
        )
    }
}

fun ICorChainDsl<OsmsContext>.validateBranchUid(title: String) = worker {
    this.title = title

    val regExp = Regex(UID_REGEX)

    on { bookingValidating.branch.branchUid.asString().isNotBlank() && !bookingValidating.branch.branchUid.asString().contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "branchUid",
                violationCode = "badUid",
                description = "field must contain [1-36] characters: letters, digits and '.'"
            )
        )
    }
}

fun ICorChainDsl<OsmsContext>.validateOfficeUid(title: String) = worker {
    this.title = title

    val regExp = Regex(UID_REGEX)

    on { bookingValidating.office.officeUid.asString().isNotBlank() && !bookingValidating.office.officeUid.asString().contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "officeUid",
                violationCode = "badUid",
                description = "field must contain [1-36] characters: letters, digits and '.'"
            )
        )
    }
}

fun ICorChainDsl<OsmsContext>.validateFloorUid(title: String) = worker {
    this.title = title

    val regExp = Regex(UID_REGEX)

    on { bookingValidating.floor.floorUid.asString().isNotBlank() && !bookingValidating.floor.floorUid.asString().contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "floorUid",
                violationCode = "badUid",
                description = "field must contain [1-36] characters: letters, digits and '.'"
            )
        )
    }
}

const val UID_REGEX = "[0-9a-zA-Z]{1,36}\$"
