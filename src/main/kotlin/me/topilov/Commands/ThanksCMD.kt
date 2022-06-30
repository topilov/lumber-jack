package me.topilov.Commands

import me.topilov.Utils.SendCommandsAPI
import me.topilov.app
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ThanksCMD : CommandExecutor {

    private val sendCommandsAPI = SendCommandsAPI()
    val manager = app.manager

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>?): Boolean {
        if (!cmd.name.equals("thanks", ignoreCase = true)) return false

        val player = sender as Player

        if (app.activeBoosters.isEmpty()) {
            sendCommandsAPI.sendFromConfig(player.name, manager?.getThanks()?.getStringList("none")!!)
            return false
        }

        var thanks = 0

        app.thanks.keys.forEach { key ->

            var playerThanks = app.thanks[key]
            val listPlayerThanks = playerThanks?.split(",")
            val booster = app.activeBoosters[key]?.split(":")

            if (!listPlayerThanks?.contains(player.name)!! && booster!![2] != player.name) {
                sendCommandsAPI.sendFromConfig(booster[2], manager?.getThanks()?.getStringList("mapping." + booster[0] + ".thanks")!!)
                playerThanks = "$playerThanks,${player.name}"

                app.thanks[key] = playerThanks

                thanks += 1

            }

        }

        if (thanks == 0) {
            sendCommandsAPI.sendFromConfig(player.name, manager?.getThanks()?.getStringList("already")!!)
            return false
        }

        sendCommandsAPI.sendFromConfig(player.name, manager?.getThanks()?.getStringList("mapping.stats_player_exp.executor")!!)

        return true
    }
}