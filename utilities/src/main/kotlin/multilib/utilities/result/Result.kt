package multilib.utilities.result

import multilib.utilities.commandsData.Token

class Result {
    private var exit: Boolean?
    private var message: String
    private var token: Token

    constructor() {
        this.exit = false
        this.message = ""
        this.token = Token()
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