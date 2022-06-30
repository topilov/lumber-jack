package me.topilov.Utils

import de.tr7zw.nbtapi.NBTItem
import me.topilov.app
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask


class ActionCMD : CommandExecutor {

    private val data = app.data

    private val manager = app.manager
    private val sendCommandsAPI = SendCommandsAPI()
    private val configItemStackAPI = ConfigItemStackAPI()
    private val economy = app.economy

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>?): Boolean {
        if (cmd.name == "softeco") {
            if (sender is Player) return true

            //softeco %player% 250

            val player = Bukkit.getPlayer(args!![0])

            sendCommandsAPI.sendReward(manager?.getSoftEco()?.getStringList("message")!!, args[1].toInt(), player?.name!!)
            economy?.depositPlayer(player, args[1].toDouble())

            return true
        }
        if (cmd.name == "broadlocal") {
            if (sender is Player) return true

            //text %player% %message%

            val messageString = ArrayList<String>()

            for (i in 0 until args!!.size) {
                messageString.add(args[i])
            }

            val message = java.lang.String.join(" ", messageString)


            Bukkit.getOnlinePlayers().forEach{player ->
                player?.sendMessage(message)
            }

            return true
        }

        if (cmd.name == "text") {
            if (sender is Player) return true

            //text %player% %message%

            val player = Bukkit.getServer().getPlayer(args!![0])

            val messageString = ArrayList<String>()

            for (i in 1 until args.size) {
                messageString.add(args[i])
            }

            val message = java.lang.String.join(" ", messageString)

            player?.sendMessage(message)

            return true
        }

        if (cmd.name == "booster") {
            if (sender is Player) return true

            //booster player %player% add stats_player_exp 120 1

            //booster global %player% %add stats_player_exp 60 1

            if (args!![0] == "global") {

                val currentBooster = args[3]
                val xBooster = args[5].toDouble()
                val time = args[4].toLong() * 60
                val player = args[1]

                if (args[2] == "add") {
                    app.globalBoosters[currentBooster] = app.globalBoosters[currentBooster]!! + xBooster

                    val key = Math.random()
                    app.activeBoosters[key] = "$currentBooster:$time:$player"
                    app.thanks[key] = ""

                    object : BukkitRunnable() {
                        override fun run() {
                            app.globalBoosters[currentBooster] = app.globalBoosters[currentBooster]!! - xBooster
                            app.activeBoosters.remove(key)
                            app.thanks.remove(key)
                        }
                    }.runTaskLater(app, time * 20L)

                    object : BukkitRunnable() {
                        override fun run() {

                            val booster = app.activeBoosters[key]?.split(":")

                            if (app.activeBoosters.containsKey(key) && booster!![1].toInt() > 0) {
                                app.activeBoosters[key] = booster[0] + ":" + (booster[1].toInt() - 5) + ":" + booster[2]
                            } else {
                                cancel()
                            }

                        }
                    }.runTaskTimer(app, 5 * 20, 5 * 20)

                }
            }

            if (args[0] == "player") {

                val player = Bukkit.getServer().getPlayer(args[1])
                val currentBooster = args[3]
                val xBooster = args[5].toDouble()
                val time = args[4]

                if (args[2] == "add") {
                    app.getLocalBoosterMap(currentBooster)?.put(player!!, app.getLocalBoosterMap(currentBooster)?.get(player)!! + xBooster)

                    object : BukkitRunnable() {
                        override fun run() {
                            app.getLocalBoosterMap(currentBooster)?.put(player!!, app.getLocalBoosterMap(currentBooster)?.get(player)!! - xBooster)
                        }
                    }.runTaskLater(app, time.toLong() * 20L * 60L)

                }
            }


            return true
        }

        if (cmd.name == "getitem") {
            if (sender is Player) return true

            //getitem %player% money_axe 1

            val player = Bukkit.getServer().getPlayer(args!![0])

            var itemStack = configItemStackAPI.getItemStack("items." + args[1] + "." + args[2], "items." + args[1] + "." + args[2],
                manager?.getUpgradeConfig()!!, player!!)

            val nbtRequired = NBTItem(itemStack)
            nbtRequired.setString("item", args[1])
            nbtRequired.setInteger("level", args[2].toInt())
            itemStack = nbtRequired.item

            player.inventory.addItem(itemStack)

            return true
        }

        if (cmd.name == "loctp") {
            if (sender is Player) return true

            //sudo %player% %command%

            val player = Bukkit.getServer().getPlayer(args!![0])

            val location = manager?.getLocationsConfig()!!.get("locations." + args[1] + ".location") as Location

            player!!.teleport(location)

            return true
        }

        if (cmd.name == "sudo") {
            if (sender is Player) return true

            //sudo %player% %command%

            val player = Bukkit.getServer().getPlayer(args!![0])

            val commandList = ArrayList<String>()

            for (i in 1 until args.size) {
                commandList.add(args[i])
            }

            val command = "/" + java.lang.String.join(" ", commandList)

            player?.chat(command)

            return true
        }
        if (cmd.name == "morestats") {
            if (sender is Player) return true

            //morestats %player% level 1 false

            val player = Bukkit.getServer().getPlayer(args!![0])

            if (args[1] == "add") {
                data?.addInt(args[2], "players", player!!.name, args[3].toInt())
                return true
            }

            if (args[1] == "remove") {
                data?.removeInt(args[2], "players", player!!.name, args[3].toInt())
                return true
            }

            if (args[1] == "set") {
                data?.setInt(args[2], "players", player!!.name, args[3].toInt())
                return true
            }

            return true
        }
        if (cmd.name == "scrmsg") {
            if (sender is Player) return true

            ////scrmsg %player% normal §cУ вас максимальный уровень! | §7Используйте§e /rebirth§7, чтобы переродиться

            val player = Bukkit.getServer().getPlayer(args!![0])

            if (args[1] == "normal") {

                val messageString = ArrayList<String>()

                for (i in 2 until args.size) {
                    messageString.add(args[i])
                }

                val message = java.lang.String.join(" ", messageString)

                val parts = message.split(" | ")
                val title = parts[0]
                val subtitle = parts[1]

                player!!.sendTitle(title, subtitle, 10, 70, 20)
            }

            if (args[1] == "action") {

                val messageString = ArrayList<String>()

                for (i in 2 until args.size) {
                    messageString.add(args[i])
                }

                val message = java.lang.String.join(" ", messageString)

                player!!.sendActionBar(message)
            }

            return true
        }

        if (cmd.name == "chancecmd") {
            if (sender is Player) return true

            //chancecmd %player% %item% %chance%

            val player = Bukkit.getServer().getPlayer(args!![0])


            when ((Math.random() * (100 / args[2].toInt()) + 1).toInt()) {
                1 -> {
                    sendCommandsAPI.sendFromConfig(player!!.name, manager?.getChanceCMDConfig()?.getStringList("mapping." + args[1])!!)
                }
            }
            return true
        }

        if (cmd.name == "st") {
            if (sender is Player) return true

            //st %player% %message%

            val player = Bukkit.getServer().getPlayer(args!![0])

            val messageString = ArrayList<String>()

            for (i in 1 until args.size) {
                messageString.add(args[i])
            }

            val message = "§8[§ai§8]§f " + java.lang.String.join(" ", messageString)

            player?.sendMessage(message)

            return true
        }

        if (cmd.name == "et") {
            if (sender is Player) return true

            //et %player% %message%

            val player = Bukkit.getServer().getPlayer(args!![0])

            val messageString = ArrayList<String>()

            for (i in 1 until args.size) {
                messageString.add(args[i])
            }

            val message = "§8[§ci§8]§c " + java.lang.String.join(" ", messageString)

            player?.sendMessage(message)

            return true
        }

        if (cmd.name == "sound") {
            if (sender is Player) return true

            //sound %player% %sound% %volume% %pitch%

            val player = Bukkit.getServer().getPlayer(args!![0])

            player?.playSound(player.location, Sound.valueOf(args[1]), args[2].toFloat(), args[3].toFloat())

            return true
        }

        return false
    }
}