package ru.otus.osms.cor.test

import kotlinx.coroutines.test.runTest
import ru.otus.osms.cor.*
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class CorDslTest {
    private suspend fun execute(dsl: ICorExecDsl<TestContext>): TestContext {
        val ctx = TestContext()

        dsl.build().exec(ctx)

        return ctx
    }

    @Test
    @JsName("handle_should_execute")
    fun handleShouldExecute() = runTest {
        val chain = rootChain<TestContext> {
            worker {
                handle { history += "${WORKER_NAME_1}; " }
            }
        }
        val ctx = execute(chain)

        assertEquals("${WORKER_NAME_1}; ", ctx.history)
    }

    @Test
    @JsName("on_should_check_condition")
    fun onShouldCheckCondition() = runTest {
        val chain = rootChain<TestContext> {
            worker {
                on { status == TestContext.CorStatuses.ERROR }
                handle { history += "${WORKER_NAME_1}; " }
            }
            worker {
                on { status == TestContext.CorStatuses.NONE }
                handle {
                    history += "${WORKER_NAME_2}; "
                    status = TestContext.CorStatuses.FAILING
                }
            }
            worker {
                on { status == TestContext.CorStatuses.FAILING }
                handle { history += "${WORKER_NAME_3}; " }
            }
        }

        assertEquals("${WORKER_NAME_2}; ${WORKER_NAME_3}; ", execute(chain).history)
    }

    @Test
    @JsName("except_should_execute_when_exception")
    fun exceptShouldExecuteWhenException() = runTest {
        val chain = rootChain<TestContext> {
            worker {
                handle { throw RuntimeException("some error") }
                except { history += it.message }
            }
        }

        assertEquals("some error", execute(chain).history)
    }

    @Test
    @JsName("should_throw_when_exception_and_no_except")
    fun shouldThrowWhenExceptionAndNoExcept() = runTest {
        val chain = rootChain<TestContext> {
            worker("throw") { throw RuntimeException("some error") }
        }

        assertFails {
            execute(chain)
        }
    }

    @Test
    @JsName("complex_chain_example")
    fun complexChainExample() = runTest {
        val chain = rootChain<TestContext> {
            worker {
                title = "Инициализация статуса"
                description = "При старте обработки цепочки, статус еще не установлен. Проверяем его"

                on { status == TestContext.CorStatuses.NONE }
                handle { status = TestContext.CorStatuses.RUNNING }
                except { status = TestContext.CorStatuses.ERROR }
            }

            chain {
                on { status == TestContext.CorStatuses.RUNNING }

                worker(
                    title = "Лямбда обработчик",
                    description = "Пример использования обработчика в виде лямбды"
                ) {
                    some += 4
                }
            }

            parallel {
                on {
                    some < 15
                }

                worker(title = "Increment some") {
                    some++
                }
            }

            printResult()

        }.build()

        val ctx = TestContext()

        chain.exec(ctx)

        println("Complete: $ctx")
    }

    private fun ICorChainDsl<TestContext>.printResult() = worker(title = "Print example") {
        println("some = $some")
    }
}