package me.topilov.Utils

import org.bukkit.Bukkit
import org.bukkit.entity.Player

class SendCommandsAPI {

    fun sendFromConfig(player: String, list: List<String>) {

        for (cmd in list) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player))
        }

    }

    fun sendTimesBoosters(list: List<String>, time: Int, player: String) {

        for (cmd in list) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd
                .replace("%player%", player)
                .replace("%time%", (time / 60).toString()))
        }

    }

    fun sendReward(list: List<String>, amount: Int, player: String) {

        for (cmd in list) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd
                .replace("%player%", player)
                .replace("%money%", amount.toString()))
        }
    }

    fun sendActionWithPet(player: String, list: List<String>, level: Int, name: String) {

        for (cmd in list) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd
                .replace("%level%", level.toString())
                .replace("%name%", name)
                .replace("%player%", player))
        }

    }
}