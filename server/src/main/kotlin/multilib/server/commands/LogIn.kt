package multilib.server.commands

import allForCommands.commands.AbstractCommand
import multilib.server.dataBase.DataBaseWorker
import multilib.utilities.commandsData.Token
import multilib.utilities.input.InputSystem
import multilib.utilities.result.Result
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tools.DataList

class LogIn: AbstractCommand(), KoinComponent {
    private val clientList: DataList by inject()
    private val input = InputSystem()
    private val dataBaseWorker = DataBaseWorker()
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

    override fun action(data: Map<String, String?>): Result {
        dataBaseWorker.getConnectionToDataBase()
        val result = Result()
        val login = data["login"]!!
        val password = data["password"]!!

        if (dataBaseWorker.getUserInfoForLogIn(login, password)) {
            result.setMessage("This user does not exist")
            return result
        }
        dataBaseWorker.closeConnectionToDataBase()
        input.outMsg("Client log in")
        result.setMessage("You are log in")
        val token = Token()
        token.setAddress(data["address"]!!)
        token.setPort(data["port"]!!)
        token.setHashToken()
        clientList.getTokenList().add(token)
        return result
    }
    override fun getDescription(): String = description
    override fun getFields() = fields
    override fun tokenRequirements(): Boolean = false

}