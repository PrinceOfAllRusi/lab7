package tools

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import multilib.server.dataBase.DataBaseWorker
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import multilib.utilities.input.*
import multilib.utilities.result.Result
import multilib.server.tools.socket.ServerSocket
import multilib.utilities.commandsData.*
import multilib.utilities.serializ.Serializer


class CommandProcessor: KoinComponent {

    private val dataBaseWorker: DataBaseWorker by inject()
    private val commandsList: CommandsList by inject()

    suspend fun process(input: Input) = withContext(Dispatchers.Default) {

        val channel1 = Channel<Pair<ClientCommandsData, Result>>()
        val channel2 = Channel<Pair<ClientCommandsData, Result>>()
        val channel3 = Channel<Result>()
        var mapData: MutableMap<String, String?>
        val serializer = Serializer()
        val socket = ServerSocket()
        dataBaseWorker.getConnectionToDataBase()
        dataBaseWorker.fillOrgsList()
        val s = StringBuilder()
        s.append("register : ")
        s.append(commandsList.getCommand("register")!!.getDescription() + "\n")
        s.append("log_in : ")
        s.append(commandsList.getCommand("log_in")!!.getDescription() + "\n")
        s.append("exit : ")
        s.append(commandsList.getCommand("exit")!!.getDescription() + "\n")

        var command = ""
        var requirements = true
        var xml = ""

        launch {
            while (true) {
                val result = Result()
                socket.receive()
                result.port = socket.getPort()
                result.host = socket.getHost()
                xml = socket.getXmlData()
                val receiveCommandsData: ClientCommandsData = serializer.deserialize(xml)
                result.setToken(receiveCommandsData.getToken())
                channel1.send(Pair(receiveCommandsData, result))
            }
        }
        launch {
            for (pair in channel1) {
                val receiveCommandsData = pair.first
                val result = pair.second
                if (commandsList.getCommandsVersion() != receiveCommandsData.getCommandsVersion()) {
                    socket.sendCommandsData()
                    input.outMsg("Client connected without registration\n")
                    continue
                }
                CoroutineScope(Job()).launch {
//                    delay(10000)
                    command = receiveCommandsData.getName()
                    input.outMsg("Client send command: $command\n")
                    requirements = commandsList.getCommand(command)!!.tokenRequirements()
                    if ((receiveCommandsData.getToken().getTokenName() == "" ||
                                !receiveCommandsData.getToken().validityCheck()) && requirements) {
                        result.setMessage("You cannot do it. Login or register\n$s")
                        channel3.send(result)
                    } else channel2.send(Pair(receiveCommandsData, result))
                }
            }
        }
        launch {
            for (pair in channel2) {
                val receiveCommandsData = pair.first
                var result = pair.second
                CoroutineScope(Job()).launch {
                    try {
                        mapData = receiveCommandsData.getMapData()
                        mapData["address"] = socket.getHost().toString()
                        mapData["port"] = socket.getPort().toString()
                        mapData["userLogin"] = socket.getToken()!!.getLogin()
                        result = commandsList.getCommand(command)?.action(mapData, result)!!
                    } catch ( e: NumberFormatException ) {
                        input.outMsg("Wrong data\n")
                        if ( input.javaClass == InputFile("").javaClass ) {}
                    } catch ( e: NullPointerException ) {
                        input.outMsg("Not all data entered\n")
                    }
                    if (requirements && result.getToken().getTokenName() != "") {
                        result.setToken(commandsList.getCommand(command)?.updateToken(receiveCommandsData.getToken())!!)
                    }
                    channel3.send(result)
                }
            }
        }
        launch {
            for (result in channel3) {
                xml = serializer.serialize(result)
                val host = result.host
                val port = result.port

                socket.send(xml, host, port)
                if (result.getExit() == true) {
                    result.setExit(false)
                }
            }
        }
    }
}