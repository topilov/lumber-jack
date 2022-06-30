package me.topilov.Utils
import me.topilov.app
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Economy : CommandExecutor {

    private val economy: net.milkbowl.vault.economy.Economy? = app.economy

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (command.name.equals("eco", ignoreCase = true)) {

            val player = sender as Player

            if (args[0].equals("add", ignoreCase = true)) {
                if (!sender.isOp) {
                    sender.sendMessage(ChatColor.DARK_RED.toString() + "Недостаточно прав")
                    return true
                }
                if (args.size == 3) {
                    try {
                        val target = Bukkit.getPlayer(args[1])
                        val depositAmount = args[2].toDouble()

                        economy?.depositPlayer(player, depositAmount)

                        assert(target != null)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                return true
            }
            if (args[0].equals("balance", ignoreCase = true)) {
                if (args.size == 1) {
                    try {
                        val balance = economy?.getBalance(sender)
                        sender.sendMessage(ChatColor.YELLOW.toString() + "Баланс: " + balance)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                return true
            }
            if (args[0].equals("remove", ignoreCase = true)) {
                if (args.size == 2) {
                    try {
                        val withdrawAmount = args[1].toDouble()
                        economy?.withdrawPlayer(sender, withdrawAmount)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                return true
            }
        }

        return true
    }

}
