package ru.otus.osms.common.models

data class OsmsOffice(
    val officeUid: OsmsOfficeUid = OsmsOfficeUid.NONE,
    val name: String = "",
    val description: String = "",
) {
    companion object {
        val NONE = OsmsOffice()
    }
}
