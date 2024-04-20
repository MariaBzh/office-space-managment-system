package ru.otus.osms.repo.pg

data class SqlProperties(
    val url: String = "jdbc:postgresql://localhost:5432/osms",
    val user: String = "osms-app",
    val password: String = "admin123",
    val schema: String = "osms",
)
