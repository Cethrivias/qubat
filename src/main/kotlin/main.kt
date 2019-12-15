import db.Db
import config.ConfigContainer
import config.Query
import kotlinx.coroutines.*
import logger.LoggerContainer
import java.lang.Exception
import java.lang.Thread.sleep
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    ConfigContainer.init(args)
    val config = ConfigContainer.getConfig()
    val log = LoggerContainer.logger


    log.info("Args: ${config.args.entries.joinToString { "${it.key} = ${it.value}" }}")

    sleep(5000)

    runBlocking {
        val time = measureTimeMillis {
            config.queries.map { dbQueries ->
                GlobalScope.async {
                    val dbName = dbQueries.key.padEnd(10)
                    log.info("[$dbName]: Connecting")
                    val db =
                        config.databases.getOrElse(dbQueries.key, { throw Error("Config is not specified for a db") })
                    val connection = Db.getInstance(db)

                    log.info("[$dbName]: Injecting args")
                    val queries = dbQueries.value
                        .filter { it.statement.isNotEmpty() }
                        .map { GlobalScope.async { Query(it.log, injectArgs(it.statement)) } }
                        .awaitAll()

                    log.info("[$dbName]: Executing queries")

                    queries.map {
                        GlobalScope.async {
                            log.info("[$dbName]: [Started]: ${it.log ?: it.statement}")
                            connection.executeUpdate(it.statement)
                            log.info("[$dbName]: [Done   ]: ${it.log ?: it.statement}")
                        }
                    }.awaitAll()

                }
            }.awaitAll()
        }

        log.info("Execution time $time ms")
    }
}

fun injectArgs(str: String): String {
    val config = ConfigContainer.getConfig()
    var strTmp = str

    "\\\$\\{(\\w+)}".toRegex()
        .findAll(str)
        .map { it.groupValues[0] to it.groupValues[1] }
        .toMap()
        .forEach {
            val value = config.args[it.value] ?: throw Exception("Unknown argument '${it.value}'")
            strTmp = strTmp.replace(it.key, value)
        }

    return strTmp
}

