package multilib.server.commands

import allForCommands.commands.AbstractCommand
import allForCommands.commands.Save
import multilib.utilities.commandsData.Token
import multilib.utilities.input.InputSystem
import multilib.utilities.result.Result

class LogOut: AbstractCommand() {
    private val input = InputSystem()
    private val description: String = "log out from app"
    private var fields: Map<String, Map<String, String>> = mapOf()

    override fun action(data: Map<String, String?>, result: Result): Result {
        val save = Save()
        save.action(data, result)
        result.setMessage("You are log out\n")
        input.outMsg("Client log out\n")
        result.setToken(Token())

        return result
    }
    override fun getDescription(): String = description
    override fun getFields() = fields
}