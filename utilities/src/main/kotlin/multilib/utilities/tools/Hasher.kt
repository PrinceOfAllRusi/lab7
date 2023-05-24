package multilib.utilities.tools

import java.security.MessageDigest
import java.text.Format
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Hasher {
    private val sha384 = MessageDigest.getInstance("SHA-384")

    fun hashString(string: String): String {
        val byteArray = sha384.digest(string.toByteArray())
        val builder = StringBuilder()
        for (byte in byteArray) {
            builder.append(String.format("%02x", byte))
        }
        return builder.toString()
    }
    fun hashToken(login: String, time: LocalDateTime): String {
        val firstPart = hashString(login)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val secondPart = hashString(time.format(formatter))
        val thirdPart = hashString("DevolveConstrueSeriouslyWesternizeFurthermoreIndependence")

        return "$firstPart.$secondPart.$thirdPart"
    }
}