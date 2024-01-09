package ru.otus.osms.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class OsmsBranchUid(
    private val uid: String,
) {
    fun asString() = uid

    companion object {
        val NONE = OsmsBranchUid("")
    }
}