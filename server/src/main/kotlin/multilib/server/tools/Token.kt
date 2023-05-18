package multilib.server.tools

import java.time.LocalDateTime

class Token {
    private var token = ""
    private var time = LocalDateTime.now()
    private var address = ""
    fun setTime() {
        time = LocalDateTime.now()
    }
    fun getTime() = time
    fun setAddress(address: String) {
        this.address = address
    }
    fun getAddress() = address
    fun setToken() {
        token = address.hashCode().toString()
    }
    fun getToken() = token
}