package tools

import allForCommands.commands.AbstractCommand

class CommandsList {
    private var listCommands: Map<String, AbstractCommand> = mapOf()
    private var commandsVersion: Int = 1
    constructor(listCommands: Map<String, AbstractCommand>, version: Int) {
        this.listCommands = listCommands
        this.commandsVersion = version
    }
    fun containsCommand(command: String): Boolean = listCommands.containsKey(command)
    fun getCommand(command: String): AbstractCommand? = listCommands[command]
    fun getCommandsVersion() = commandsVersion
    fun getDescription(): String {
        val s = StringBuilder()
        for (command in listCommands.keys) {
            s.append(command)
            s.append(" : ")
            s.append(listCommands[command]!!.getDescription())
            s.append("\n")
        }
        return s.toString()
    }
}