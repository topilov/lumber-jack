package me.topilov.Utils

import me.clip.placeholderapi.PlaceholderAPI
import me.topilov.App
import me.topilov.app
import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scheduler.BukkitRunnable

class BossBar : Listener {

    val manager = app.manager

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        object : BukkitRunnable() {
            override fun run() {
                create(e.player)
            }
        }.runTaskLater(app, 20)
    }


    private fun create(player: Player) {

        val bar = Bukkit.getServer().createBossBar("", BarColor.YELLOW, BarStyle.SOLID)
        val title = PlaceholderAPI.setPlaceholders(player, manager?.getShowBooster()?.getString("title")!!)

        bar.setTitle(title)
        bar.addPlayer(player)

        reload(bar, player)
    }

    private fun setTitle(player: Player, bar: BossBar) {
        val title = PlaceholderAPI.setPlaceholders(player, manager?.getShowBooster()?.getString("title")!!)
        bar.setTitle(title)
    }

    private fun reload(bar: BossBar, player: Player) {
        object : BukkitRunnable() {
            override fun run() {

                val playerName = player.name

                if (Bukkit.getPlayer(playerName) != null) {
                    setTitle(player, bar)
                } else {
                    cancel()
                }

            }
        }.runTaskTimer(app, 5 * 20, 5 * 20)
    }

}