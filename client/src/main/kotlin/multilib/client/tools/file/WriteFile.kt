package multilib.client.tools.file

import multilib.utilities.result.Result
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.IOException

class WriteFile {

    fun write(way: String, dataStr: String): Result {
        val env = way
        val result = Result()

        val path = System.getenv(env)
        if (path == null) {
            result.setMessage("This variable does not exist")
            return result
        }

        val writter: BufferedWriter = BufferedWriter( FileWriter(path) )
        try {
            writter.write(dataStr)
        } catch (e: IOException) {
            result.setMessage("Access denied")
            return result
        } finally {
            if ( writter != null ) {
                writter.close()
            }
        }

        result.setMessage("Recording completed successfully")

        return result
    }
}

