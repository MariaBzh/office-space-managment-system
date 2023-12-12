package ru.otus.osms.mappers.kmp.v1.exceptions

import ru.otus.osms.common.models.OsmsCommand

class UnknownOsmsCommand(command: OsmsCommand) : Throwable("Wrong command $command at mapping toTransport stage")