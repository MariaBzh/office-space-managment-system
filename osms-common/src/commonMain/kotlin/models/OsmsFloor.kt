package ru.otus.osms.common.models

data class OsmsFloor(
    val floorUid: OsmsFloorUid = OsmsFloorUid.NONE,
    val level: String = "",
    val description: String = "",
) {
    companion object {
        val NONE = OsmsFloor()
    }
}