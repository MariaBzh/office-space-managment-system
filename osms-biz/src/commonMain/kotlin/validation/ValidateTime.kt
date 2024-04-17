package ru.otus.osms.biz.validation

import kotlinx.datetime.toLocalDateTime
import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.helpers.errorValidation
import ru.otus.osms.common.helpers.fail
import ru.otus.osms.cor.ICorChainDsl
import ru.otus.osms.cor.worker

fun ICorChainDsl<OsmsContext>.validateStartTimeFormat(title: String) = worker {
    this.title = title

    val regExp = Regex(DATE_TIME_REGEX)

    on { bookingValidating.startTime.isNotBlank() && !bookingValidating.startTime.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "startTime",
                violationCode = "badTime",
                description = "value ${bookingValidating.startTime} must match pattern 'YYYY-mm-ddThh:MM:ss.SSSS'"
            )
        )
    }
}

fun ICorChainDsl<OsmsContext>.validateEndTimeFormat(title: String) = worker {
    this.title = title

    val regExp = Regex(DATE_TIME_REGEX)

    on { bookingValidating.endTime.isNotBlank() && !bookingValidating.endTime.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "endTime",
                violationCode = "badTime",
                description = "value ${bookingValidating.endTime} must match pattern 'YYYY-mm-ddThh:MM:ss.SSSS'"
            )
        )
    }
}

fun ICorChainDsl<OsmsContext>.validateTime(title: String) = worker {
    this.title = title

    on { (bookingValidating.endTime.isNotBlank() && bookingValidating.startTime.isNotBlank())
            && bookingValidating.endTime.toLocalDateTime().compareTo(bookingValidating.startTime.toLocalDateTime()) == -1 }
    handle {
        fail(
            errorValidation(
                field = "startTime, endTime",
                violationCode = "badTime",
                description = "'endTime' must be after 'startTime'"
            )
        )
    }
}

const val DATE_TIME_REGEX = "^(19|20)\\d{2}-(0[1-9]|1[1,2])-(0[1-9]|[12][0-9]|3[01])T[0-2]\\d:[0-5]\\d:[0-5]\\d(?:\\.\\d+)?Z?\$"