package tools.file

import multilib.utilities.result.Result
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.IOException

class WriteFile {
    fun write(way: String, dataStr: String): Result {
        val result = Result()
        val writter: BufferedWriter = BufferedWriter( FileWriter(way) )
        try {
            writter.write(dataStr)
        } catch (e: IOException) {
            result.setMessage("Access denied")
            return result
        } finally {
                writter.close()
        }
        result.setMessage("Recording completed successfully")

        return result
    }
}

