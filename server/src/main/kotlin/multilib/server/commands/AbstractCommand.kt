package allForCommands.commands

import multilib.utilities.input.*
import multilib.utilities.result.Result

abstract class AbstractCommand {
    private var description: String
    private var fields: Map<String, Map<String, String>>
    constructor() {
        this.description = ""
        this.fields = mapOf()
    }

    open fun action(map: Map<String, String?>): Result {
        return Result()
    }
    open fun getDescription(): String = description
    open fun getFields() = fields
    open fun commandBuilding(mapData: MutableMap<String, String>, data: String): MutableMap<String, String> {
        val input = InputFile(data)

        for (key in fields.keys) {
            mapData.put(key, input.getNextWord(null))
        }

        return mapData
    }
    open fun tokenRequirements(): Boolean = true
}