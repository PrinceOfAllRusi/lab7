package multilib.server.tools

import java.security.MessageDigest

class Hasher {
    private val sha384 = MessageDigest.getInstance("SHA-384")

    fun hash(string: String): String {
        val byteArray = sha384.digest(string.toByteArray())
        val builder = StringBuilder()
        for (byte in byteArray) {
            builder.append(String.format("%02x", byte))
        }
        return builder.toString()
    }
    fun compareHash(string: String, hash: String): Boolean { // возвращает true, едси хеши совпадают
        return hash(string) == hash
    }
}