package allForCommands.commands

import multilib.utilities.result.Result

class Exit : AbstractCommand() {
    private val save = Save()
    private val description: String = "terminate program"
    private var fields: Map<String, Map<String, String>> = mapOf()

    override fun action(data: Map<String, String?>, result: Result): Result {
        val newResult = save.action(data, result)
        newResult.setExit(true)

        return newResult
    }
    override fun getDescription(): String = description
    override fun getFields() = fields
    override fun tokenRequirements(): Boolean = false
}