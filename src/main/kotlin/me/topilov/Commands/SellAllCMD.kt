package me.topilov.Commands

import de.tr7zw.nbtapi.NBTItem
import me.topilov.app
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SellAllCMD: CommandExecutor {

    val data = app.data
    private val economy = app.economy
    private val manager = app.manager

    override fun onCommand(player: CommandSender, cmd: Command, label: String, args: Array<out String>?): Boolean {

        if (!cmd.name.equals("sellall", ignoreCase = true)) return false
        if (player !is Player) return false
        if (args?.isEmpty()!! && data?.getInt("sellall", "players", player.name) != 1) {
            player.sendMessage(manager?.getSellAll()?.getString("needBuy")!!)
            return false
        }

        if (args.isEmpty() && data?.getInt("sellall", "players", player.name) == 1) {

            sellItems(player, "nbt", "emptyInventory", "message")

            return true
        } else if (args[0] == manager?.getSellAll()?.getString("longUsage")) {

            sellItems(player, "nbt", "emptyInventory", "message")

            return true
        } else if (args[0] == manager?.getSellAll()?.getString("traders.longUsage")) {

            sellItems(player, "traders.nbt", "traders.emptyInventory", "traders.message")

            return true
        }

        return true
    }

    private fun sellItems(player: Player, pathToNBT: String, pathToEmptyInventory: String, pathToMessage: String) {

        var sells = 0.0
        var items = 0

        for (i in 0..player.inventory.size) {
            val itemStack = player.inventory.getItem(i)

            if (itemStack != null) {
                val nbtItemStack = NBTItem(itemStack)

                if (nbtItemStack.hasKey(manager?.getSellAll()?.getString(pathToNBT))) {
                    sells += nbtItemStack.getString(manager?.getSellAll()?.getString(pathToNBT)).toDouble() * itemStack.amount
                    items += itemStack.amount
                    player.inventory.setItem(i, null)
                    economy?.depositPlayer(player, nbtItemStack.getString(manager?.getSellAll()?.getString(pathToNBT)).toDouble() * itemStack.amount)
                }
            }
        }

        if (items == 0) {
            player.sendMessage(manager?.getSellAll()?.getString(pathToEmptyInventory)!!)
            return
        }

        player.sendMessage(manager?.getSellAll()?.getString(pathToMessage)!!
            .replace("%items%", items.toString())
            .replace("%sum%", sells.toString()))
    }
}