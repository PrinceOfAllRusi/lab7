package multilib.utilities.result

import multilib.utilities.commandsData.Token
import java.net.InetAddress

class Result {
    private var exit: Boolean?
    private var message: String
    private var token: Token
    var host: InetAddress
    var port: Int

    constructor() {
        this.exit = false
        this.message = ""
        this.token = Token()
        this.host = InetAddress.getLocalHost()
        this.port = 0
    }
    fun getExit(): Boolean? {
        return exit
    }
    fun setExit(exit: Boolean?) {
        this.exit = exit
    }

    fun getMessage(): String = message

    fun setMessage(message: String) {
        this.message = message
    }
    fun getToken() = token
    fun setToken(token: Token) {
        this.token = token
    }
}