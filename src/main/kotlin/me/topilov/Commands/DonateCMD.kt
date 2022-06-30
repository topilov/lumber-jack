package me.topilov.Commands

import me.topilov.Utils.ConfigItemStackAPI
import me.topilov.Utils.SendCommandsAPI
import me.topilov.app
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class DonateCMD: CommandExecutor, Listener {

    private val sendCommandsAPI = SendCommandsAPI()
    private val configItemStackAPI = ConfigItemStackAPI()
    val manager = app.manager
    val inventory = Bukkit.getServer().createInventory(null, 54, "Внутриигровые покупки")

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>?): Boolean {
        if (!cmd.name.equals("donate", ignoreCase = true)) return false
        if (sender !is Player) return false

        addItemsToInventory(sender)

        sender.openInventory(inventory)


        return true
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        if (e.view.title != "Внутриигровые покупки") return

        val player = e.whoClicked as Player


    }

    private fun buyCurrentItem(currentItem: ItemStack, player: Player) {
        manager?.getDonateGUI()?.getConfigurationSection("items")?.getKeys(false)?.forEach { key ->

            if (currentItem.itemMeta!!.displayName == manager.getDonateGUI()!!.getString("items.$key.view.name")) {

                val requiredTokens = manager.getDonateGUI()?.getInt("items.$key.price")
                val playerTokens = 123

                if (playerTokens < requiredTokens!!) {
                    sendCommandsAPI.sendFromConfig(player.name, manager.getDonateGUI()?.getStringList("items.$key.price")!!)
                    return
                }

                sendCommandsAPI.sendFromConfig(player.name, manager.getDonateGUI()!!.getStringList("items.$key.commands"))

            }
        }
    }

    private fun addItemsToInventory(player: Player) {
        manager?.getDonateGUI()?.getConfigurationSection("items")?.getKeys(false)?.forEach { key ->

            val slot = manager.getDonateGUI()!!.getInt("items.$key.view.slot")

            val itemStack = configItemStackAPI.getItemStack("items.$key.view", "items.$key",
                manager.getDonateGUI()!!, player)

            inventory.setItem(slot, itemStack)

        }
    }
}