package allForCommands.commands

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import organization.MyCollection
import organization.Organization
import multilib.utilities.input.*
import multilib.utilities.result.Result


class RemoveById: AbstractCommand(), KoinComponent {

    private val orgs: MyCollection<Organization> by inject()
    private val description: String = "remove element from collection by its id"
    private var fields: Map<String, Map<String, String>> = mapOf(
        "value" to mapOf<String, String>(
            "type" to "Int",
            "min" to "0"
        )
    )

    override fun action(data: Map<String, String?>, result: Result): Result {

        val id: Int = data["value"]!!.toInt()
        var message = "This organization is not in the collection\n"
        val userLogin = data["userLogin"]

        for ( org in orgs ) {
            if ( org.getId() == id ) {
                if (org.getUserLogin() == userLogin) {
                    orgs.remove( org )
                    message = "Done\n"
                } else message = "You do not have access to this organization\n"
                break
            }
        }
        result.setMessage(message)
        return result
    }
    override fun getDescription(): String = description
    override fun getFields() = fields
    override fun commandBuilding(mapData: MutableMap<String, String>, data: String): MutableMap<String, String> {
        val input = InputFile(data)

        for (key in fields.keys) {
            mapData.put(key, input.getNextWord(null))
        }

        return mapData
    }
}