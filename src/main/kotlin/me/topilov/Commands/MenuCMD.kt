package me.topilov.Commands

import de.tr7zw.nbtapi.NBTItem
import me.topilov.Utils.ConfigItemStackAPI
import me.topilov.Utils.SendCommandsAPI
import me.topilov.app
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack


class MenuCMD : CommandExecutor, Listener {

    private val manager = app.manager
    private val sendCommandsAPI = SendCommandsAPI()
    private val configItemStackAPI = ConfigItemStackAPI()

    private val inventory = Bukkit.getServer().createInventory(null, 45, "Меню")

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>?): Boolean {
        if (cmd.name == "menu") {
            if (sender is ConsoleCommandSender) return true

            val player = sender as Player

            addItemsToInventory(player, inventory)

            player.openInventory(inventory)

        }

        return true
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        if (e.view.title != "Меню") return

        e.isCancelled = true

        manager?.getGuiCommandConfig()?.getConfigurationSection("commands.menu.items")?.getKeys(false)?.forEach { mine ->

            if (e.currentItem?.itemMeta!!.displayName == manager.getGuiCommandConfig()!!.getString("commands.menu.items.$mine.item.name")) {

                e.whoClicked.closeInventory()

                sendCommandsAPI.sendFromConfig(e.whoClicked.name, manager.getGuiCommandConfig()!!.getStringList("commands.menu.items.$mine.commands"))

            }
        }
    }

    private fun addItemsToInventory(player: Player, inventory: Inventory) {
        manager?.getGuiCommandConfig()?.getConfigurationSection("commands.menu.items")?.getKeys(false)?.forEach { key ->

            val slot = manager.getGuiCommandConfig()!!.getInt("commands.menu.items.$key.item.slot")

            val itemStack = configItemStackAPI.getItemStack(
                "commands.menu.items.$key.item", "commands.menu.items.$key.item",
                manager.getGuiCommandConfig()!!, player)

            inventory.setItem(slot, itemStack)

        }
    }

}