package tools

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import multilib.utilities.input.*
import multilib.utilities.result.Result
import multilib.server.tools.socket.ServerSocket
import multilib.utilities.commandsData.*
import multilib.utilities.serializ.Serializer


class CommandProcessor: KoinComponent {

    private val commandsList: CommandsList by inject()
    private val clientList: DataList by inject()

    fun process(input: Input) {

        var result = Result()
        var mapData: Map<String, String?>
        val serializer = Serializer()
        val socket = ServerSocket()
        var registrationRequirement = true

        var command = ""
        var receiveCommandsData = ClientCommandsData() //получаемые от клиента данные

        var xml = ""

        while ( true ) {

            result.setMessage("")
            socket.receive()
            xml = socket.getXmlData()
            receiveCommandsData = serializer.deserialize(xml)

            if (commandsList.getCommandsVersion() != receiveCommandsData.getCommandsVersion()) {
                socket.sendCommandsData()
                input.outMsg("Client connected without registration")
                continue
            }
            command = receiveCommandsData.getName()
            input.outMsg("Client send command: $command")

            if (receiveCommandsData.getToken()!!.getToken() == "" &&
                commandsList.getCommand(command)!!.tokenRequirements()) {
                val s = StringBuilder()
                s.append("register : ")
                s.append(commandsList.getCommand("register")!!.getDescription() + "\n")
                s.append("log_in : ")
                s.append(commandsList.getCommand("log_in")!!.getDescription() + "\n")
                s.append("exit : ")
                s.append(commandsList.getCommand("exit")!!.getDescription() + "\n")
                result.setMessage("You cannot do it. Login or register\n$s")
                xml = serializer.serialize(result)
                socket.send(xml)
                continue
            }
            try {
                mapData = receiveCommandsData.getMapData()
                mapData.put("token", receiveCommandsData.getToken()!!.getToken())
                mapData.put("address", socket.getHost().toString())
                mapData.put("port", socket.getPort().toString())
                result = commandsList.getCommand(command)?.action(mapData)!!
            } catch ( e: NumberFormatException ) {
                input.outMsg("Wrong data")
                if ( input.javaClass == InputFile("").javaClass ) {
                    continue
                }
            } catch ( e: NullPointerException ) {
                input.outMsg("Not all data entered")
            }
            xml = serializer.serialize(result)
            socket.send(xml)

            if (result.getExit() == true) {
                clientList.getTokenList().remove(socket.getToken())
                result.setExit(false)
            }
        }
    }
}