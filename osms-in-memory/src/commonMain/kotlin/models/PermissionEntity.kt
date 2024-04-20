package models

import ru.otus.osms.common.models.OsmsBookingPermissions

data class PermissionEntity (
    val id: String? = null,
    val name: String? = null,
) {

    fun toInternal() = name?.let { OsmsBookingPermissions.valueOf(name) } ?: OsmsBookingPermissions.READ
}