package me.topilov.Utils

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.topilov.app
import org.bukkit.entity.Player
import java.util.*


class PlaceHolder : PlaceholderExpansion() {

    private val data = app.data
    private val manager = app.manager

    override fun getAuthor(): String {
        return "topilov"
    }

    override fun getIdentifier(): String {
        return "async"
    }

    override fun getVersion(): String {
        return "1.0.0"
    }

    override fun onPlaceholderRequest(player: Player?, params: String): String? {

        if (player == null) return null

        if (params.equals("level", ignoreCase = true)) {

            return data?.getInt("level", "players", player.name)?.toString()
        }

        if (params.equals("rebirth", ignoreCase = true)) {

            val rebirth = data?.getInt("rebirth", "players", player.name)

            return if (rebirth!! > 0) {
                manager?.getRebirthConfig()?.getString("chatMapping.$rebirth")
            } else {
                manager?.getRebirthConfig()?.getString("placeholderZeroLevel")
            }
        }

        if (params.equals("rebirth_chat", ignoreCase = true)) {

            val rebirth = data?.getInt("rebirth", "players", player.name)

            return if (rebirth!! > 0) {
                manager?.getRebirthConfig()?.getString("chatMapping.$rebirth")
            } else {
                ""
            }
        }

        if (params.equals("player_exp", ignoreCase = true)) {

            return data?.getInt("player_exp", "players", player.name)?.toString()
        }

        if (params.equals("bits", ignoreCase = true)) {

            return data?.getInt("bits", "players", player.name)?.toString()
        }

        if (params.equals("broke_blocks", ignoreCase = true)) {

            return data?.getInt("broke_blocks", "players", player.name)?.toString()
        }

        if (params.equals("kills_mobs", ignoreCase = true)) {

            return data?.getInt("kills_mobs", "players", player.name)?.toString()
        }

        if (params.equals("kills_bosses", ignoreCase = true)) {

            return data?.getInt("kills_bosses", "players", player.name)?.toString()
        }

        if (params.equals("kills_players", ignoreCase = true)) {

            return data?.getInt("kills_players", "players", player.name)?.toString()
        }

        if (params.equals("stats_player_exp", ignoreCase = true)) {

            val value = app.getTotalBooster("stats_player_exp", player)
            return String.format(Locale.US, "%.1f", value)
        }

        if (params.equals("stats_money", ignoreCase = true)) {

            val value = app.getTotalBooster("stats_money", player)
            return String.format(Locale.US, "%.1f", value)
        }

        if (params.equals("stats_broke_blocks", ignoreCase = true)) {

            val value = app.getTotalBooster("stats_broke_blocks", player)
            return String.format(Locale.US, "%.1f", value)
        }

        if (params.equals("stats_kills_mobs", ignoreCase = true)) {

            val value = app.getTotalBooster("stats_kills_mobs", player)
            return String.format(Locale.US, "%.1f", value)
        }

        if (params.equals("world", ignoreCase = true)) {

            val world = player.world.name

            return try {
                manager?.getChatConfig()?.getString("worlds.$world")
            } catch (ex: java.lang.NullPointerException) {
                manager?.getChatConfig()?.getString("defaultWorld")
            }

        }


        return null
    }
}