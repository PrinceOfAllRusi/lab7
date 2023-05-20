package multilib.server.commandsData

import allForCommands.commands.*
import multilib.server.commands.LogIn
import multilib.server.commands.LogOut
import multilib.server.commands.Registration

class ServerCommandsData {
    private var mapCommands: Map<String, Map<String, Map<String, String>>>
    private var commandsVersion: Int
    constructor(){
        mapCommands = mapOf("help" to Help().getFields(),
            "info" to Info().getFields(), "show" to Show().getFields(), "add" to Add().getFields(),
            "update" to Update().getFields(), "remove_by_id" to RemoveById().getFields(),
            "clear" to Clear().getFields(), "execute_script" to ExecuteScript().getFields(),
            "exit" to Exit().getFields(), "insert_at" to InsertAt().getFields(),
            "remove_at" to RemoveAt().getFields(), "remove_lower" to RemoveLower().getFields(),
            "remove_all_by_employees_count" to RemoveAllByEmployeesCount().getFields(),
            "count_greater_than_annual_turnover" to CountGreaterThanAnnualTurnover().getFields(),
            "filter_starts_with_name" to FilterStartsWithName().getFields(),
            "register" to Registration().getFields(), "log_in" to LogIn().getFields(),
            "log_out" to LogOut().getFields())
        commandsVersion = 1
    }
    fun getMapCommands() = mapCommands
    fun setMapCommands(mapCommands: Map<String, Map<String, Map<String, String>>>){
        this.mapCommands = mapCommands
    }
    fun getCommandsVersion() = commandsVersion
    fun setCommandsVersion(version: Int) {
        this.commandsVersion = version
    }

}