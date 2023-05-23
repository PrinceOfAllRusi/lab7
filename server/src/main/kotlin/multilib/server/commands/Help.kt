package allForCommands.commands

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tools.CommandsList
import multilib.utilities.result.Result


class Help : AbstractCommand(), KoinComponent {

    private val description: String = "display help on available commands"
    private val commandsList: CommandsList by inject()
    private var fields: Map<String, Map<String, String>> = mapOf()
    override fun action(data: Map<String, String?>, result: Result): Result {
        result.setMessage(commandsList.getDescription() + "\n")

        return result
    }
    override fun getDescription(): String = description
    override fun getFields() = fields
}