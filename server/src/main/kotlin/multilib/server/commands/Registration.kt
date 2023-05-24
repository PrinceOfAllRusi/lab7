package multilib.server.commands

import allForCommands.commands.AbstractCommand
import multilib.server.dataBase.DataBaseWorker
import multilib.utilities.tools.Hasher
import multilib.utilities.input.InputSystem
import multilib.utilities.result.Result
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Registration: AbstractCommand(), KoinComponent {
    private val dataBaseWorker: DataBaseWorker by inject()
    private val hasher = Hasher()
    private val input = InputSystem()
    private val description: String = "allows you to register"
    private var fields: Map<String, Map<String, String>> = mapOf(
        "login" to mapOf(
            "title" to "Enter login\n",
            "type" to "String"
        ),
        "password" to mapOf(
            "title" to "Enter password\n",
            "type" to "String",
            "minLength" to "8"
        )
    )
    override fun action(data: Map<String, String?>, result: Result): Result {
        val login = data["login"]!!
        val password = hasher.hashString(data["password"]!!)
        val user = dataBaseWorker.getUser(login, password)

        if (user.getLogin() == login) {
            result.setMessage("This name already exist\n")
            return result
        }
        dataBaseWorker.registerUser(login, password)
        result.setMessage("Done\n")
        input.outMsg("Client registered\n")
        return result
    }
    override fun getDescription(): String = description
    override fun getFields() = fields
    override fun tokenRequirements(): Boolean = false

}
