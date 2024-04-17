package ru.otus.osms.biz

import ru.otus.osms.biz.groups.operation
import ru.otus.osms.biz.groups.stubs
import ru.otus.osms.biz.validation.*
import ru.otus.osms.biz.workers.*
import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.OsmsCorSettings
import ru.otus.osms.common.models.*
import ru.otus.osms.cor.rootChain
import ru.otus.osms.cor.worker

class OsmsBookingProcessor(
        @Suppress("unused")
        private val corSettings: OsmsCorSettings = OsmsCorSettings.NONE
) {
    suspend fun exec(ctx: OsmsContext) = BusinessChain.exec(ctx.also { it.corSettings = corSettings })

    companion object {
        private val BusinessChain = rootChain {
            initStatus("Init status")
            operation("Create booking", OsmsCommand.CREATE) {
                stubs("Stubs processing") {
                    stubCreateSuccess("Create booking success")
                    stubValidationBadUid("Bad UID validation error")
                    stubValidationBadTime("Bad time validation error")
                    stubDbError("DB error")
                    stubNoCase("Stub case not found")
                }
                validation {
                    worker("Copy fields in 'bookingValidating'") { bookingValidating = bookingRequest.deepCopy() }

                    validateUserNotBlank("Check if 'userUid' is not blank")
                    validateUserUid("Check if 'userUid' valid")

                    worker("Clean user UID") { bookingValidating.userUid = OsmsUserUid.NONE }

                    validateWorkplaceNotBlank("Check if 'workplaceUid' is not blank")
                    validateWorkplaceUid("Check if 'workplaceUid' valid")

                    worker("Clean workplace UID") { bookingValidating.workspaceUid = OsmsWorkspaceUid.NONE }

                    validateBranchNotBlank("Check if 'branch' is not blank")
                    validateBranchUidNotBlank("Check if 'branchUid' is not blank")
                    validateBranchUid("Check if 'branchUid' is valid")

                    worker("Clean branch") { bookingValidating.branch = OsmsBranch.NONE }

                    validateFloorNotBlank("Check if 'floor' is not blank")
                    validateFloorUidNotBlank("Check if 'floorUid' is not blank")
                    validateFloorUid("Check if 'floorUid' is valid")

                    worker("Clean floor") { bookingValidating.floor = OsmsFloor.NONE }

                    validateOfficeNotBlank("Check if 'office' is not blank")
                    validateOfficeUidNotBlank("Check if 'officeUid' is not blank")
                    validateOfficeUid("Check if 'officeUid' is valid")

                    worker("Clean office") { bookingValidating.office = OsmsOffice.NONE }

                    validateStartTimeNotBlank("Check if 'startTime' is not blank")
                    validateEndTimeNotBlank("Check if 'endTime' is not blank")
                    validateStartTimeFormat("Check if start time format is valid")
                    validateEndTimeFormat("Check if end time format is valid")
                    validateTime("Check if time range is valid")

                    worker("Clean start time") { bookingValidating.startTime = "" }
                    worker("Clean end time") { bookingValidating.endTime = "" }

                    finishBookingValidation("Finish checks")
                }
            }
            operation("Read booking", OsmsCommand.READ) {
                stubs("Stubs processing") {
                    stubReadSuccess("Read booking success")
                    stubBookingNotFound("Booking not found")
                    stubValidationBadUid("Bad UID validation error")
                    stubDbError("DB error")
                    stubNoCase("Stub case not found")
                }
                validation {
                    worker("Copy fields in 'bookingValidating'") { bookingValidating = bookingRequest.deepCopy() }
                    worker("Clean bookingUid") { bookingValidating.bookingUid = OsmsBookingUid.NONE }

                    validateBookingNotBlank("Check if 'bookingUid' is not blank")
                    validateBookingUid("Check if 'bookingUid' valid")

                    finishBookingValidation("Finish checks")
                }
            }
            operation("Изменить объявление", OsmsCommand.UPDATE) {
                stubs("Stubs processing") {
                    stubUpdateSuccess("Имитация успешной обработки")
                    stubBookingNotFound("Booking not found")
                    stubValidationBadUid("Bad UID validation error")
                    stubValidationBadTime("Bad time validation error")
                    stubDbError("DB error")
                    stubNoCase("Stub case not found")
                }
                validation {
                    worker("Copy fields in 'bookingValidating'") { bookingValidating = bookingRequest.deepCopy() }

                    validateBookingNotBlank("Check if 'bookingUid' is not blank")
                    validateBookingUid("Check if 'bookingUid' valid")

                    worker("Clean booking UID") { bookingValidating.bookingUid = OsmsBookingUid.NONE }

                    validateUserNotBlank("Check if 'userUid' is not blank")
                    validateUserUid("Check if 'userUid' valid")

                    worker("Clean user UID") { bookingValidating.userUid = OsmsUserUid.NONE }

                    validateWorkplaceNotBlank("Check if 'workplaceUid' is not blank")
                    validateWorkplaceUid("Check if 'workplaceUid' valid")

                    worker("Clean workplace UID") { bookingValidating.workspaceUid = OsmsWorkspaceUid.NONE }

                    validateBranchNotBlank("Check if 'branch' is not blank")
                    validateBranchUidNotBlank("Check if 'branchUid' is not blank")
                    validateBranchUid("Check if 'branchUid' is valid")

                    worker("Clean branch") { bookingValidating.branch = OsmsBranch.NONE }

                    validateFloorNotBlank("Check if 'floor' is not blank")
                    validateFloorUidNotBlank("Check if 'floorUid' is not blank")
                    validateFloorUid("Check if 'floorUid' is valid")

                    worker("Clean floor") { bookingValidating.floor = OsmsFloor.NONE }

                    validateOfficeNotBlank("Check if 'office' is not blank")
                    validateOfficeUidNotBlank("Check if 'officeUid' is not blank")
                    validateOfficeUid("Check if 'officeUid' is valid")

                    worker("Clean office") { bookingValidating.office = OsmsOffice.NONE }

                    validateStartTimeNotBlank("Check if 'startTime' is not blank")
                    validateEndTimeNotBlank("Check if 'endTime' is not blank")
                    validateStartTimeFormat("Check if start time format is valid")
                    validateEndTimeFormat("Check if end time format is valid")
                    validateTime("Check if time range is valid")

                    worker("Clean start time") { bookingValidating.startTime = "" }
                    worker("Clean end time") { bookingValidating.endTime = "" }

                    finishBookingValidation("Finish checks")
                }
            }
            operation("Удалить объявление", OsmsCommand.DELETE) {
                stubs("Stubs processing") {
                    stubDeleteSuccess("Имитация успешной обработки")
                    stubBookingNotFound("Booking not found")
                    stubValidationBadUid("Bad UID validation error")
                    stubDbError("DB error")
                    stubNoCase("Stub case not found")
                }
                validation {
                    worker("Copy fields in 'bookingValidating'") { bookingValidating = bookingRequest.deepCopy() }
                    worker("Clean 'booking UID'") { bookingValidating.bookingUid = OsmsBookingUid.NONE }

                    validateBookingNotBlank("Check if 'bookingUid' is not blank")
                    validateBookingUid("Check if 'bookingUid' valid")

                    finishBookingValidation("Finish checks")
                }
            }
            operation("Поиск объявлений", OsmsCommand.SEARCH) {
                stubs("Stubs processing") {
                    stubSearchSuccess("Имитация успешной обработки")
                    stubBookingNotFound("Booking not found")
                    stubValidationBadUid("Bad UID validation error")
                    stubValidationBadTime("Bad time validation error")
                    stubDbError("DB error")
                    stubNoCase("Stub case not found")
                }
                validation {
                    worker("Копируем поля в adFilterValidating") { bookingFilterValidating = bookingFilterRequest.copy() }

                    finishBookingFilterValidation("Успешное завершение процедуры валидации")
                }
            }
        }.build()
    }
}
