package multilib.client

import multilib.client.modul.SingletonObject
import tools.CommandProcessor
import multilib.utilities.input.*
import org.koin.core.context.GlobalContext.startKoin

fun main() {

    startKoin {
        modules(SingletonObject.mod)
    }
    val input= InputSystem()

    val commandProcessor: CommandProcessor = CommandProcessor()
    commandProcessor.process(input)
}