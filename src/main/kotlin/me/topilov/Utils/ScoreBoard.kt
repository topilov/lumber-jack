package me.topilov.Utils

import fr.mrmicky.fastboard.FastBoard
import me.clip.placeholderapi.PlaceholderAPI
import me.topilov.app
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class ScoreBoard : Listener {

    private var boards: MutableMap<UUID, FastBoard> = HashMap()
    private val manager = app.manager

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val player = e.player

        createBoard(player)

    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        val player = e.player
        val board: FastBoard = boards.remove(player.uniqueId)!!

        board.delete()
    }

    private fun createBoard(player: Player) {

        val board = FastBoard(player)
        board.updateTitle(manager?.getPlaceHolderConfig()?.getString("name"))
        boards[player.uniqueId] = board

        object : BukkitRunnable() {
            override fun run() {
                updateBoard(board)
            }
        }.runTaskTimer(app, 20, manager?.getPlaceHolderConfig()?.getLong("updateIntervalInTicks")!!)

    }

    private fun updateBoard(board: FastBoard) {
        board.updateLines(
            PlaceholderAPI.setPlaceholders(
                board.player, manager?.getPlaceHolderConfig()?.getStringList("lines")!!
            )
        )
    }
}