package me.topilov.Utils

import me.topilov.app
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader

class ConfigManager {

    private var levelupConfigFile: File? = null
    private var levelupConfig: FileConfiguration? = null
    private var dataBaseConfigFile: File? = null
    private var dataBaseConfig: FileConfiguration? = null
    private var upgradeConfigFile: File? = null
    private var upgradeConfig: FileConfiguration? = null
    private var chanceCMDConfigFile: File? = null
    private var chanceCMDConfig: FileConfiguration? = null
    private var rebirthConfigFile: File? = null
    private var rebirthConfig: FileConfiguration? = null
    private var guiCommandConfigFile: File? = null
    private var guiCommandConfig: FileConfiguration? = null
    private var locationsConfigFile: File? = null
    private var locationsConfig: FileConfiguration? = null
    private var placeHolderConfigFile: File? = null
    private var placeHolderConfig: FileConfiguration? = null
    private var chatConfigFile: File? = null
    private var chatConfig: FileConfiguration? = null
    private var disableItemDropConfigFile: File? = null
    private var disableItemDropConfig: FileConfiguration? = null
    private var showBoosterConfigFile: File? = null
    private var showBoosterConfig: FileConfiguration? = null
    private var thanksConfigFile: File? = null
    private var thanksConfig: FileConfiguration? = null
    private var softEcoConfigFile: File? = null
    private var softEcoConfig: FileConfiguration? = null
    private var donateGUIConfigFile: File? = null
    private var donateGUIConfig: FileConfiguration? = null
    private var ljConfigFile: File? = null
    private var ljConfig: FileConfiguration? = null
    private var sellAllConfigFile: File? = null
    private var sellAllConfig: FileConfiguration? = null
    private var petsConfigFile: File? = null
    private var petsConfig: FileConfiguration? = null
    private var petsUpgradeConfigFile: File? = null
    private var petsUpgradeConfig: FileConfiguration? = null
    private var moreUpgradeConfigFile: File? = null
    private var moreUpgradeConfig: FileConfiguration? = null
    private var petsBoosterConfigFile: File? = null
    private var petsBoosterConfig: FileConfiguration? = null

