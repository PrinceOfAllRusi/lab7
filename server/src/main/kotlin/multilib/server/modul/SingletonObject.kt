package multilib.server.modul

import allForCommands.commands.*
import multilib.server.commands.LogIn
import multilib.server.commands.LogOut
import multilib.server.commands.Registration
import multilib.server.dataBase.DataBaseWorker
import org.koin.dsl.module
import organization.MyCollection
import organization.Organization
import tools.CommandsList
import tools.CreateOrganization
import kotlin.collections.ArrayList

object SingletonObject {

    val mod = module {
        single<MyCollection<Organization>> { MyCollection() }
        single<ArrayList<String>> { ArrayList() }
        single<CreateOrganization> { CreateOrganization() }
        single<DataBaseWorker> { DataBaseWorker() }

        val listCommand: Map<String, AbstractCommand> = mapOf("help" to Help(),
            "info" to Info(), "show" to Show(), "add" to Add(),
            "update" to Update(), "remove_by_id" to RemoveById(),
            "clear" to Clear(), "execute_script" to ExecuteScript(),
            "exit" to Exit(), "insert_at" to InsertAt(),
            "remove_at" to RemoveAt(), "remove_lower" to RemoveLower(),
            "remove_all_by_employees_count" to RemoveAllByEmployeesCount(),
            "count_greater_than_annual_turnover" to CountGreaterThanAnnualTurnover(),
            "filter_starts_with_name" to FilterStartsWithName(),
            "register" to Registration(), "log_in" to LogIn(), "log_out" to LogOut())
        val commandsVersion = 1

        val CommandsList: CommandsList = CommandsList( listCommand, commandsVersion )
        single<CommandsList> { CommandsList }
    }
}