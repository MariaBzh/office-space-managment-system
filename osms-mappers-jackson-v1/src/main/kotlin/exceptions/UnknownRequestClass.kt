package ru.otus.osms.mappers.jackson.v1.exceptions

class UnknownRequestClass(clazz: Class<*>) : RuntimeException("Class $clazz cannot be mapped to OsmsContext")