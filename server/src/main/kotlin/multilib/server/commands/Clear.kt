package allForCommands.commands

import multilib.server.dataBase.DataBaseWorker
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import organization.MyCollection
import organization.Organization
import multilib.utilities.result.Result

class Clear: AbstractCommand(), KoinComponent {

    private val orgs: MyCollection<Organization> by inject()
    private val description: String = "clear the collection"
    private var fields: Map<String, Map<String, String>> = mapOf()

    override fun action(data: Map<String, String?>, result: Result): Result {
        val newOrgs = MyCollection<Organization>()
        val userLogin = data["userLogin"]
        for (org in orgs) {
            if ( org.getUserLogin() == userLogin ) {
                newOrgs.add(org)
            }
        }
        for (org in newOrgs) {
            orgs.remove(org)
        }
        result.setMessage("Done\n")

        return result
    }
    override fun getDescription(): String = description
    override fun getFields() = fields
}