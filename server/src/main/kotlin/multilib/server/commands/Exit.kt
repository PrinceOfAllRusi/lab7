package allForCommands.commands

import multilib.utilities.result.Result

class Exit : AbstractCommand() {
    private val description: String = "terminate program"
    private var fields: Map<String, Map<String, String>> = mapOf()

    override fun action(data: Map<String, String?>, result: Result): Result {
        if (result.getToken().getTokenName() != "") {
            result.setMessage("You can not exit. Log out first\n")
            return result
        }
        result.setExit(true)

        return result
    }
    override fun getDescription(): String = description
    override fun getFields() = fields
    override fun tokenRequirements(): Boolean = false
}