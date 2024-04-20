package ru.otus.osms.biz

import ru.otus.osms.biz.general.initRepo
import ru.otus.osms.biz.general.prepareResult
import ru.otus.osms.biz.groups.operation
import ru.otus.osms.biz.groups.stubs
import ru.otus.osms.biz.repo.*
import ru.otus.osms.biz.validation.*
import ru.otus.osms.biz.workers.*
import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.OsmsCorSettings
import ru.otus.osms.common.models.*
import ru.otus.osms.cor.chain
import ru.otus.osms.cor.rootChain
import ru.otus.osms.cor.worker

class OsmsBookingProcessor(
        @Suppress("unused")
        private val corSettings: OsmsCorSettings = OsmsCorSettings.NONE
) {
    suspend fun exec(context: OsmsContext) = BusinessChain.exec(context.also { it.corSettings = corSettings })

    companion object {
        private val BusinessChain = rootChain {
            initStatus("Init status")
            initRepo("Init repo")
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

                    validateWorkplaceNotBlank("Check if 'workplaceUid' is not blank")
                    validateWorkplaceUid("Check if 'workplaceUid' valid")

                    validateBranchNotBlank("Check if 'branch' is not blank")
                    validateBranchUidNotBlank("Check if 'branchUid' is not blank")
                    validateBranchUid("Check if 'branchUid' is valid")

                    validateFloorNotBlank("Check if 'floor' is not blank")
                    validateFloorUidNotBlank("Check if 'floorUid' is not blank")
                    validateFloorUid("Check if 'floorUid' is valid")

                    validateOfficeNotBlank("Check if 'office' is not blank")
                    validateOfficeUidNotBlank("Check if 'officeUid' is not blank")
                    validateOfficeUid("Check if 'officeUid' is valid")

                    validateStartTimeNotBlank("Check if 'startTime' is not blank")
                    validateEndTimeNotBlank("Check if 'endTime' is not blank")
                    validateStartTimeFormat("Check if start time format is valid")
                    validateEndTimeFormat("Check if end time format is valid")
                    validateTime("Check if time range is valid")

                    finishBookingValidation("Finish checks")
                }
                chain {
                    title = "Логика сохранения"
                    repoPrepareCreate("Подготовка объекта для сохранения")
                    repoCreate("Создание объявления в БД")
                }
                prepareResult("Подготовка ответа")
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

                    validateBookingNotBlank("Check if 'bookingUid' is not blank")
                    validateBookingUid("Check if 'bookingUid' valid")

                    finishBookingValidation("Finish checks")
                }
                chain {
                    title = "Логика чтения"
                    repoRead("Чтение объявления из БД")
                    worker {
                        title = "Подготовка ответа для Read"
                        on { state == OsmsState.RUNNING }
                        handle { bookingRepoDone = bookingRepoRead }
                    }
                }
                prepareResult("Подготовка ответа")
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

                    validateUserNotBlank("Check if 'userUid' is not blank")
                    validateUserUid("Check if 'userUid' valid")

                    validateWorkplaceNotBlank("Check if 'workplaceUid' is not blank")
                    validateWorkplaceUid("Check if 'workplaceUid' valid")

                    validateBranchNotBlank("Check if 'branch' is not blank")
                    validateBranchUidNotBlank("Check if 'branchUid' is not blank")
                    validateBranchUid("Check if 'branchUid' is valid")

                    validateFloorNotBlank("Check if 'floor' is not blank")
                    validateFloorUidNotBlank("Check if 'floorUid' is not blank")
                    validateFloorUid("Check if 'floorUid' is valid")

                    validateOfficeNotBlank("Check if 'office' is not blank")
                    validateOfficeUidNotBlank("Check if 'officeUid' is not blank")
                    validateOfficeUid("Check if 'officeUid' is valid")

                    validateStartTimeNotBlank("Check if 'startTime' is not blank")
                    validateEndTimeNotBlank("Check if 'endTime' is not blank")
                    validateStartTimeFormat("Check if start time format is valid")
                    validateEndTimeFormat("Check if end time format is valid")
                    validateTime("Check if time range is valid")

                    validateLockNotEmpty("Check if lock is not empty")
                    validateLockProperFormat("Validate lock")

                    finishBookingValidation("Finish checks")
                }
                chain {
                    title = "Логика сохранения"
                    repoRead("Чтение объявления из БД")
                    repoPrepareUpdate("Подготовка объекта для обновления")
                    repoUpdate("Обновление объявления в БД")
                }
                prepareResult("Подготовка ответа")
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

                    validateBookingNotBlank("Check if 'bookingUid' is not blank")
                    validateBookingUid("Check if 'bookingUid' valid")

                    validateLockNotEmpty("Check if lock is not empty")
                    validateLockProperFormat("Validate lock")

                    finishBookingValidation("Finish checks")
                }
                chain {
                    title = "Логика удаления"
                    repoRead("Чтение объявления из БД")
                    repoPrepareDelete("Подготовка объекта для удаления")
                    repoDelete("Удаление объявления из БД")
                }
                prepareResult("Подготовка ответа")
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
                repoSearch("Поиск объявления в БД по фильтру")
                prepareResult("Подготовка ответа")
            }
        }.build()
    }
}
