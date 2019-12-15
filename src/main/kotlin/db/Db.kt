package db

import config.Database
import org.apache.commons.dbcp2.BasicDataSource

class Db internal constructor(database: Database) {
    private var connectionPool: BasicDataSource

    init {
        val dbUrl = "jdbc:mysql://${database.host}:${database.port}/${database.database}"
        connectionPool = BasicDataSource()

        connectionPool.username = database.username
        connectionPool.password = database.password
        connectionPool.driverClassName = "com.mysql.cj.jdbc.Driver"
        connectionPool.url = dbUrl
        connectionPool.initialSize = 5
        connectionPool.maxTotal = 20
    }

    fun executeUpdate(query: String): Int {
        return connectionPool.connection.createStatement().executeUpdate(query);
    }

    companion object {
        @Volatile private var instances = HashMap<String, Db>()

        fun getInstance(database: Database): Db {
            val hash = "${database.host}${database.port}${database.database}${database.username}"
            val instance = instances[hash]

            if (instance != null) {
                return instance
            }

            return synchronized(this) {
                val instanceSync = instances[hash]
                if (instanceSync != null) {
                    instanceSync
                } else {
                    val created = Db(database)
                    instances[hash] = created
                    created
                }
            }
        }
    }
}


