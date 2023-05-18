package multilib.server.dataBase

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class DataBaseWorker {
    private lateinit var connection: Connection
    private lateinit var statement: Statement
    private lateinit var resultSet: ResultSet

    fun getUserInfo() {
        try {
            Class.forName("org.postgresql.Driver")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        try {
            connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/lab7", "postgres", "8574")
            statement = connection.createStatement()
            resultSet = statement.executeQuery("SELECT * from users")

            while (resultSet.next()) {
                println(resultSet.getString("login"))
                println(resultSet.getString("password"))
            }
            statement.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}
