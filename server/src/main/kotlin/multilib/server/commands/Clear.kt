package allForCommands.commands

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import organization.MyCollection
import organization.Organization
import tools.CreateOrganization
import multilib.utilities.result.Result

class Clear: AbstractCommand(), KoinComponent {

    private val orgs: MyCollection<Organization> by inject()
    private val creator: CreateOrganization by inject()
    private val description: String = "clear the collection"
    private var fields: Map<String, Map<String, String>> = mapOf()

    override fun action(data: Map<String, String?>, result: Result): Result {
        orgs.clear()
        creator.setCounter(0)
        result.setMessage("Done")

        return result
    }
    override fun getDescription(): String = description
    override fun getFields() = fields
}