package me.topilov.Listeners

import me.topilov.Utils.PetsAPI
import me.topilov.app
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent

class PetListener: Listener {

    val manager = app.manager
    val data = app.data
    private val petsAPI = PetsAPI()

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        app.activePets[e.player] = data!!.getString("active", "pets", e.player.name)
        app.petMoneyBooster[e.player] = 0.0
        app.petExpBooster[e.player] = 0.0
        app.petBlocksBooster[e.player] = 0.0
        app.petMobsBooster[e.player] = 0.0

        petsAPI.spawnPets(e.player)
        petsAPI.reloadPetBoosters(e.player)
    }

    @EventHandler
    fun onMove(e: PlayerMoveEvent) {

        petsAPI.movePets(e.player)
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        data!!.setString("active", "pets", e.player.name, app.activePets[e.player])
        app.activePets.remove(e.player)

        petsAPI.removePets(e.player)
    }
}