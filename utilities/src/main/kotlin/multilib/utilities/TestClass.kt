package multilib.utilities

import java.util.Scanner

fun main() {
    val set = setOf("one", "two", "three")
    val sumLength = set.map{it.length}.reduce{acc, it -> acc + it}
    println(sumLength)

    val list = listOf(
        listOf("str", "str"),
        listOf("str", "str")
    )
    val newList = list.flatMap { it }

    val map: Map<String, Int> = set.associateWith { it.length }
}

class Clinett {
    fun subscribe(sender: Sender) {
        println("Sub to channel")
        sender.addClient(this)
    }
    fun getMsg(s: String) {
        println(s)
    }
}
class Sub {
    var msg = ""
    fun setMsg() {
        msg = readln()
    }
}
class Sender {
    var msg = ""
    val listName = mutableListOf<Clinett>()
    fun sendMsg(s: String) {
        if (msg != s) {
            listName.forEach{it.getMsg(s)}
        }
    }
    fun addClient(client: Clinett) {
        listName.add(client)
    }

}