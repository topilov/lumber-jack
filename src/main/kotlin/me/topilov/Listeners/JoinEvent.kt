package me.topilov.Listeners

import me.topilov.Utils.PetsAPI
import me.topilov.Utils.SQLGetter
import me.topilov.app
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.util.*


class JoinEvent : Listener {

    private val data: SQLGetter? = app.data
    val petsAPI = PetsAPI()

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val player = e.player

        data?.createPlayer(player)
        data?.createPets(player)
        putLocalBoosters(player)

    }

    fun putLocalBoosters(player: Player) {
        if (!app.localExpBooster.containsKey(player)) {
            app.localExpBooster[player] = 0.0
        }

        if (!app.localMoneyBooster.containsKey(player)) {
            app.localMoneyBooster[player] = 0.0
        }

        if (!app.localBlocksBooster.containsKey(player)) {
            app.localBlocksBooster[player] = 0.0
        }

        if (!app.localMobsBooster.containsKey(player)) {
            app.localMobsBooster[player] = 0.0
        }
    }

}