package ru.otus.osms.biz.general

import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.helpers.errorAdministration
import ru.otus.osms.common.helpers.fail
import ru.otus.osms.common.models.OsmsWorkMode
import ru.otus.osms.common.repo.IBookingRepository
import ru.otus.osms.cor.ICorChainDsl
import ru.otus.osms.cor.worker

fun ICorChainDsl<OsmsContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от зпрошенного режима работы        
    """.trimIndent()
    handle {
        bookingRepo = when {
            workMode == OsmsWorkMode.TEST -> corSettings.repoTest
            workMode == OsmsWorkMode.STUB -> corSettings.repoStub
            else -> corSettings.repoProd
        }
        if (workMode != OsmsWorkMode.STUB && bookingRepo == IBookingRepository.NONE) fail(
            errorAdministration(
                field = "repo",
                violationCode = "dbNotConfigured",
                description = "The database is unconfigured for chosen workmode ($workMode). " +
                        "Please, contact the administrator staff"
            )
        )
    }
}