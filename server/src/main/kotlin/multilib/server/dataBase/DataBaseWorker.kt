package multilib.server.dataBase

import multilib.server.tools.User
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
    private lateinit var user: User

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
            input.outMsg("Database access error\n")
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
            input.outMsg("Database access error\n")
        }
        preparedStatement.close()
    }
    fun getUser(login: String, password: String): User {
        sql = "SELECT * FROM users WHERE login = ? AND password = ?"
        var id = 0
        var userLogin = ""
        var userPassword = ""
        user = User()
        try {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setString(1, login)
            preparedStatement.setString(2, password)
            resultSet = preparedStatement.executeQuery()

            while (resultSet.next()) {
                id = resultSet.getInt(1)
                userLogin = resultSet.getString(2)
                userPassword = resultSet.getString(3)
                user.setId(id)
                user.setLogin(userLogin)
                user.setPassword(userPassword)
            }
        } catch (e: SQLException) {
            input.outMsg("Database access error when searching user\n")
        }
        preparedStatement.close()
        return user
    }
    fun fillOrgsList() {
        sql = "SELECT * FROM organization;"
        val orgData = mutableMapOf<String, String>()
        var id = 0
        var creationDate = LocalDateTime.now()
        var ts: Timestamp
        var organization = Organization()
        var login = ""
        val organizationWithId = Organization()
        try {
            preparedStatement = connection.prepareStatement(sql)
            resultSet = preparedStatement.executeQuery()

            while (resultSet.next()) {
                id = resultSet.getInt(1)
                orgData["name"] = resultSet.getString(2)
                orgData["x"] = resultSet.getString(3)
                orgData["y"] = resultSet.getString(4)
                ts = resultSet.getTimestamp(5)
                creationDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(ts.getTime()), ZoneOffset.UTC)
                orgData["annualTurnover"] = resultSet.getString(6)
                orgData["employeesCount"] = resultSet.getString(7)
                orgData["type"] = resultSet.getString(8)
                orgData["street"] = resultSet.getString(9)
                orgData["zipCode"] = resultSet.getString(10)
                orgData["userLogin"] = resultSet.getString(11)
                organizationWithId.setId(id)
                organization = creator.create(orgData, organizationWithId)
                organization.setCreationDate(creationDate)
                orgs.add(organization)
            }
        } catch (e: SQLException) {
            input.outMsg("Database access error\n")
        }
        preparedStatement.close()
        sql = ""
    }
    fun addOrg(organization: Organization, result: Result): Result {
        sql = "INSERT INTO organization VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
        val login = result.getToken().getLogin()
        try {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setInt(1, organization.getId()!!)
            preparedStatement.setString(2, organization.getName())
            preparedStatement.setInt(3, organization.getCoordinatesX())
            preparedStatement.setLong(4, organization.getCoordinatesY())
            val timestamp: Timestamp = Timestamp.valueOf(organization.getCreationDate())
            preparedStatement.setTimestamp(5, timestamp)
            preparedStatement.setDouble(6, organization.getAnnualTurnover()!!)
            preparedStatement.setInt(7, organization.getEmployeesCount()!!)
            preparedStatement.setString(8, organization.getType().toString())
            preparedStatement.setString(9, organization.getPostalAddressStreet())
            preparedStatement.setString(10, organization.getPostalAddressZipCode())
            preparedStatement.setString(11, login)

            preparedStatement.executeUpdate()

            result.setMessage("Done\n")

        } catch (e: SQLException) {
            input.outMsg("Database access error when adding organization\n")
            result.setMessage("Error, please try again\n")
        }
        preparedStatement.close()

        return result
    }
    fun deleteAllInfoFromOrgTable() {
        sql = "TRUNCATE organization"
        try {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.executeUpdate()
        } catch (e: SQLException) {
            input.outMsg("Database access error when deleting info from organization\n")
        }
        preparedStatement.close()
    }
    fun getNextOrgId(): Int {
        sql = "SELECT nextval('organization_id_seq');"
        var id = 0
        try {
            preparedStatement = connection.prepareStatement(sql)
            resultSet = preparedStatement.executeQuery()
            resultSet.next()
            id = resultSet.getInt(1)
        } catch (e: SQLException) {
            input.outMsg("Database access error when searching next organization_id\n")
        }
        return id
    }
    fun resetOrgId() {
        sql = "ALTER SEQUENCE organization_id_seq RESTART WITH 1;"
        try {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.executeUpdate()
        } catch (e: SQLException) {
            input.outMsg("Database access error reset organization_id\n")
        }
    }
}