    private fun reloadLevelupConfig() {
        if (levelupConfigFile == null) {
            levelupConfigFile = File(app.dataFolder, "levelup.yml")
        }
        levelupConfig = YamlConfiguration.loadConfiguration(levelupConfigFile!!)
        val defaultStream: InputStream? = app.getResource("levelup.yml")
        if (defaultStream != null) {
            val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream))
            (levelupConfig as YamlConfiguration).setDefaults(defaultConfig)
        }
    }

    fun getLevelupConfig(): FileConfiguration? {
        if (levelupConfig == null) {
            reloadLevelupConfig()
        }
        return levelupConfig
    }

    private fun reloadDataBaseConfig() {
        if (dataBaseConfigFile == null) {
            dataBaseConfigFile = File(app.dataFolder, "database.yml")
        }
        dataBaseConfig = YamlConfiguration.loadConfiguration(dataBaseConfigFile!!)
        val defaultStream: InputStream? = app.getResource("database.yml")
        if (defaultStream != null) {
            val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream))
            (dataBaseConfig as YamlConfiguration).setDefaults(defaultConfig)
        }
    }

    fun getDataBaseConfig(): FileConfiguration? {
        if (dataBaseConfig == null) {
            reloadDataBaseConfig()
        }
        return dataBaseConfig
    }

    private fun reloadUpgradeConfig() {
        if (upgradeConfigFile == null) {
            upgradeConfigFile = File(app.dataFolder, "upgrade.yml")
        }
        upgradeConfig = YamlConfiguration.loadConfiguration(upgradeConfigFile!!)
        val defaultStream: InputStream? = app.getResource("upgrade.yml")
        if (defaultStream != null) {
            val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream))
            (upgradeConfig as YamlConfiguration).setDefaults(defaultConfig)
        }
    }

    fun getUpgradeConfig(): FileConfiguration? {
        if (upgradeConfig == null) {
            reloadUpgradeConfig()
        }
        return upgradeConfig
    }

    private fun reloadChanceCMDConfig() {
        if (chanceCMDConfigFile == null) {
            chanceCMDConfigFile = File(app.dataFolder, "chancecmd.yml")
        }
        chanceCMDConfig = YamlConfiguration.loadConfiguration(chanceCMDConfigFile!!)
        val defaultStream: InputStream? = app.getResource("chancecmd.yml")
        if (defaultStream != null) {
            val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream))
            (chanceCMDConfig as YamlConfiguration).setDefaults(defaultConfig)
        }
    }

    fun getChanceCMDConfig(): FileConfiguration? {
        if (chanceCMDConfig == null) {
            reloadChanceCMDConfig()
        }
        return chanceCMDConfig
    }

    private fun reloadRebirthConfig() {
        if (rebirthConfigFile == null) {
            rebirthConfigFile = File(app.dataFolder, "rebirth.yml")
        }
        rebirthConfig = YamlConfiguration.loadConfiguration(rebirthConfigFile!!)
        val defaultStream: InputStream? = app.getResource("rebirth.yml")
        if (defaultStream != null) {
            val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream))
            (rebirthConfig as YamlConfiguration).setDefaults(defaultConfig)
        }
    }

    fun getRebirthConfig(): FileConfiguration? {
        if (rebirthConfig == null) {
            reloadRebirthConfig()
        }
        return rebirthConfig
    }

    private fun reloadGuiCommandConfig() {
        if (guiCommandConfigFile == null) {
            guiCommandConfigFile = File(app.dataFolder, "guicommand.yml")
        }
        guiCommandConfig = YamlConfiguration.loadConfiguration(guiCommandConfigFile!!)
        val defaultStream: InputStream? = app.getResource("guicommand.yml")
        if (defaultStream != null) {
            val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream))
            (guiCommandConfig as YamlConfiguration).setDefaults(defaultConfig)
        }
    }

    fun getGuiCommandConfig(): FileConfiguration? {
        if (guiCommandConfig == null) {
            reloadGuiCommandConfig()
        }
        return guiCommandConfig
    }


    private fun reloadLocationsConfig() {
        if (locationsConfigFile == null) {
            locationsConfigFile = File(app.dataFolder, "locations.yml")
        }
        locationsConfig = YamlConfiguration.loadConfiguration(locationsConfigFile!!)
        val defaultStream: InputStream? = app.getResource("locations.yml")
        if (defaultStream != null) {
            val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream))
            (locationsConfig as YamlConfiguration).setDefaults(defaultConfig)
        }
    }

    fun getLocationsConfig(): FileConfiguration? {
        if (locationsConfig == null) {
            reloadLocationsConfig()
        }
        return locationsConfig
    }

    private fun reloadPlaceHolderConfig() {
        if (placeHolderConfigFile == null) {
            placeHolderConfigFile = File(app.dataFolder, "placeholder.yml")
        }
        placeHolderConfig = YamlConfiguration.loadConfiguration(placeHolderConfigFile!!)
        val defaultStream: InputStream? = app.getResource("placeholder.yml")
        if (defaultStream != null) {
            val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream))
            (placeHolderConfig as YamlConfiguration).setDefaults(defaultConfig)
        }
    }

    fun getPlaceHolderConfig(): FileConfiguration? {
        if (placeHolderConfig == null) {
            reloadPlaceHolderConfig()
        }
        return placeHolderConfig
    }

    private fun reloadChatConfig() {
        if (chatConfigFile == null) {
            chatConfigFile = File(app.dataFolder, "chat.yml")
        }
        chatConfig = YamlConfiguration.loadConfiguration(chatConfigFile!!)
        val defaultStream: InputStream? = app.getResource("chat.yml")
        if (defaultStream != null) {
            val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream))
            (chatConfig as YamlConfiguration).setDefaults(defaultConfig)
        }
    }

    fun getChatConfig(): FileConfiguration? {
        if (chatConfig == null) {
            reloadChatConfig()
        }
        return chatConfig
    }

    private fun reloadDisableItemDrop() {
        if (disableItemDropConfigFile == null) {
            disableItemDropConfigFile = File(app.dataFolder, "disableitemdrop.yml")
        }
        disableItemDropConfig = YamlConfiguration.loadConfiguration(disableItemDropConfigFile!!)
        val defaultStream: InputStream? = app.getResource("disableitemdrop.yml")
        if (defaultStream != null) {
            val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream))
            (disableItemDropConfig as YamlConfiguration).setDefaults(defaultConfig)
        }
    }

    fun getDisableItemDrop(): FileConfiguration? {
        if (disableItemDropConfig == null) {
            reloadDisableItemDrop()
        }
        return disableItemDropConfig
    }

    private fun reloadShowBooster() {
        if (showBoosterConfigFile == null) {
            showBoosterConfigFile = File(app.dataFolder, "showbooster.yml")
        }
        showBoosterConfig = YamlConfiguration.loadConfiguration(showBoosterConfigFile!!)
        val defaultStream: InputStream? = app.getResource("showbooster.yml")
        if (defaultStream != null) {
            val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream))
            (showBoosterConfig as YamlConfiguration).setDefaults(defaultConfig)
        }
    }

    fun getShowBooster(): FileConfiguration? {
        if (showBoosterConfig == null) {
            reloadShowBooster()
        }
        return showBoosterConfig
    }

    private fun reloadThanks() {
        if (thanksConfigFile == null) {
            thanksConfigFile = File(app.dataFolder, "thanks.yml")
        }
        thanksConfig = YamlConfiguration.loadConfiguration(thanksConfigFile!!)
        val defaultStream: InputStream? = app.getResource("thanks.yml")
        if (defaultStream != null) {
            val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream))
            (thanksConfig as YamlConfiguration).setDefaults(defaultConfig)
        }
    }

    fun getThanks(): FileConfiguration? {
        if (thanksConfig == null) {
            reloadThanks()
        }
        return thanksConfig
    }

    private fun reloadSoftEco() {
        if (softEcoConfigFile == null) {
            softEcoConfigFile = File(app.dataFolder, "softeco.yml")
        }
        softEcoConfig = YamlConfiguration.loadConfiguration(softEcoConfigFile!!)
        val defaultStream: InputStream? = app.getResource("softeco.yml")
        if (defaultStream != null) {
            val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream))
            (softEcoConfig as YamlConfiguration).setDefaults(defaultConfig)
        }
    }

    fun getSoftEco(): FileConfiguration? {
        if (softEcoConfigFile == null) {
            reloadSoftEco()
        }
        return softEcoConfig
    }

    private fun reloadDonateGUI() {
        if (donateGUIConfigFile == null) {
            donateGUIConfigFile = File(app.dataFolder, "donategui.yml")
        }
        donateGUIConfig = YamlConfiguration.loadConfiguration(donateGUIConfigFile!!)
        val defaultStream: InputStream? = app.getResource("donategui.yml")
        if (defaultStream != null) {
            val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream))
            (donateGUIConfig as YamlConfiguration).setDefaults(defaultConfig)
        }
    }

    fun getDonateGUI(): FileConfiguration? {
        if (donateGUIConfigFile == null) {
            reloadDonateGUI()
        }
        return donateGUIConfig
    }

    private fun reloadLj() {
        if (ljConfigFile == null) {
            ljConfigFile = File(app.dataFolder, "lj.yml")
        }
        ljConfig = YamlConfiguration.loadConfiguration(ljConfigFile!!)
        val defaultStream: InputStream? = app.getResource("lj.yml")
        if (defaultStream != null) {
            val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream))
            (ljConfig as YamlConfiguration).setDefaults(defaultConfig)
        }
    }

    fun getLj(): FileConfiguration? {
        if (ljConfigFile == null) {
            reloadLj()
        }
        return ljConfig
    }

    private fun reloadSellAll() {
        if (sellAllConfigFile == null) {
            sellAllConfigFile = File(app.dataFolder, "sellall.yml")
        }
        sellAllConfig = YamlConfiguration.loadConfiguration(sellAllConfigFile!!)
        val defaultStream: InputStream? = app.getResource("sellall.yml")
        if (defaultStream != null) {
            val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream))
            (sellAllConfig as YamlConfiguration).setDefaults(defaultConfig)
        }
    }

    fun getSellAll(): FileConfiguration? {
        if (sellAllConfigFile == null) {
            reloadSellAll()
        }
        return sellAllConfig
    }

    private fun reloadPets() {
        if (petsConfigFile == null) {
            petsConfigFile = File(app.dataFolder, "pets.yml")
        }
        petsConfig = YamlConfiguration.loadConfiguration(petsConfigFile!!)
        val defaultStream: InputStream? = app.getResource("pets.yml")
        if (defaultStream != null) {
            val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream))
            (petsConfig as YamlConfiguration).setDefaults(defaultConfig)
        }
    }

    fun getPets(): FileConfiguration? {
        if (petsConfigFile == null) {
            reloadPets()
        }
        return petsConfig
    }

    private fun reloadPetsUpgrade() {
        if (petsUpgradeConfigFile == null) {
            petsUpgradeConfigFile = File(app.dataFolder, "petsupgrade.yml")
        }
        petsUpgradeConfig = YamlConfiguration.loadConfiguration(petsUpgradeConfigFile!!)
        val defaultStream: InputStream? = app.getResource("petsupgrade.yml")
        if (defaultStream != null) {
            val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream))
            (petsUpgradeConfig as YamlConfiguration).setDefaults(defaultConfig)
        }
    }

    fun getPetsUpgrade(): FileConfiguration? {
        if (petsUpgradeConfigFile == null) {
            reloadPetsUpgrade()
        }
        return petsUpgradeConfig
    }

    private fun reloadMoreUpgrade() {
        if (moreUpgradeConfigFile == null) {
            moreUpgradeConfigFile = File(app.dataFolder, "moreupgrade.yml")
        }
        moreUpgradeConfig = YamlConfiguration.loadConfiguration(moreUpgradeConfigFile!!)
        val defaultStream: InputStream? = app.getResource("moreupgrade.yml")
        if (defaultStream != null) {
            val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream))
            (moreUpgradeConfig as YamlConfiguration).setDefaults(defaultConfig)
        }
    }

    fun getMoreUpgrade(): FileConfiguration? {
        if (moreUpgradeConfigFile == null) {
            reloadMoreUpgrade()
        }
        return moreUpgradeConfig
    }

    private fun reloadPetsBooster() {
        if (petsBoosterConfigFile == null) {
            petsBoosterConfigFile = File(app.dataFolder, "petsbooster.yml")
        }
        petsBoosterConfig = YamlConfiguration.loadConfiguration(petsBoosterConfigFile!!)
        val defaultStream: InputStream? = app.getResource("petsbooster.yml")
        if (defaultStream != null) {
            val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream))
            (petsBoosterConfig as YamlConfiguration).setDefaults(defaultConfig)
        }
    }

    fun getPetsBooster(): FileConfiguration? {
        if (petsBoosterConfigFile == null) {
            reloadPetsBooster()
        }
        return petsBoosterConfig
    }
}