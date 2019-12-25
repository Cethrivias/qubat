package db

import com.zaxxer.hikari.HikariDataSource
import config.Database

class Db internal constructor(database: Database) {
    private var connectionPool: HikariDataSource

    init {
        val dbUrl = "jdbc:mysql://${database.host}:${database.port}/${database.database}"
        connectionPool = HikariDataSource()

        connectionPool.username = database.username
        connectionPool.password = database.password
        connectionPool.driverClassName = "com.mysql.cj.jdbc.Driver"
        connectionPool.jdbcUrl = dbUrl
        connectionPool.minimumIdle = 10
        connectionPool.maximumPoolSize = 10
    }

    fun executeUpdate(query: String): Int = connectionPool.connection.use {
        it.createStatement().executeUpdate(query)
    }

    companion object {
        @Volatile
        private var instances = HashMap<String, Db>()

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


