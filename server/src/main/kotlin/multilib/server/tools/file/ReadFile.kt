package tools.file

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import multilib.utilities.input.*
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader

class ReadFile: KoinComponent {

    private val absoluteWay: ArrayList<String> by inject()

    fun read(input: Input): String? {

        val env = input.getNextWord("Enter an environment variable containing the path to the file")
        val s = StringBuilder()

        for(i in absoluteWay) {
            if (i == env) {
                return null
            }
        }
        absoluteWay.add(env)


        val path = System.getenv(env)
        if (path == null) {
            input.outMsg("This variable does not exist")
            return null
        }
        var reader: InputStreamReader? = null

        try {
            reader = InputStreamReader(FileInputStream(path), "UTF8")
            var i = -1

            while ((reader.read().also { i = it }) != -1) {
                s.append(i.toChar())
            }

            s.append("\nexit")

        } catch (e: FileNotFoundException) {
            input.outMsg("This file does not exist")
        } catch (e: IOException) {
            input.outMsg("Incorrect data entry")
        } finally {
            if (reader != null) {
                reader.close()
            }
        }

        return s.toString()
    }
}