package ru.otus.osms.common.models

data class OsmsBranch(
    val branchUid: OsmsBranchUid = OsmsBranchUid.NONE,
    val name: String = "",
    val description: String = "",
) {
    companion object {
        val NONE = OsmsBranch()
    }
}