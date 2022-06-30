package me.topilov.Utils

import me.topilov.app
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class MySQL {
    private var connection: Connection? = null

    fun isConnected(): Boolean {
        return connection != null
    }

    var manager: ConfigManager? = app.manager

    @Throws(ClassNotFoundException::class, SQLException::class)
    fun connect() {
        if (!isConnected()) {
            val password: String? = manager?.getDataBaseConfig()?.getString("database.password")
            val username: String? = manager?.getDataBaseConfig()?.getString("database.username")
            val database: String? = manager?.getDataBaseConfig()?.getString("database.databaseName")
            val port: String? = manager?.getDataBaseConfig()?.getString("database.port")
            val host: String? = manager?.getDataBaseConfig()?.getString("database.host")
            connection = DriverManager.getConnection(
                "jdbc:mysql://" +
                        host + ":" + port + "/" + database + "?allowPublicKeyRetrieval=true&useSSL=false",
                username, password
            )
        }
    }

    fun disconnect() {
        if (isConnected()) {
            try {
                connection!!.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    fun getConnection(): Connection? {
        return connection
    }
}