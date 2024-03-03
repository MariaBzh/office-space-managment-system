package ru.otus.osms.logging.common

enum class LogLevel(
    private val levelId: Int,
    private val levelCode: String,
) {
    ERROR(40, "ERROR"),
    WARN(30, "WARN"),
    INFO(20, "INFO"),
    DEBUG(10, "DEBUG"),
    TRACE(0, "TRACE");

    @Suppress("unused")
    fun toInt(): Int {
        return levelId
    }

    /**
     * Returns the string representation of this Level.
     */
    override fun toString(): String {
        return levelCode
    }
}