package allForCommands.commands

import multilib.utilities.tools.Hasher
import multilib.utilities.commandsData.Token
import multilib.utilities.input.*
import multilib.utilities.result.Result
import java.time.LocalDateTime

abstract class AbstractCommand {
    private var description: String
    private var fields: Map<String, Map<String, String>>
    constructor() {
        this.description = ""
        this.fields = mapOf()
    }

    open fun action(map: Map<String, String?>, result: Result): Result {
        return result
    }
    open fun getDescription(): String = description
    open fun getFields() = fields
    open fun commandBuilding(mapData: MutableMap<String, String>, data: String): MutableMap<String, String> {
        val input = InputFile(data)

        for (key in fields.keys) {
            mapData[key] = input.getNextWord(null)
        }

        return mapData
    }
    open fun tokenRequirements(): Boolean = true
    open fun updateToken(token: Token): Token {
        val login = token.getLogin()
        val time = LocalDateTime.now()
        val tokenName = Hasher().hashToken(login, time)
        token.setTokenName(tokenName)
        token.setTime(time)
        return token
    }
}