package ru.otus.osms.db.inmemory.models

import ru.otus.osms.common.models.OsmsFloor
import ru.otus.osms.common.models.OsmsFloorUid

data class FloorEntity (
    val floorUid: String? = null,
    val level: String? = null,
    val description: String? = null
) {

    fun toInternal() = OsmsFloor(
        floorUid = floorUid?.let { OsmsFloorUid(floorUid) } ?: OsmsFloorUid.NONE,
        level = level ?: "",
        description = description ?: "",
    )
}
