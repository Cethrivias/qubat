package db

import config.Database
import java.sql.Connection
import java.sql.DriverManager

class Db internal constructor(database: Database) {
    private var connection: Connection

    init {
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance()
        connection = DriverManager.getConnection(
            "jdbc:mysql://${database.host}:${database.port}/${database.database}",
            database.username,
            database.password
        )
    }

    fun executeUpdate(query: String): Int {
        return connection.createStatement().executeUpdate(query);
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


