package ru.otus.osms.cor

interface ICorExec<T> {
    val title: String
    val description: String
    suspend fun exec(context: T)
}