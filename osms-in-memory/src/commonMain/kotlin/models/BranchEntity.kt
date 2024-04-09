package ru.otus.osms.db.models

import ru.otus.osms.common.models.OsmsBranch
import ru.otus.osms.common.models.OsmsBranchUid

data class BranchEntity (
    val branchUid: String? = null,
    val name: String? = null,
    val description: String? = null,
) {

    fun toInternal() = OsmsBranch(
        branchUid = branchUid?.let { OsmsBranchUid(branchUid) } ?: OsmsBranchUid.NONE,
        name = name ?: "",
        description = description ?: "",
    )
}
