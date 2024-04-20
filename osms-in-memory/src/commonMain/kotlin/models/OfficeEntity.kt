package ru.otus.osms.db.inmemory.models

import ru.otus.osms.common.models.OsmsOffice
import ru.otus.osms.common.models.OsmsOfficeUid

data class OfficeEntity (
    val officeUid: String? = null,
    val name: String? = null,
    val description: String? = null,
) {

    fun toInternal() = OsmsOffice(
        officeUid = officeUid?.let { OsmsOfficeUid(officeUid) } ?: OsmsOfficeUid.NONE,
        name = name ?: "",
        description = description ?: "",
    )
}