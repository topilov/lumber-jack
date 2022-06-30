package me.topilov.Utils

import de.tr7zw.nbtapi.NBTItem
import me.clip.placeholderapi.PlaceholderAPI
import me.kodysimpson.simpapi.heads.SkullCreator
import me.topilov.Listeners.PetListener
import me.topilov.app
import org.bukkit.Material
import org.bukkit.block.Skull
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class ConfigItemStackAPI {

    private var manager: ConfigManager? = app.manager
    private var economy = app.economy
    private var data = app.data
    private val petsAPI = PetsAPI()

    fun getItemStack(path: String, pathToPrice: String, config: FileConfiguration, player: Player): ItemStack {

        var itemStack = ItemStack(Material.getMaterial(config.getString("$path.type")!!)!!,
            config.getInt("$path.amount"))

        val itemMeta = itemStack.itemMeta

        itemMeta.setDisplayName(config.getString("$path.name"))

        val newListLore = ArrayList<String>()

        for (string in config.getStringList("$path.lore")) {
            newListLore.add(string
                .replace("%ready_money%", getReadyMoney(pathToPrice, config, player))
                .replace("%current_money%", economy?.getBalance(player)?.toInt().toString())
                .replace("%need_money%", config.getInt("$pathToPrice.price").toString().toInt().toString())
                .replace("%ready_level%", getReadyLevel(path, config, player))
                .replace("%current_level%", data?.getInt("level", "players", player.name).toString())
                .replace("%need_level%", config.getInt("$path.stats.level.count").toString().toInt().toString())
                .replace("%ready_broke_blocks%", getReadyBlocks(path, config, player))
                .replace("%ready_kills_mobs%", getReadyMobs(path, config, player))
                .replace("%current_broke_blocks%", data?.getDouble("broke_blocks", "players", player.name)?.toInt().toString())
                .replace("%current_kills_mobs%", data?.getDouble("kills_mobs", "players", player.name)?.toInt().toString())
                .replace("%need_broke_blocks%", config.getInt("$path.stats.broke_blocks.count").toString())
                .replace("%need_kills_mobs%", config.getInt("$path.stats.kills_mobs.count").toString())
                .replace("%price%", config.getInt("$pathToPrice.price").toString())
                .replace("%money%", manager?.getMoreUpgrade()?.getString("defaultVisibleStat.money.line")!!))
        }

        itemMeta.lore = newListLore

        for (itemFlag in config.getStringList("$path.flags")) {
            itemMeta.addItemFlags(ItemFlag.valueOf(itemFlag))
        }

        for (enchant in config.getStringList("$path.enchantment")) {

            val values: Array<String> = enchant.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            itemMeta.addEnchant(Enchantment.getByName(values[0])!!, values[1].toInt(), true)
        }

        if (config.getBoolean("$path.unbreakable")) {
            itemMeta.isUnbreakable = true
        }

        itemStack.itemMeta = itemMeta

        /*if (useData) {
        }*/

        if (config.getString("$path.data")!!.isNotEmpty()) {

            val nbtRequired = NBTItem(itemStack)
            val nbtValues = config.getString("$path.data")!!.split(":")

            nbtRequired.setString(nbtValues[0], nbtValues[1])
            itemStack = nbtRequired.item
        }


        return itemStack
    }

    fun getPetItemStack(path: String, petNumber: Int, config: FileConfiguration, player: Player): ItemStack {

        var itemStack = SkullCreator.itemFromBase64(manager?.getPets()?.getString("pets.$petNumber.helmetOwnerTexture"))

        val itemMeta = itemStack.itemMeta

        itemMeta.setDisplayName(
            config.getString("$path.name")!!.replace("%level%", petsAPI.getPetLevel(player, petNumber).toString()))

        val newListLore = ArrayList<String>()

        for (string in config.getStringList("$path.lore")) {
            newListLore.add(string
                .replace("%equip%", getEquip(petNumber, player, config)))
        }

        itemMeta.lore = newListLore

        for (itemFlag in config.getStringList("$path.flags")) {
            itemMeta.addItemFlags(ItemFlag.valueOf(itemFlag))
        }

        for (enchant in config.getStringList("$path.enchantment")) {

            val values: Array<String> = enchant.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            itemMeta.addEnchant(Enchantment.getByName(values[0])!!, values[1].toInt(), true)
        }

        if (config.getBoolean("$path.unbreakable")) {
            itemMeta.isUnbreakable = true
        }

        itemStack.itemMeta = itemMeta


        /*if (useData) {
        }*/

        if (config.getString("$path.data")!!.isNotEmpty()) {

            val nbtRequired = NBTItem(itemStack)
            val nbtValues = config.getString("$path.data")!!.split(":")

            nbtRequired.setString(nbtValues[0], nbtValues[1])
            itemStack = nbtRequired.item
        }


        return itemStack
    }

    fun getMoreUpgradeItemStack(path: String, config: FileConfiguration, configMoreUpgrade: FileConfiguration, player: Player): ItemStack {

        var itemStack = ItemStack(Material.getMaterial(config.getString("$path.type")!!)!!,
            config.getInt("$path.amount"))

        val itemMeta = itemStack.itemMeta

        itemMeta.setDisplayName(config.getString("$path.name"))

        val newListLore = ArrayList<String>()

        for (string in config.getStringList("$path.lore")) {
            newListLore.add(string
                .replace("%money%", configMoreUpgrade.getString("defaultVisibleStat.money.line")!!)
                .replace("%rebirth%", configMoreUpgrade.getString("defaultVisibleStat.rebirth.line")!!))
        }

        itemMeta.lore = newListLore

        for (itemFlag in config.getStringList("$path.flags")) {
            itemMeta.addItemFlags(ItemFlag.valueOf(itemFlag))
        }

        for (enchant in config.getStringList("$path.enchantment")) {

            val values: Array<String> = enchant.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            itemMeta.addEnchant(Enchantment.getByName(values[0])!!, values[1].toInt(), true)
        }

        if (config.getBoolean("$path.unbreakable")) {
            itemMeta.isUnbreakable = true
        }

        itemStack.itemMeta = itemMeta

        /*if (useData) {
        }*/


        for (nbt in config.getStringList("$path.data")) {
            val nbtRequired = NBTItem(itemStack)
            val nbtValues = nbt.split(":")

            nbtRequired.setString(nbtValues[0], nbtValues[1])
            itemStack = nbtRequired.item
        }


        return itemStack
    }

    private fun getEquip(petNumber: Int, player: Player, config: FileConfiguration): String {
        val pets = app.activePets[player]?.split(":")
        return if (!pets?.contains(petNumber.toString())!!) {
            config.getString("equipOk")!!
        } else {
            config.getString("equipNo")!!
        }
    }


    private fun getReadyMoney(path: String, config: FileConfiguration, player: Player): String {

        val balance = economy?.getBalance(player)?.toInt()

        return if (balance!! >= config.getInt("$path.price")) {
            config.getString("moneyOk").toString()
        } else {
            config.getString("moneyNo").toString()
        }
    }


    private fun getReadyBlocks(path: String, config: FileConfiguration, player: Player): String {

        val blocks = data?.getDouble("broke_blocks", "players", player.name)

        return if (blocks!! >= manager?.getLevelupConfig()?.getInt("$path.stats.broke_blocks.count")!!) {
            config.getString("$path.stats.broke_blocks.readyOk").toString()
        } else {
            config.getString("$path.stats.broke_blocks.readyNo").toString()
        }
    }

    private fun getReadyMobs(path: String, config: FileConfiguration, player: Player): String {

        val mobs = data?.getDouble("kills_mobs", "players", player.name)

        return if (mobs!! >= manager?.getLevelupConfig()?.getInt("$path.stats.kills_mobs.count")!!) {
            config.getString("$path.stats.kills_mobs.readyOk").toString()
        } else {
            config.getString("$path.stats.kills_mobs.readyNo").toString()
        }
    }

    private fun getReadyLevel(path: String, config: FileConfiguration, player: Player): String {

        val level = data?.getInt("level", "players", player.name)

        return if (level!! >= config.getInt("$path.stats.level.count")) {
            config.getString("$path.stats.level.readyOk").toString()
        } else {
            config.getString("$path.stats.level.readyNo").toString()
        }
    }
}
