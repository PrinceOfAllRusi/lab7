package multilib.server.commands

import allForCommands.commands.AbstractCommand
import multilib.server.dataBase.DataBaseWorker
import multilib.server.tools.Hasher
import multilib.utilities.commandsData.Token
import multilib.utilities.input.InputSystem
import multilib.utilities.result.Result
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tools.DataList

class LogIn: AbstractCommand(), KoinComponent {
    private val dataBaseWorker: DataBaseWorker by inject()
    private val clientList: DataList by inject()
    private val hasher = Hasher()
    private val input = InputSystem()
    private val description: String = "allows you to login"
    private var fields: Map<String, Map<String, String>> = mapOf(
        "login" to mapOf(
            "title" to "Enter login",
            "type" to "String"
        ),
        "password" to mapOf(
            "title" to "Enter password",
            "type" to "String"
        )
    )

    override fun action(data: Map<String, String?>, result: Result): Result {
        val login = data["login"]!!
        val password = hasher.hash(data["password"]!!)

        if (dataBaseWorker.getUserInfoForLogIn(login, password)) {
            result.setMessage("This user does not exist")
            return result
        }
        input.outMsg("Client log in")
        result.setMessage("You are log in")
        val token = Token()
        token.setAddress(data["address"]!!)
        token.setPort(data["port"]!!)
        token.setLogin(login)
        token.setHashToken()
        clientList.getTokenList().add(token)
        result.setToken(token)
        return result
    }
    override fun getDescription(): String = description
    override fun getFields() = fields
    override fun tokenRequirements(): Boolean = false

}