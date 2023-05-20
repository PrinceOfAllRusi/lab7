package multilib.utilities.commandsData

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class Token {
    private var token: String
    private var time: LocalDateTime
    private var address: String
    private var port: String
    constructor() {
        token = ""
        time = LocalDateTime.now()
        address = ""
        port = ""
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
    fun getToken() = token
    fun setToken(token: String) {
        this.token = token
    }
    fun setHashToken() {
        token = address.hashCode().toString()
    }
    fun setPort(host: String) {
        this.port = host
    }
    fun getPort() = port
    fun validityCheck(): Boolean { //вернет true, если токен валидный
        val hours = time.until(LocalDateTime.now(), ChronoUnit.HOURS)
        return hours < 1
    }
}