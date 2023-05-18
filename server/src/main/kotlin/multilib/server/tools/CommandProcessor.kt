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

        var result: Result? = Result()
        var mapData: Map<String, String?>
        val serializer = Serializer()
        val socket = ServerSocket()

        var command = ""
        var receiveCommandsData = ClientCommandsData() //получаемые от клиента данные

        var xml = ""

        while ( true ) {

            socket.receive()

            if (clientList.getAddressList().size == 0 ||
                !clientList.getAddressList().contains(socket.getPort().toString() + socket.getHost().toString())) {

                clientList.getAddressList().add(socket.getPort().toString() + socket.getHost().toString())
                socket.sendCommandsData()
                input.outMsg("Client connected")
                continue
            }

            xml = socket.getXmlData()
            receiveCommandsData = serializer.deserialize(xml)

            command = receiveCommandsData.getName()

            input.outMsg("Client send command: " + command)

            result?.setMessage("")

            if ( !commandsList.containsCommand(command) ) {
                result!!.setMessage("This command does not exist")
            }
            else {
                try {
                    mapData = receiveCommandsData.getMapData()
                    result = commandsList.getCommand(command)?.action(mapData)

                } catch ( e: NumberFormatException ) {
                    input.outMsg("Wrong data")
                    if ( input.javaClass == InputFile("").javaClass ) {
                        continue
                    }
                } catch ( e: NullPointerException ) {
                    input.outMsg("Not all data entered")
                }
            }

            xml = serializer.serialize(result)
            socket.send(xml)

            if (result?.getExit() == true) {
                clientList.getAddressList().remove(socket.getPort().toString() + socket.getHost().toString())
                result.setExit(false)
            }
        }
    }
}