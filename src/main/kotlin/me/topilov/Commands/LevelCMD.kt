package me.topilov.Commands

import me.topilov.Utils.ConfigItemStackAPI
import me.topilov.Utils.ConfigManager
import me.topilov.Utils.SQLGetter
import me.topilov.Utils.SendCommandsAPI
import me.topilov.app
import net.milkbowl.vault.economy.Economy
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

class LevelCMD : CommandExecutor, Listener {

    private val configItemStackAPI = ConfigItemStackAPI()
    private val sendCommandsAPI = SendCommandsAPI()
    private var manager: ConfigManager? = app.manager
    private var data: SQLGetter? = app.data
    private var economy: Economy? = app.economy

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (cmd.name.equals("level", ignoreCase = true)) {
            if (sender is ConsoleCommandSender) return true

            val player = sender as Player

            val inventory = Bukkit.getServer().createInventory(player, 9,
                manager?.getLevelupConfig()?.getString("inventory.title")!!)

            val level = data?.getInt("level", "players", player.name)

            if (level!! >= manager?.getLevelupConfig()?.getInt("maxLevel.max")!!) {
                sendCommandsAPI.sendFromConfig(player.name, manager!!.getLevelupConfig()!!.getStringList("maxLevel.commands"))
                return false
            }

            addItemsToInventory(player, inventory)

            player.openInventory(inventory)

        }
        return false
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        if (e.view.title == manager?.getLevelupConfig()?.getString("inventory.title")) {

            val player = e.whoClicked as Player

            if (e.slot == 4) {

                e.isCancelled = true
                upLevel(player)
            }
        }
    }

    private fun addItemsToInventory(player: Player, inventory: Inventory) {
        val level = data?.getInt("level", "players", player.name)

        val itemStack = configItemStackAPI.getItemStack("levels.$level", "levels.$level", manager?.getLevelupConfig()!!, player)

        inventory.setItem(4, itemStack)
    }

    private fun upLevel(player: Player) {

        val level = data?.getInt("level", "players", player.name)

        val balance = economy!!.getBalance(player)
        val blocks = data?.getDouble("broke_blocks", "players", player.name)
        val mobs = data?.getDouble("kills_mobs", "players", player.name)

        val requiredMoney = manager?.getLevelupConfig()?.getInt("levels.$level.stats.money.count")
        val requiredBlocks = manager?.getLevelupConfig()?.getInt("levels.$level.stats.broke_blocks.count")
        val requiredMobs = manager?.getLevelupConfig()?.getInt("levels.$level.stats.kills_mobs.count")

        if (balance < requiredMoney!!) {
            sendCommandsAPI.sendFromConfig(player.name, manager?.getLevelupConfig()!!.getStringList("moreStats.moreMoney"))
            return
        } else if (blocks!! < requiredBlocks!!) {
            sendCommandsAPI.sendFromConfig(player.name, manager?.getLevelupConfig()!!.getStringList("moreStats.broke_blocks"))
            return
        } else if (mobs!! < requiredMobs!!) {
            sendCommandsAPI.sendFromConfig(player.name, manager?.getLevelupConfig()!!.getStringList("moreStats.kills_mobs"))
            return
        }

        economy!!.withdrawPlayer(player, requiredMoney.toDouble())

        player.closeInventory()

        sendCommandsAPI.sendFromConfig(player.name, manager?.getLevelupConfig()!!.getStringList("levels.$level.commands"))
    }
}