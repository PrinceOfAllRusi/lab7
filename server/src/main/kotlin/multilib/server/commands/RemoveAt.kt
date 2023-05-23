package allForCommands.commands

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import organization.MyCollection
import organization.Organization
import multilib.utilities.input.*
import multilib.utilities.result.Result

class RemoveAt: AbstractCommand(), KoinComponent {

    private val orgs: MyCollection<Organization> by inject()
    private val description: String = "remove the element at the given position in the collection"
    private var fields: Map<String, Map<String, String>> = mapOf(
        "value" to mapOf<String, String>(
            "type" to "Int",
            "min" to "-1"
        )
    )

    override fun action(data: Map<String, String?>, result: Result): Result {
        val index = data["value"]!!.toInt() + 1
        try {
            val org = orgs[index]
            if (org.getUserLogin() == result.getToken().getLogin()) {
                orgs.removeAt(index)
                result.setMessage("Done\n")
            } else {
                result.setMessage("You do not have access to this organization\n")
            }
        } catch (e: IndexOutOfBoundsException) {
            result.setMessage("There is no organization in this position\n")
        }

        return result
    }
    override fun getDescription(): String = description
    override fun getFields() = fields
    override fun commandBuilding(mapData: MutableMap<String, String>, data: String): MutableMap<String, String> {
        val input = InputFile(data)

        for (key in fields.keys) {
            mapData[key] = input.getNextWord(null)
        }

        return mapData
    }
}
