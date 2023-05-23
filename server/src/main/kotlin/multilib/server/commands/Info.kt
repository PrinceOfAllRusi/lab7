package allForCommands.commands

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import organization.MyCollection
import organization.Organization
import multilib.utilities.result.Result
import java.time.format.DateTimeFormatter

class Info : AbstractCommand(), KoinComponent {

    private val orgs: MyCollection<Organization> by inject()
    private val description: String = "display information about the collection"
    private var fields: Map<String, Map<String, String>> = mapOf()
    override fun action(data: Map<String, String?>, result: Result): Result {
        val s = StringBuilder()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        s.append( "Collection type " + orgs.javaClass.toString() + "\n" )
        s.append( "Initialization date " + orgs.getCreationDate()!!.format(formatter) + "\n" )
        s.append( "Amount of elements " + orgs.size + "\n")
        result.setMessage(s.toString())

        return result
    }
    override fun getDescription(): String = description
    override fun getFields() = fields
}