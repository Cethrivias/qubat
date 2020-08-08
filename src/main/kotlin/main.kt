import config.ConfigContainer
import core.DatabaseQueries
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import ui.Picture
import ui.PictureRefresher
import java.lang.Thread.sleep
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {

    ConfigContainer.init(args)
    val config = ConfigContainer.getConfig()
    val picture = Picture()
    val refresher = PictureRefresher(picture)

    refresher.run()

    while (Countdown.seconds > 0) {
        sleep(1000)
        Countdown.seconds--
    }

    runBlocking {
        val time = measureTimeMillis {
            config.queries.map { dbQueries ->
                GlobalScope.async {
                    val db =
                        config.databases.getOrElse(dbQueries.key, { throw Error("Config is not specified for a db") })
                    DatabaseQueries(db, dbQueries.value).execute()
                }
            }.awaitAll()
        }

        refresher.stop()
        picture.paint()

        println("Execution time $time ms")
    }

    println("Press Enter to exit")
    readLine()
}
