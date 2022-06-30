package me.topilov.Commands

import de.tr7zw.nbtapi.NBTItem
import me.topilov.Utils.ConfigItemStackAPI
import me.topilov.Utils.PetsAPI
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


class PetsCMD: CommandExecutor, Listener {

    private val petsAPI = PetsAPI()
    val sendCommandsAPI = SendCommandsAPI()
    val configItemStackAPI = ConfigItemStackAPI()
    val manager = app.manager
    val data = app.data
    val economy = app.economy

    override fun onCommand(player: CommandSender, cmd: Command, label: String, args: Array<out String>?): Boolean {
        if (!cmd.name.equals("pets", ignoreCase = true)) return false
        if (player !is Player) return false

        val inventory = Bukkit.getServer().createInventory(player, 45, "Питомцы")

        addItemsToInventory(player, inventory)

        player.openInventory(inventory)

        return true
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {

        val player = e.whoClicked as Player

        if (e.view.title == "Питомцы") {
            if (e.currentItem?.type != Material.PLAYER_HEAD) return

            e.isCancelled = true

            val pets = app.activePets[player]?.split(":")
            val size = pets?.size!! - 1
            val maxSlots = data!!.getInt("maxSlots", "pets", player.name)

            if (e.isLeftClick) {

                manager?.getPetsUpgrade()?.getConfigurationSection("pets")?.getKeys(false)?.forEach { key ->

                    if (e.currentItem?.itemMeta?.displayName?.equals(
                            manager.getPetsUpgrade()?.getString("pets.$key.item.name")
                                ?.replace("%level%", petsAPI.getPetLevel(player, key.toInt()).toString())!!
                        )!!) {

                        if (!pets.contains(key)) {

                            player.sendMessage(size.toString())
                            player.sendMessage(app.activePets[player]!!)

                            if (size >= maxSlots) {
                                sendCommandsAPI.sendFromConfig(
                                    player.name,
                                    manager.getPetsUpgrade()?.getStringList("maxPetsCommands")!!)

                                return
                            }

                            sendCommandsAPI.sendActionWithPet(
                                player.name,
                                manager.getPetsUpgrade()?.getStringList("equipOkCommands")!!,
                                petsAPI.getPetLevel(player, key.toInt()),
                                manager.getPetsUpgrade()?.getString("pets.$key.name")!!
                            )

                            app.armorStandsPets[player]?.forEach { pet ->
                                pet.remove()
                            }

                            app.armorStandsPets.remove(player)

                            app.activePets[player] = app.activePets[player] + "$key:"

                            petsAPI.spawnPets(player)
                            petsAPI.reloadPetBoosters(player)

                            player.closeInventory()

                        } else {
                            sendCommandsAPI.sendActionWithPet(
                                player.name,
                                manager.getPetsUpgrade()?.getStringList("equipNoCommands")!!,
                                petsAPI.getPetLevel(player, key.toInt()),
                                manager.getPetsUpgrade()?.getString("pets.$key.name")!!
                            )

                            app.armorStandsPets[player]?.forEach { pet ->
                                pet.remove()
                            }

                            app.armorStandsPets.remove(player)

                            app.activePets[player] = app.activePets[player]?.replace("$key:", "")!!

                            petsAPI.spawnPets(player)
                            petsAPI.reloadPetBoosters(player)

                            player.closeInventory()
                        }
                    }
                }

            } else if (e.isRightClick) {

                val inventory = Bukkit.getServer().createInventory(player, 9, "Улучшение")

                manager?.getPetsUpgrade()?.getConfigurationSection("pets")?.getKeys(false)?.forEach { key ->
                    if (e.currentItem?.itemMeta?.displayName?.equals(
                            manager.getPetsUpgrade()?.getString("pets.$key.item.name")
                                ?.replace("%level%", petsAPI.getPetLevel(player, key.toInt()).toString())!!)!!) {

                        val itemStack = configItemStackAPI.getMoreUpgradeItemStack(
                            "pets.$key.upgradable.defaultTier.guiItem",
                            manager.getPetsUpgrade()!!, manager.getMoreUpgrade()!!, player)

                        inventory.setItem(4, itemStack)
                        player.openInventory(inventory)

                        return
                    }
                }
            }
        } else if (e.view.title == "Улучшение") {

            if (e.currentItem?.type != Material.CLAY_BALL) {
                e.isCancelled = true
                return
            }

            e.isCancelled = true

            val pet = NBTItem(e.currentItem).getString("pet")

            upgradePet(player, pet.toInt())

            player.closeInventory()

        }
    }

    private fun upgradePet(player: Player, numberPet: Int) {

        val needMoney = 1.65 * petsAPI.getPetLevel(player, numberPet) + 25000
        val needRebirth = 1 + petsAPI.getPetLevel(player, numberPet)
        val currentMoney = economy?.getBalance(player)
        val currentRebirth = data!!.getInt("rebirth", "players", player.name)
        val petLevel = petsAPI.getPetLevel(player, numberPet)

        if (petLevel >= manager?.getPetsUpgrade()?.getInt("pets.$numberPet.maxLevel")!!) {
            sendCommandsAPI.sendFromConfig(player.name, manager.getMoreUpgrade()?.getStringList("defaultMaxLevelCommands")!!)
            return

        } else if (needMoney > currentMoney!!) {
            sendCommandsAPI.sendFromConfig(player.name, manager.getMoreUpgrade()?.getStringList("defaultNeedStatCommands.money")!!)
            return

        } else if (needRebirth > currentRebirth) {
            sendCommandsAPI.sendFromConfig(player.name, manager.getMoreUpgrade()?.getStringList("defaultNeedStatCommands.rebirth")!!)
            return

        }

        economy?.withdrawPlayer(player, needMoney)
        sendCommandsAPI.sendFromConfig(player.name, manager.getPetsUpgrade()?.getStringList("pets.$numberPet.upgradable.defaultTier.commandsOnBuy")!!)
        petsAPI.addPetLevel(player, numberPet, 1)

        petsAPI.reloadPets(player)
        petsAPI.reloadPetBoosters(player)
    }

    private fun addItemsToInventory(player: Player, inventory: Inventory) {
        manager?.getPetsUpgrade()?.getConfigurationSection("pets")?.getKeys(false)?.forEach{key ->

            val slot = manager.getPetsUpgrade()?.getInt("pets.$key.item.slot")

            if (petsAPI.getPetLevel(player, key.toInt()) > 0) {
                val itemStack = configItemStackAPI.getPetItemStack("pets.$key.item", key.toInt(), manager.getPetsUpgrade()!!, player)
                inventory.setItem(slot!!, itemStack)
            } else {
                val itemStack = configItemStackAPI.getItemStack("fillItem", "fillItem", manager.getPetsUpgrade()!!, player)
                inventory.setItem(slot!!, itemStack)
            }

        }
    }
}