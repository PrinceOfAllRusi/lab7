package multilib.utilities.commandsData

import multilib.utilities.tools.Hasher
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class Token {
    private var tokenName: String
    private var time: LocalDateTime
    private var address: String
    private var port: String
    private var login: String
    constructor() {
        tokenName = ""
        time = LocalDateTime.now()
        address = ""
        port = ""
        login = ""
    }
    fun getTime() = time
    fun setTime(time: LocalDateTime) {
        this.time = time
    }
    fun setTimeNow() {
        time = LocalDateTime.now()
    }
    fun setAddress(address: String) {
        this.address = address
    }
    fun getAddress() = address
    fun getTokenName() = tokenName
    fun setTokenName(token: String) {
        this.tokenName = token
    }
    fun setPort(host: String) {
        this.port = host
    }
    fun getPort() = port
    fun setLogin(login: String) {
        this.login = login
    }
    fun getLogin() = login
    fun validityCheck(): Boolean { //вернет true, если токен валидный
        val hours = time.until(LocalDateTime.now(), ChronoUnit.HOURS)
        val verifiableTokenName = Hasher().hashToken(login, time)
        return (hours < 1 && verifiableTokenName == tokenName)
    }
}