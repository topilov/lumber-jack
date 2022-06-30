package me.topilov.Commands

import me.topilov.Utils.ConfigItemStackAPI
import me.topilov.Utils.SendCommandsAPI
import me.topilov.app
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class BitsCMD : CommandExecutor, Listener  {

    val data = app.data
    val manager = app.manager
    private val configItemStackAPI = ConfigItemStackAPI()
    private val sendCommandsAPI = SendCommandsAPI()
    private val inventory = Bukkit.getServer().createInventory(null, 45, "Магазин за битсы")

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>?): Boolean {
        if (cmd.name != "bits") return false
        if (sender !is Player) return false

        addItemsToInventory(inventory, sender)

        sender.openInventory(inventory)

        return true
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        if (e.view.title != "Магазин за битсы") return

        e.isCancelled = true

        buyCurrentItem(e.currentItem!!, e.whoClicked as Player)
    }

    private fun addItemsToInventory(inventory: Inventory, player: Player) {
        manager?.getGuiCommandConfig()?.getConfigurationSection("commands.bits.items")?.getKeys(false)?.forEach { key ->

            val slot = manager.getGuiCommandConfig()!!.getInt("commands.bits.items.$key.item.slot")

            val itemStack = configItemStackAPI.getItemStack("commands.bits.items.$key.item", "commands.menu.items.$key.item",
                manager.getGuiCommandConfig()!!, player)

            inventory.setItem(slot, itemStack)

        }
    }

    private fun buyCurrentItem(currentItem: ItemStack, player: Player) {
        manager?.getGuiCommandConfig()?.getConfigurationSection("commands.bits.items")?.getKeys(false)?.forEach { key ->

            val path = "commands.bits.items.$key"

            if (currentItem.itemMeta!!.displayName == manager.getGuiCommandConfig()!!.getString("$path.item.name")) {

                val playerSub = data?.getInt("sub", "players", player.name)
                val playerRebirth = data?.getInt("rebirth", "players", player.name)
                val playerBits = data?.getInt("bits", "players", player.name)
                val requiredSub = manager.getGuiCommandConfig()?.getInt("$path.needStats.sub.count")
                val requiredRebirth = manager.getGuiCommandConfig()?.getInt("$path.needStats.rebirth.count")
                val requiredBits = manager.getGuiCommandConfig()?.getInt("$path.needStats.bits.count")

                if (playerSub != requiredSub) {
                    sendCommandsAPI.sendFromConfig(player.name, manager.getGuiCommandConfig()?.getStringList("$path.needStats.sub.error")!!)
                    return
                } else if (playerRebirth!! < requiredRebirth!!) {
                    sendCommandsAPI.sendFromConfig(player.name, manager.getGuiCommandConfig()?.getStringList("$path.needStats.rebirth.error")!!)
                    return
                } else if (playerBits!! < requiredBits!!) {
                    sendCommandsAPI.sendFromConfig(player.name, manager.getGuiCommandConfig()?.getStringList("$path.needStats.bits.error")!!)
                    return
                }

                sendCommandsAPI.sendFromConfig(player.name, manager.getGuiCommandConfig()!!.getStringList("$path.commands"))

            }
        }
    }
}