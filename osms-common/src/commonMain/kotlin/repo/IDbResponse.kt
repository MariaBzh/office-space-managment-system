package ru.otus.osms.common.repo

import ru.otus.osms.common.models.OsmsError

interface IDbResponse<T> {
    val data: T?
    val isSuccess: Boolean
    val errors: List<OsmsError>
}
