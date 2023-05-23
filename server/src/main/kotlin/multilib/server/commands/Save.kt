package allForCommands.commands

import multilib.server.dataBase.DataBaseWorker
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import organization.MyCollection
import organization.Organization
import multilib.utilities.result.Result

class Save: AbstractCommand(), KoinComponent {

    private val orgs: MyCollection<Organization> by inject()
    private val dataBaseWorker: DataBaseWorker by inject()
    private val description: String = "save collection to file"

    override fun action(data: Map<String, String?>, result: Result): Result {
        dataBaseWorker.deleteAllInfoFromOrgTable()
        for (org in orgs) {
            dataBaseWorker.addOrg(org, result)
        }
        return result
    }
    override fun getDescription(): String = description
}