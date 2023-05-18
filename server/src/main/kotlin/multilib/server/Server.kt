package multilib.server

import multilib.server.modul.SingletonObject.mod
import tools.CommandProcessor
import org.koin.core.context.GlobalContext.startKoin
import multilib.utilities.input.*

fun main() {

    startKoin {
        modules(mod)
    }

    val input = InputSystem()

    val commandProcessor: CommandProcessor = CommandProcessor()
    commandProcessor.process(input)
}