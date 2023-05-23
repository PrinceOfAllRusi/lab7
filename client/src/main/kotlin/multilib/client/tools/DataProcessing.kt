package tools

import multilib.client.tools.ScriptProcessor
import multilib.utilities.commandsData.ClientCommandsData
import organization.OrganizationType
import multilib.utilities.input.*

class DataProcessing {

    fun setData(input: Input, data: MutableMap<String, Map<String, String>>,
                sendCommandsData: ClientCommandsData): ClientCommandsData {

        if (data.size == 0) {
            return sendCommandsData
        }
        if (data.containsKey("script")) {
            val env = input.getNextWord(data["script"]!!["title"])
            val scriptProcessor = ScriptProcessor()
            val script = scriptProcessor.action(env)
            sendCommandsData.getMapData()["script"] = script
            scriptProcessor.clearScript()
            return sendCommandsData
        }

        if (data.containsKey("value")) {
            val value = input.getNextWord(null)

            try {
                when (data["value"]!!["type"]) {
                    "String" -> {
                        sendCommandsData.getMapData()["value"] = value
                    }
                    "Int" -> {
                        value.toInt()
                        if (data["value"]!!.containsKey("min")) {
                            if (value.toInt() < data["value"]!!["min"]!!.toInt()) {
                                input.outMsg("Too small value")
                                return sendCommandsData
                            }
                        }
                        sendCommandsData.getMapData()["value"] = value

                        if (data.size == 1) {
                            return sendCommandsData
                        }
                    }
                }
            } catch (e: NullPointerException) {
                input.outMsg("Invalid data type\n")
                return sendCommandsData
            }
        }

        var value = ""
        val commandData = data.filterKeys { !it.contains("value") }
        var map: Map<String, String>

        for (key in commandData.keys) {
            map = commandData[key]!!
            while (true) {
                value = input.getNextWord(map["title"])
                if (value.isBlank()) {
                    if (map.containsKey("null")) {
                        sendCommandsData.getMapData()[key] = value
                        break
                    } else {
                        input.outMsg("The field can not be empty\n")
                        continue
                    }
                }
                try {
                    when (map["type"]) {
                        "Int" -> value.toInt()
                        "Long" -> value.toLong()
                        "Double" -> value.toDouble()
                        "OrganizationType" -> OrganizationType.valueOf(value.uppercase())
                    }
                } catch (e: NullPointerException) {
                    input.outMsg("The field can not be empty\n")
                    continue
                } catch (e: IllegalArgumentException) {
                    input.outMsg("Invalid data type\n")
                    continue
                }
                if (map.containsKey("min")) {
                    if (value.toInt() < map["min"]!!.toInt()) {
                        input.outMsg("Too small value\n")
                        continue
                    }
                } else if (map.containsKey("max")) {
                    if (value.toInt() > map["max"]!!.toInt()) {
                        input.outMsg("Too much value\n")
                        continue
                    }
                } else if (map.containsKey("maxLength")) {
                    if (value.length > map["maxLength"]!!.toInt()) {
                        input.outMsg("Too much value\n")
                        continue
                    }
                } else if(map.containsKey("minLength")) {
                    if (value.length < map["minLength"]!!.toInt()) {
                        input.outMsg("Too small value\n")
                        continue
                    }
                }

                sendCommandsData.getMapData()[key] = value
                break
            }
        }

        return sendCommandsData
    }
}