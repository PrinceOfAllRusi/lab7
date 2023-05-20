package multilib.server.commands

import allForCommands.commands.AbstractCommand
import multilib.server.dataBase.DataBaseWorker
import multilib.server.tools.Hasher
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
            "title" to "Enter login",
            "type" to "String"
        ),
        "password" to mapOf(
            "title" to "Enter password",
            "type" to "String",
            "minLength" to "8"
        )
    )
    override fun action(data: Map<String, String?>, result: Result): Result {
        val login = data["login"]!!
        val password = hasher.hash(data["password"]!!)

        if (dataBaseWorker.getUserInfoForRegistration(login)) {
            result.setMessage("This name already exist")
            return result
        }
        dataBaseWorker.registerUser(login, password)
        result.setMessage("Done")
        input.outMsg("Client registered")
        return result
    }
    override fun getDescription(): String = description
    override fun getFields() = fields
    override fun tokenRequirements(): Boolean = false

}
