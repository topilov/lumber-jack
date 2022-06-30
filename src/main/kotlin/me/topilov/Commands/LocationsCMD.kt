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

class LocationsCMD : CommandExecutor, Listener {

    private val manager = app.manager
    private val data = app.data
    private val configItemStackAPI = ConfigItemStackAPI()
    private val sendCommandsAPI = SendCommandsAPI()

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>?): Boolean {
        if (cmd.name == "locations") {
            if (sender is ConsoleCommandSender) return true

            val player = sender as Player

            val inventory = Bukkit.getServer().createInventory(player, 45, "Локации")

            addItemsToInventory(inventory, player)

            player.openInventory(inventory)
        }

        return true
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        if (e.view.title != "Локации") return

        val player = e.whoClicked as Player

        e.isCancelled = true

        if (e.currentItem!!.hasItemMeta() && e.currentItem?.itemMeta!!.displayName == "§cНедоступно") return

        manager?.getLocationsConfig()?.getConfigurationSection("locations")?.getKeys(false)?.forEach{ key ->

            val name = manager.getLocationsConfig()!!.getString("locations.$key.guiItem.name")
            val commands = manager.getLocationsConfig()!!.getStringList("locations.$key.commandsOnTeleport")

            if (e.currentItem!!.hasItemMeta() && e.currentItem?.itemMeta!!.displayName == name) {
                sendCommandsAPI.sendFromConfig(player.name, commands)
                return
            }

        }

    }

    private fun addItemsToInventory(inventory: Inventory, player: Player) {

        manager?.getLocationsConfig()?.getConfigurationSection("locations")?.getKeys(false)?.forEach { key ->

            val requiredLevel = key.toInt()
            val level = data?.getInt("level", "players", player.name)
            val slot = manager.getLocationsConfig()!!.getInt("locations.$key.slot")

            if (level!! >= requiredLevel) {

                val itemStack = configItemStackAPI.getItemStack("locations.$key.guiItem", "locations.$key.guiItem",
                    manager.getLocationsConfig()!!, player)

                inventory.setItem(slot, itemStack)

            } else {

                val itemStack = configItemStackAPI.getItemStack("disabledLocation", "disabledLocation",
                    manager.getLocationsConfig()!!, player)

                inventory.setItem(slot, itemStack)
            }


        }
    }
}