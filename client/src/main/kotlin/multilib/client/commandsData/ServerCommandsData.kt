package multilib.client.commandsData

class ServerCommandsData {
    private var mapCommands: Map<String, MutableMap<String, Map<String, String>>>
    private var commandsVersion: Int
    constructor() {
        mapCommands = mapOf()
        commandsVersion = 0
    }
    fun getMapCommands() = mapCommands
    fun setMapCommands(mapCommands: Map<String, MutableMap<String, Map<String, String>>>){
        this.mapCommands = mapCommands
    }
    fun getCommandsVersion() = commandsVersion
    fun setCommandsVersion(version: Int) {
        this.commandsVersion = version
    }

}