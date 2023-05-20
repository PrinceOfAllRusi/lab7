package multilib.server.dataBase

import multilib.utilities.input.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import organization.MyCollection
import organization.Organization
import tools.CreateOrganization
import java.sql.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset


class DataBaseWorker: KoinComponent {
    private lateinit var connection: Connection
    private lateinit var preparedStatement: PreparedStatement
    private lateinit var resultSet: ResultSet
    private var sql = ""
    private var input = InputSystem()
    private val orgs: MyCollection<Organization> by inject()
    private val creator: CreateOrganization by inject()

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
    fun fillOrgsList() {
        sql = "select o.id, o.name, o.creationdate, o.annualturnover, o.employeescount, o.type, c.x, c.y," +
                " a.street, a.zipcode from organization as o natural join coordinates as c natural join address as a;"
        val orgData = mutableMapOf<String, String>()
        var id = 0
        var crearionDate = LocalDateTime.now()
        var ts: Timestamp
        var organization = Organization()
        try {
            preparedStatement = connection.prepareStatement(sql)
            resultSet = preparedStatement.executeQuery()

            while (resultSet.next()) {
                id = resultSet.getInt(1)
                orgData.put("name", resultSet.getString(2))
                ts = resultSet.getTimestamp(3)
                crearionDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(ts.getTime()), ZoneOffset.UTC)
                orgData.put("annualTurnover", resultSet.getString(4))
                orgData.put("employeesCount", resultSet.getString(5))
                orgData.put("type", resultSet.getString(6))
                orgData.put("x", resultSet.getString(7))
                orgData.put("y", resultSet.getString(8))
                orgData.put("street", resultSet.getString(9))
                orgData.put("zipCode", resultSet.getString(10))
                organization = creator.create(orgData, null)
                organization.setId(id)
                organization.setCreationDate(crearionDate)
                orgs.add(organization)
            }

        } catch (e: SQLException) {
            e.printStackTrace()
        }
        preparedStatement.close()
        sql = ""
    }
}