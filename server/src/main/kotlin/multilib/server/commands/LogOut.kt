package multilib.server.commands

import allForCommands.commands.AbstractCommand
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

    override fun action(data: Map<String, String?>): Result {
        val tokenName = data["token"]
        var lastToken = Token()
        for (token in clientList.getTokenList()) {
            if (token.getToken() == tokenName) {
                lastToken = token
                break
            }
        }
        clientList.getTokenList().remove(lastToken)
        val result = Result()
        result.setMessage("You are log out")
        input.outMsg("Client log out")

        return result
    }
    override fun getDescription(): String = description
    override fun getFields() = fields
    override fun tokenRequirements(): Boolean = false
}