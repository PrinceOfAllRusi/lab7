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
        var condition: Boolean = false

        for ( org in orgs ) {
            if ( org.getId() == id ) {
                orgs.remove( org )
                condition = true
                break
            }
        }
        if (condition) result.setMessage("Done")
        else result.setMessage("This organization is not in the collection")

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