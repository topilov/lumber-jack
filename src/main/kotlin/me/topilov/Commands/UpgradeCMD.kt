package me.topilov.Commands

import de.tr7zw.nbtapi.NBTItem
import me.topilov.Utils.ConfigItemStackAPI
import me.topilov.Utils.ConfigManager
import me.topilov.Utils.SendCommandsAPI
import me.topilov.app
import net.milkbowl.vault.economy.Economy
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

class UpgradeCMD : CommandExecutor, Listener {

    private var manager: ConfigManager? = app.manager
    private var configItemStackAPI = ConfigItemStackAPI()
    private val sendCommandsAPI = SendCommandsAPI()
    private val economy: Economy? = app.economy

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>?): Boolean {
        if (cmd.name == "upgrade") {
            if (sender is ConsoleCommandSender) return true

            val player = sender as Player
            val inventory = Bukkit.getServer().createInventory(player, 9, "Улучшение предмета")

            if (player.inventory.itemInMainHand.type == Material.AIR) {
                sendCommandsAPI.sendFromConfig(player.name, manager?.getUpgradeConfig()!!.getStringList("itemNone"))
                return true
            }

            try {
                addItemToInventory(inventory, player)
            } catch (ex: java.lang.NullPointerException) {
                sendCommandsAPI.sendFromConfig(player.name, manager?.getUpgradeConfig()!!.getStringList("maxLevel"))
                return true
            }

            player.openInventory(inventory)
        }

        return true
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        if (e.view.title != "Улучшение предмета") return
        if (e.slot != 4) return

        e.isCancelled = true
        upgradeItem(e.whoClicked as Player)
    }

    private fun addItemToInventory(inventory: Inventory, player: Player) {

        val nbtItem = NBTItem(player.inventory.itemInMainHand)
        val item: String = nbtItem.getString("item")
        val level: Int = nbtItem.getInteger("level") + 1
        //val itemStack = configItemStackAPI.getUpgradeItem((level + 1), item, player)

        val itemStack = configItemStackAPI.getItemStack("items.$item.$level.guiItem", "items.$item.$level",
            manager?.getUpgradeConfig()!!, player)

        inventory.setItem(4, itemStack)
    }

    private fun upgradeItem(player: Player) {

        val nbtItemPlayer = NBTItem(player.inventory.itemInMainHand)
        val level: Int = nbtItemPlayer.getInteger("level") + 1
        val item: String = nbtItemPlayer.getString("item")
        val balance = economy?.getBalance(player)
        val requiredMoney = manager?.getUpgradeConfig()?.getInt("items.$item.$level.price")

        if (balance!! < requiredMoney!!) {
            sendCommandsAPI.sendFromConfig(player.name, manager?.getUpgradeConfig()!!.getStringList("moreStats.moreMoney"))
            return
        }

        player.closeInventory()
        economy?.withdrawPlayer(player, requiredMoney.toDouble())
        sendCommandsAPI.sendFromConfig(player.name, manager?.getUpgradeConfig()!!.getStringList("items.$item.$level.commands"))

        var itemStack = configItemStackAPI.getItemStack("items.$item.$level", "items.$item.$level",
            manager?.getUpgradeConfig()!!, player)

        val nbtRequired = NBTItem(itemStack)
        nbtRequired.setString("item", item)
        nbtRequired.setInteger("level", level)
        itemStack = nbtRequired.item

        player.inventory.setItemInMainHand(itemStack)
    }
}