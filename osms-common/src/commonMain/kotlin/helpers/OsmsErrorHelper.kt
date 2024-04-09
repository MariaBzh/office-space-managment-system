package ru.otus.osms.common.helpers

import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.OsmsError
import ru.otus.osms.common.models.OsmsState
import ru.otus.osms.logging.common.LogLevel

fun Throwable.asOsmsError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = OsmsError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun OsmsContext.addError(vararg error: OsmsError) = errors.addAll(error)

fun OsmsContext.fail(error: OsmsError) {
    addError(error)
    state = OsmsState.FAILING
}

fun errorValidation(
    field: String,
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = OsmsError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

fun errorAdministration(
    field: String = "",
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = OsmsError(
    field = field,
    code = "administration-$violationCode",
    group = "administration",
    message = "Microservice management error: $description",
    level = level,
)
