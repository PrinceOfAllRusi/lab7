package multilib.server.commands

import allForCommands.commands.AbstractCommand
import allForCommands.commands.Save
import multilib.utilities.commandsData.Token
import multilib.utilities.input.InputSystem
import multilib.utilities.result.Result
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tools.DataList

class LogOut: AbstractCommand(), KoinComponent {
    private val clientList: DataList by inject()
    private val input = InputSystem()
    private val description: String = "log out from app"
    private var fields: Map<String, Map<String, String>> = mapOf()

    override fun action(data: Map<String, String?>, result: Result): Result {
        val tokenName = data["token"]!!
        val lastToken = clientList.getTokenFromTokenList(tokenName)
        val save = Save()
        save.action(data, result)
        clientList.getTokenList().remove(lastToken)
        result.setMessage("You are log out\n")
        input.outMsg("Client log out\n")
        result.setToken(Token())

        return result
    }
    override fun getDescription(): String = description
    override fun getFields() = fields
    override fun tokenRequirements(): Boolean = false
}