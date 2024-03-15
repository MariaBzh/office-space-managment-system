package ru.otus.osms.mappers.kmp.v1

import ru.otus.osms.common.models.OsmsError

private sealed interface Result<T>
private data class Ok<T>(val value: T) : Result<T>
private data class Err<T>(val errors: List<OsmsError>) : Result<T>

@Suppress("unused")
private fun <T> Result<T>.getOrNull(errors: MutableList<OsmsError>): T? = when (this) {
    is Ok<T> -> this.value
    is Err<T> -> {
        errors.addAll(this.errors)
        null
    }
}
