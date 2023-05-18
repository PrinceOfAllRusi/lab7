package multilib.server.commands

import allForCommands.commands.AbstractCommand
import multilib.server.dataBase.DataBaseWorker
import multilib.utilities.result.Result

class LogIn: AbstractCommand() {
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
        result.setMessage("You are log in")
        dataBaseWorker.closeConnectionToDataBase()
        return result
    }
    override fun getDescription(): String = description
    override fun getFields() = fields

}