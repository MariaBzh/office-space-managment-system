package ru.otus.osms.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class OsmsBookingLock(
    private val lock: String,
) {
    fun asString() = lock

    companion object {
        val NONE = OsmsBookingLock("")
    }
}