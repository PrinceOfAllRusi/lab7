package multilib.utilities.commandsData

class ClientCommandsData {
    private var name: String
    private var mapData: MutableMap<String, String?>
    private var token: Token
    private var commandsVersion: Int
    constructor() {
        name = ""
        mapData = mutableMapOf()
        token = Token()
        commandsVersion = 0
    }
    fun getName() = name
    fun setName(name: String) {
        this.name = name
    }
    fun getMapData() = mapData
    fun setMapData(mapCommands: MutableMap<String, String?>) {
        this.mapData = mapCommands
    }
    fun clearMap() {
        mapData.clear()
    }
    fun setToken(token: Token) {
        this.token = token
    }
    fun getToken() = token
    fun setCommandsVersion(commandsVersion: Int) {
        this.commandsVersion = commandsVersion
    }
    fun getCommandsVersion() = commandsVersion
}