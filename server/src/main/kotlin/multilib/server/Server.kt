package multilib.server

import kotlinx.coroutines.runBlocking
import multilib.server.modul.SingletonObject.mod
import tools.CommandProcessor
import org.koin.core.context.GlobalContext.startKoin
import multilib.utilities.input.*

fun main(): Unit = runBlocking {
    startKoin {
        modules(mod)
    }
    val input = InputSystem()
    val commandProcessor = CommandProcessor()
    commandProcessor.process(input)

}