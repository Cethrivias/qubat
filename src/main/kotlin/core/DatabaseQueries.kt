package core

import config.Database
import config.Query
import db.Db
import enums.QueryStatus
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay

class DatabaseQueries(database: Database, private val queries: List<Query>) {
    private val connection = Db.getInstance(database)

    suspend fun execute() = queries.map {
            GlobalScope.async {
                it.status = QueryStatus.running
                it.injectArgs()
                delay(500)
                try {
                    connection.executeUpdate(it.statement)
                    it.status = QueryStatus.done
                } catch (e: Exception) {
                    it.status = QueryStatus.error
                }
            }
        }.awaitAll()
}
