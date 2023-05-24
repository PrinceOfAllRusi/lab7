package multilib.server.commands

import allForCommands.commands.AbstractCommand
import multilib.server.dataBase.DataBaseWorker
import multilib.utilities.tools.Hasher
import multilib.utilities.commandsData.Token
import multilib.utilities.input.InputSystem
import multilib.utilities.result.Result
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LogIn: AbstractCommand(), KoinComponent {
    private val dataBaseWorker: DataBaseWorker by inject()
    private val hasher = Hasher()
    private val input = InputSystem()
    private val description: String = "allows you to login"
    private var fields: Map<String, Map<String, String>> = mapOf(
        "login" to mapOf(
            "title" to "Enter login\n",
            "type" to "String"
        ),
        "password" to mapOf(
            "title" to "Enter password\n",
            "type" to "String"
        )
    )

    override fun action(data: Map<String, String?>, result: Result): Result {
        val login = data["login"]!!
        val password = hasher.hashString(data["password"]!!)
        val user = dataBaseWorker.getUser(login, password)

        if (user.getLogin() == "") {
            result.setMessage("This user does not exist\n")
            return result
        }
        input.outMsg("Client log in\n")
        result.setMessage("You are log in\n")
        val token = Token()
        token.setAddress(data["address"]!!)
        token.setPort(data["port"]!!)
        token.setLogin(login)
        token.setTokenName(hasher.hashToken(login, token.getTime()))
        result.setToken(token)
        return result
    }
    override fun getDescription(): String = description
    override fun getFields() = fields
    override fun tokenRequirements(): Boolean = false

}