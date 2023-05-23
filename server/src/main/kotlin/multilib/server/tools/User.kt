package multilib.server.tools

class User {
    private var id = 0
    private var login = ""
    private var password = ""
    fun getId() = id
    fun setId(id: Int) {
        this.id = id
    }
    fun getLogin() = login
    fun setLogin(login: String) {
        this.login = login
    }
    fun getPassword() = password
    fun setPassword(password: String) {
        this.password = password
    }
}