package multilib.client.tools.socket

import multilib.utilities.input.InputSystem
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException

class ClientSocket {
    private val port = 1313
    private val host: InetAddress = InetAddress.getLocalHost()
    private val clientSocket  = DatagramSocket()
    private var sendingDataBuffer = ByteArray(65535)
    private val receivingDataBuffer = ByteArray(65535)
    private lateinit var sendingPacket: DatagramPacket
    private var receivingPacket = DatagramPacket(receivingDataBuffer, receivingDataBuffer.size)
    private var receivedData = ""
    private val input = InputSystem()

    fun send(s: String) {
        sendingDataBuffer = s.toByteArray()
        sendingPacket = DatagramPacket(sendingDataBuffer, sendingDataBuffer.size, host, port)
        clientSocket.send(sendingPacket)
    }
    fun receive(): String {
        receivedData = ""

        clientSocket.soTimeout = 100000
        try {
            clientSocket.receive(receivingPacket)
            receivedData = String(receivingPacket.data, 0, receivingPacket.length)
        } catch (e: SocketTimeoutException) {
            input.outMsg("Connection timeout\n")
        }

        return receivedData
    }
}
