package ru.otus.osms.common.helpers

import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.OsmsError

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