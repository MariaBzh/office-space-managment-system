package ru.otus.osms.cor.test

import kotlinx.coroutines.test.runTest
import ru.otus.osms.cor.handlers.CorChain
import ru.otus.osms.cor.handlers.CorWorker
import ru.otus.osms.cor.handlers.executeSequential
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class CorChainTest {
    @Test
    @JsName("chain_should_execute_workers")
    fun chainShouldExecuteWorkers() = runTest {
        val createWorker = { title: String ->
            CorWorker<TestContext>(
                title = title,
                blockOn = { status == TestContext.CorStatuses.NONE },
                blockHandle = { history += "$title; " }
            )
        }
        val chain = CorChain(
            execs = listOf(createWorker(WORKER_NAME_1), createWorker(WORKER_NAME_2)),
            title = "chain_test",
            handler = ::executeSequential
        )
        val ctx = TestContext()

        chain.exec(ctx)

        assertEquals("${WORKER_NAME_1}; ${WORKER_NAME_2}; ", ctx.history)
    }
}