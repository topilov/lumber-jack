package me.topilov.Commands

import me.topilov.Utils.ConfigManager
import me.topilov.Utils.PetsAPI
import me.topilov.Utils.SendCommandsAPI
import me.topilov.app
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask


class TestCMD : CommandExecutor{

    private var bukkitTask: BukkitTask? = null;
    private var configManager: ConfigManager? = app.manager
    val petsAPI = PetsAPI()
    private val sendCommandsAPI = SendCommandsAPI()

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (cmd.name.equals("test", ignoreCase = true)) {

            val player = sender as Player

            if (args[0].equals("start", ignoreCase = true)) {
                bukkitTask = object : BukkitRunnable() {
                    override fun run() {
                        sender.sendMessage("Scheduler")
                    }
                }.runTaskTimer(app, 20, 20)
            }

            if (args[0].equals("stop", ignoreCase = true)) {
                bukkitTask!!.cancel();
            }

            if (args[0].equals("test", ignoreCase = true)) {
                petsAPI.reloadPetBoosters(player)
            }

            if (args[0].equals("test2", ignoreCase = true)) {
                player.sendMessage(app.getPetBoosterHashMap("stats_kills_mobs")?.get(player)!!.toString())
            }

            if (args[0].equals("broadlocal", ignoreCase = true)) {
                app.activeBoosters.keys.forEach{key ->

                    val booster = app.activeBoosters[key]?.split(":")

                    sendCommandsAPI.sendTimesBoosters(configManager?.getThanks()?.getStringList("mapping." + booster!![0] + ".execute")!!,
                        booster!![1].toInt(),
                        booster[2])
                }
            }
        }
        return true;
    }
}