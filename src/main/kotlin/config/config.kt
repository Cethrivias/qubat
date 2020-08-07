package config

import com.sksamuel.hoplite.ConfigLoader
import enums.QueryStatus
import java.nio.file.Paths

data class Query(val log: String, var statement: String, @Volatile var status: QueryStatus = QueryStatus.pending) {
    fun injectArgs() {
        val config = ConfigContainer.getConfig()

        "\\\$\\{(\\w+)}".toRegex()
            .findAll(statement)
            .map { it.groupValues[0] to it.groupValues[1] }
            .toMap()
            .forEach {
                val value = config.args[it.value] ?: throw Exception("Unknown argument '${it.value}'")
                statement = statement.replace(it.key, value)
            }
    }
}


data class Database(
    val username: String,
    val password: String,
    val host: String,
    val port: String,
    val database: String
)

data class Config(
    val args: Map<String, String>,
    val databases: Map<String, Database>,
    val queries: Map<String, List<Query>>
)

class ConfigContainer {
    companion object {
        @Volatile
        private var instance: Config? = null

        fun init(addons: Array<String>) {
            val i1 = instance
            if (i1 != null) {
                return
            }

            return synchronized(this) {
                if (instance == null) {
                    val created = load(addons)
                    instance = created
                }
            }
        }

        fun getConfig(): Config = instance!!

        private fun load(addons: Array<String>): Config {
            val dir = System.getProperty("user.dir")
            val paths = (listOf("qubat") + addons.asList())
                .map { Paths.get(dir, if (it.endsWith(".yaml")) it else "$it.yaml") }
                .asReversed()

            return ConfigLoader().loadConfigOrThrow(paths)
        }
    }
}
