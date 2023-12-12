package ru.otus.osms.common.models

data class OsmsBookingSearchFilter(
    var userUid: OsmsUserUid = OsmsUserUid.NONE,
    var branch: OsmsBranch = OsmsBranch.NONE,
    var floor: OsmsFloor = OsmsFloor.NONE,
    var office: OsmsOffice = OsmsOffice.NONE,
    var workspaceUid: OsmsWorkspaceUid = OsmsWorkspaceUid.NONE,
    var startTime: String = "",
    var endTime: String = "",
)