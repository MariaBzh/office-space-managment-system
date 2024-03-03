package ru.otus.osms.logging.common

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class OsmsLoggerProvider(
    private val provider: (String) -> IOsmsLogWrapper = { IOsmsLogWrapper.DEFAULT }
) {
    fun logger(loggerUid: String) = provider(loggerUid)
    fun logger(clazz: KClass<*>) = provider(clazz.qualifiedName ?: clazz.simpleName ?: "(unknown)")

    fun logger(function: KFunction<*>) = provider(function.name)
}