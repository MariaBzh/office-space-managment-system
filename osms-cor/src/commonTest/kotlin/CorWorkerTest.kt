package ru.otus.osms.cor.test

import kotlinx.coroutines.test.runTest
import ru.otus.osms.cor.handlers.CorWorker
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class CorWorkerTest {
    @Test
    @JsName("worker_should_execute_handle")
    fun workerShouldExecuteHandle() = runTest {
        val worker = CorWorker<TestContext>(
            title = WORKER_NAME_1,
            blockHandle = { history += "${WORKER_NAME_1}; " }
        )
        val ctx = TestContext()

        worker.exec(ctx)

        assertEquals("${WORKER_NAME_1}; ", ctx.history)
    }

    @Test
    @JsName("worker_should_not_execute_when_off")
    fun workerShouldNotExecuteWhenOff() = runTest {
        val worker = CorWorker<TestContext>(
            title = WORKER_NAME_1,
            blockOn = { status == TestContext.CorStatuses.ERROR },
            blockHandle = { history += "${WORKER_NAME_1}; " }
        )
        val ctx = TestContext()

        worker.exec(ctx)

        assertEquals("", ctx.history)
    }

    @Test
    @JsName("worker_should_handle_exception")
    fun workerShouldHandleException() = runTest {
        val worker = CorWorker<TestContext>(
            title = WORKER_NAME_1,
            blockHandle = { throw RuntimeException(ERROR_MESSAGE) },
            blockExcept = { e -> history += e.message }
        )
        val ctx = TestContext()

        worker.exec(ctx)

        assertEquals(ERROR_MESSAGE, ctx.history)
    }
}