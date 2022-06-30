package me.topilov.Commands

import me.topilov.Utils.ConfigItemStackAPI
import me.topilov.Utils.SendCommandsAPI
import me.topilov.app
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory

class RebirthCMD : CommandExecutor, Listener {

    private val configItemStackAPI = ConfigItemStackAPI()
    private val sendCommandsAPI = SendCommandsAPI()
    private val data = app.data
    private val economy = app.economy
    private val manager = app.manager

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>?): Boolean {
        if (cmd.name == "rebirth") {
            if (sender is ConsoleCommandSender) return true

            val player = sender as Player

            val inventory = Bukkit.getServer().createInventory(player, 9, "Перерождение")

            try {
                addItemsToInventory(player, inventory)
            } catch (ex: java.lang.NullPointerException) {
                sendCommandsAPI.sendFromConfig(player.name, manager?.getRebirthConfig()!!.getStringList("maxRebirth"))
                return true
            }

            player.openInventory(inventory)
        }

        return true
    }

    private fun addItemsToInventory(player: Player, inventory: Inventory) {

        val rebirth = data?.getInt("rebirth", "players", player.name)

        val itemStack = configItemStackAPI.getItemStack("rebirths.$rebirth", "rebirths.$rebirth",
            manager?.getRebirthConfig()!!, player)

        inventory.setItem(4, itemStack)
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        if (e.view.title != "Перерождение") return

        e.isCancelled = true
        rebirthUp(e.whoClicked as Player)
    }

    private fun rebirthUp(player: Player) {

        val rebirth = data?.getInt("rebirth", "players", player.name)
        val level = data?.getInt("level", "players", player.name)
        val balance = economy?.getBalance(player)?.toInt()
        val requiredLevel = manager?.getRebirthConfig()!!.getInt("rebirths.$rebirth.stats.level.count")
        val requiredBalance = manager.getRebirthConfig()!!.getInt("rebirths.$rebirth.price")

        if (balance!! < requiredBalance) {
            sendCommandsAPI.sendFromConfig(player.name, manager.getRebirthConfig()!!.getStringList("moreMoney"))
            return
        } else if (level!! < requiredLevel) {
            sendCommandsAPI.sendFromConfig(player.name, manager.getRebirthConfig()!!.getStringList("moreStats.level"))
            return
        }

        player.closeInventory()

        sendCommandsAPI.sendFromConfig(player.name, manager.getRebirthConfig()!!.getStringList("rebirths.$rebirth.commands"))
    }
}