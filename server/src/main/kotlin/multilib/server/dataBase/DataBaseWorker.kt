package multilib.server.dataBase

import multilib.utilities.input.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class DataBaseWorker {
    private lateinit var connection: Connection
    private lateinit var statement: Statement
    private lateinit var preparedStatement: PreparedStatement
    private lateinit var resultSet: ResultSet
    private var sql = ""
    private var input = InputSystem()

    fun getConnectionToDataBase() {
        try {
            Class.forName("org.postgresql.Driver")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        try {
            connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/lab7", "postgres", "8574")
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
    fun closeConnectionToDataBase() {
        connection.close()
    }
    fun registerUser(login: String, password: String) {
        sql = "INSERT INTO users (login, password) VALUES(?, ?)"
        try {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setString(1, login)
            preparedStatement.setString(2, password)

            preparedStatement.executeUpdate()

        } catch (e: SQLException) {
            e.printStackTrace()
        }
        preparedStatement.close()
        sql = ""
    }
    fun getUserInfoForLogIn(login: String, password: String): Boolean { //возвращает true, если такого пользователя нет
        sql = "SELECT NOT EXISTS(SELECT * FROM users WHERE login = ? AND password = ?)"
        var flag = true
        try {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setString(1, login)
            preparedStatement.setString(2, password)

            resultSet = preparedStatement.executeQuery()

            while (resultSet.next()) {
                flag = resultSet.getBoolean(1)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        preparedStatement.close()
        sql = ""
        return flag
    }
    fun getUserInfoForRegistration(login: String): Boolean { //возвращает true, если такой пользователь уже существует
        sql = "SELECT EXISTS(SELECT * FROM users WHERE login = ?)"
        var flag = false
        try {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setString(1, login)

            resultSet = preparedStatement.executeQuery()

            while (resultSet.next()) {
                flag = resultSet.getBoolean(1)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        preparedStatement.close()
        sql = ""
        return flag
    }
}