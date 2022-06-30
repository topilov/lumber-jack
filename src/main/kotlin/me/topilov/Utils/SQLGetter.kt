package me.topilov.Utils

import me.topilov.App
import me.topilov.app
import org.bukkit.entity.Player
import java.sql.PreparedStatement
import java.sql.SQLException

class SQLGetter {

    fun createTablePlayers() {
        val ps: PreparedStatement
        try {
            ps = app.sql?.getConnection()?.prepareStatement(
                "CREATE TABLE IF NOT EXISTS players "
                        + "(name VARCHAR(100),rebirth INT(100) NOT NULL DEFAULT '0',level INT(100) NOT NULL DEFAULT '1',bits INT(100) NOT NULL DEFAULT '0'," +
                        "player_exp DOUBLE NOT NULL DEFAULT '0.0',broke_blocks DOUBLE NOT NULL DEFAULT '0.0',kills_mobs DOUBLE NOT NULL DEFAULT '0.0'," +
                        "kills_bosses DOUBLE NOT NULL DEFAULT '0.0',kills_players DOUBLE NOT NULL DEFAULT '0.0'," +
                        "stats_player_exp DOUBLE NOT NULL DEFAULT '1.0',stats_money DOUBLE NOT NULL DEFAULT '1.0'," +
                        "stats_broke_blocks DOUBLE NOT NULL DEFAULT '1.0'," +
                        "stats_kills_mobs DOUBLE NOT NULL DEFAULT '1.0'," +
                        "sub INT(100) NOT NULL DEFAULT '0'," +
                        "sellall INT(100) NOT NULL DEFAULT '0',PRIMARY KEY (NAME))"
            )!!
            ps.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun createPlayer(player: Player) {
        try {
            val name = player.name
            if (!exists(name)) {
                val ps2: PreparedStatement = app.sql?.getConnection()?.prepareStatement(
                    ("INSERT IGNORE INTO players"
                            + " (name) VALUES (?)")
                )!!
                ps2.setString(1, player.name)
                ps2.executeUpdate()
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    private fun exists(name: String): Boolean {
        try {
            val ps: PreparedStatement =
                app.sql?.getConnection()?.prepareStatement("SELECT * FROM players WHERE name=?")!!
            ps.setString(1, name)
            val results = ps.executeQuery()

            return results.next()

        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return false
    }

    fun createTablePets() {
        val ps: PreparedStatement
        try {
            ps = app.sql?.getConnection()?.prepareStatement(
                "CREATE TABLE IF NOT EXISTS pets "
                        + "(name VARCHAR(100), pets VARCHAR(100) NOT NULL DEFAULT '1:0,2:0,3:0,4:0,5:0,6:0,7:0,8:0,9:0,10:0,11:0,12:0,13:0,14:0,15:0,16:0,17:0,18:0,19:0,20:0,21:0'," +
                        "active VARCHAR(100) NOT NULL DEFAULT ''," +
                        "maxSlots INT(100) NOT NULL DEFAULT '2',PRIMARY KEY (NAME))"
            )!!
            ps.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun createPets(player: Player) {
        try {
            val name = player.name
            if (!existsPets(name)) {
                val ps2: PreparedStatement = app.sql?.getConnection()?.prepareStatement(
                    ("INSERT IGNORE INTO pets"
                            + " (name) VALUES (?)")
                )!!
                ps2.setString(1, player.name)
                ps2.executeUpdate()
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    private fun existsPets(name: String): Boolean {
        try {
            val ps: PreparedStatement =
                app.sql?.getConnection()?.prepareStatement("SELECT * FROM pets WHERE name=?")!!
            ps.setString(1, name)
            val results = ps.executeQuery()

            return results.next()

        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return false
    }

    fun getDouble(`object`: String, table: String, name: String): Double {
        try {
            val ps: PreparedStatement =
                app.sql?.getConnection()!!.prepareStatement("SELECT $`object` FROM $table WHERE name=?")
            ps.setString(1, name)
            val rs = ps.executeQuery()
            val value: Double
            if (rs.next()) {
                value = rs.getDouble(`object`)
                return value
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return 0.0
    }

    fun setDouble(`object`: String, table: String, name: String, value: Double) {
        try {
            val ps: PreparedStatement =
                app.sql?.getConnection()!!.prepareStatement("UPDATE $table SET $`object`=? WHERE name=?")
            ps.setDouble(1, value)
            ps.setString(2, name)
            ps.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun addDouble(`object`: String, table: String, name: String, value: Double) {
        try {
            val ps: PreparedStatement =
                app.sql?.getConnection()!!.prepareStatement("UPDATE $table SET $`object`=? WHERE name=?")
            ps.setDouble(1, getDouble(`object`, table, name) + value)
            ps.setString(2, name)
            ps.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun removeDouble(`object`: String, table: String, name: String, value: Double) {
        try {
            val ps: PreparedStatement =
                app.sql?.getConnection()!!.prepareStatement("UPDATE $table SET $`object`=? WHERE name=?")
            ps.setDouble(1, getDouble(`object`, table, name) - value)
            ps.setString(2, name)
            ps.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun getInt(`object`: String, table: String, name: String): Int {
        try {
            val ps: PreparedStatement =
                app.sql?.getConnection()!!.prepareStatement("SELECT $`object` FROM $table WHERE name=?")
            ps.setString(1, name)
            val rs = ps.executeQuery()
            val value: Int
            if (rs.next()) {
                value = rs.getInt(`object`)
                return value
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return 0
    }

    fun setInt(`object`: String, table: String, name: String, value: Int) {
        try {
            val ps: PreparedStatement =
                app.sql?.getConnection()!!.prepareStatement("UPDATE $table SET $`object`=? WHERE name=?")
            ps.setInt(1, value)
            ps.setString(2, name)
            ps.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun addInt(`object`: String, table: String, name: String, value: Int) {
        try {
            val ps: PreparedStatement =
                app.sql?.getConnection()!!.prepareStatement("UPDATE $table SET $`object`=? WHERE name=?")
            ps.setInt(1, getInt(`object`, table, name) + value)
            ps.setString(2, name)
            ps.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun removeInt(`object`: String, table: String, name: String, value: Int) {
        try {
            val ps: PreparedStatement =
                app.sql?.getConnection()!!.prepareStatement("UPDATE $table SET $`object`=? WHERE name=?")
            ps.setInt(1, getInt(`object`, table, name) - value)
            ps.setString(2, name)
            ps.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun getString(`object`: String, table: String, name: String?): String {
        try {
            val ps: PreparedStatement =
                app.sql?.getConnection()!!.prepareStatement("SELECT $`object` FROM $table WHERE name=?")
            ps.setString(1, name)
            val rs = ps.executeQuery()
            var string: String? = null
            if (rs.next()) {
                string = rs.getString(`object`)
                return string
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return ""
    }

    fun addString(`object`: String, table: String, name: String?, string: String?) {
        try {
            val ps: PreparedStatement =
                app.sql?.getConnection()!!.prepareStatement("UPDATE $table SET $`object`=? WHERE name=?")
            ps.setString(1, getString(`object`, table, name) + string)
            ps.setString(2, name)
            ps.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun setString(`object`: String, table: String, name: String?, string: String?) {
        try {
            val ps: PreparedStatement =
                app.sql?.getConnection()!!.prepareStatement("UPDATE $table SET $`object`=? WHERE name=?")
            ps.setString(1, string)
            ps.setString(2, name)
            ps.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}