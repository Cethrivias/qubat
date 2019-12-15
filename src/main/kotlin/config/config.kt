package config

import com.sksamuel.hoplite.ConfigLoader
import logger.LoggerContainer
import java.nio.file.Path
import java.util.*
import java.util.logging.Logger

data class Query(val log: String?, val statement: String)

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
        private val log = LoggerContainer.logger
        @Volatile
        private var instance: Config? = null

        fun init(addons: Array<String>) {
            val i1 = instance
            if (i1 != null) {
                return
            }

            return synchronized(this) {
                if (addons.isNotEmpty()) {
                    log.info("Loading configs: ${addons.joinToString()}")
                }
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
                .map { Path.of(dir, "/$it.yaml") }
                .asReversed()

            return ConfigLoader().loadConfigOrThrow(paths)
        }
    }
}
