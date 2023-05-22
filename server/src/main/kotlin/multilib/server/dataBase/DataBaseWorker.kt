package multilib.server.dataBase

import multilib.utilities.input.*
import multilib.utilities.result.Result
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
            input.outMsg("Database access error")
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
            input.outMsg("Database access error")
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
            input.outMsg("Database access error")
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
            input.outMsg("Database access error when adding user")
        }
        preparedStatement.close()
        sql = ""
        return flag
    }
    fun fillOrgsList() {
        sql = "select * from organization;"
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
                orgData.put("x", resultSet.getString(3))
                orgData.put("y", resultSet.getString(4))
                ts = resultSet.getTimestamp(5)
                crearionDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(ts.getTime()), ZoneOffset.UTC)
                orgData.put("annualTurnover", resultSet.getString(6))
                orgData.put("employeesCount", resultSet.getString(7))
                orgData.put("type", resultSet.getString(8))
                orgData.put("street", resultSet.getString(9))
                orgData.put("zipCode", resultSet.getString(10))
                organization = creator.create(orgData, null)
                organization.setId(id)
                organization.setCreationDate(crearionDate)
                orgs.add(organization)
            }
        } catch (e: SQLException) {
            input.outMsg("Database access error")
        }
        preparedStatement.close()
        sql = ""
    }
    fun getUserId(login: String): Int? {//возвращает id нужного пользователя
        var id = 0
        sql = "SELET * FROM users WHERE login = ?;"
        try {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setString(1, login)
            resultSet = preparedStatement.executeQuery()
            id = resultSet.getInt(1)
        } catch (e: SQLException) {
            input.outMsg("Database access error when searching user")
            return null
        }
        return id
    }
    fun addOrg(organization: Organization, result: Result): Result {
        sql = "INSERT INTO organization VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
        val login = result.getToken().getLogin()
        val id = getUserId(login)
        if (id == null) {
            result.setMessage("Error, please try again")
            return result
        }
        try {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setString(1, organization.getId().toString())
            preparedStatement.setString(2, organization.getName())
            preparedStatement.setString(3, organization.getCoordinatesX())
            preparedStatement.setString(4, organization.getCoordinatesY())
            preparedStatement.setString(5, organization.getCreationDate().toString())
            preparedStatement.setString(6, organization.getAnnualTurnover().toString())
            preparedStatement.setString(7, organization.getEmployeesCount().toString())
            preparedStatement.setString(8, organization.getType().toString())
            preparedStatement.setString(9, organization.getPostalAddressStreet())
            preparedStatement.setString(10, organization.getPostalAddressZipCode())
            preparedStatement.setString(11, id.toString())
            resultSet = preparedStatement.executeQuery()

            orgs.add(organization)
            result.setMessage("Done")

        } catch (e: SQLException) {
            input.outMsg("Database access error when adding organization")
            result.setMessage("Error, please try again")
        }
        preparedStatement.close()


        return result
    }
}