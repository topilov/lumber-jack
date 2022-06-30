package me.topilov

import me.topilov.Commands.*
import me.topilov.Listeners.JoinEvent
import me.topilov.Listeners.PetListener
import me.topilov.Listeners.TreeBreakEvent
import me.topilov.Utils.*
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.java.JavaPlugin
import java.sql.SQLException


lateinit var app: App

class App : JavaPlugin() {

    var manager: ConfigManager? = null
    var economy: Economy? = null
    var sql: MySQL? = null
    var data: SQLGetter? = null
    val activePets: MutableMap<Player, String> = HashMap()
    val thanks: MutableMap<Double, String> = HashMap()
    val activeBoosters: MutableMap<Double, String> = HashMap()
    val localExpBooster: MutableMap<Player, Double> = HashMap()
    val localMoneyBooster: MutableMap<Player, Double> = HashMap()
    val localBlocksBooster: MutableMap<Player, Double> = HashMap()
    val localMobsBooster: MutableMap<Player, Double> = HashMap()
    val globalBoosters: MutableMap<String, Double> = HashMap()
    val armorStandsPets: MutableMap<Player, ArrayList<ArmorStand>> = HashMap()
    val petExpBooster: MutableMap<Player, Double> = HashMap()
    val petMoneyBooster: MutableMap<Player, Double> = HashMap()
    val petBlocksBooster: MutableMap<Player, Double> = HashMap()
    val petMobsBooster: MutableMap<Player, Double> = HashMap()

    override fun onEnable() {

        app = this
        manager = ConfigManager()

        setupDatabase()
        setupEconomy()
        setupCommands()
        setupEvents()
        loadWorlds()
        putGlobalBoosters()

        PlaceHolder().register()


    }

    override fun onDisable() {
        sql?.disconnect()
        Bukkit.getOnlinePlayers().forEach{player ->
            player.kickPlayer("Server is restarting")
        }

        unloadWorlds()
    }

    fun putGlobalBoosters() {
        globalBoosters["stats_player_exp"] = 0.0
        globalBoosters["stats_money"] = 0.0
        globalBoosters["stats_broke_blocks"] = 0.0
        globalBoosters["stats_kills_mobs"] = 0.0
    }

    private fun unloadWorlds() {
        for (world: World in Bukkit.getWorlds()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mw unload ${world.name}")
        }
    }

    private fun loadWorlds() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mw load forest1")
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mw load forest2")
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mw load forest3")
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mw load forest4")
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mw load forest5")
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mw load forest6")
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mw load forest7")
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mw load forest8")
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mw load forest9")
        Bukkit.getWorld("forest1")?.isAutoSave = false
        Bukkit.getWorld("forest2")?.isAutoSave = false
        Bukkit.getWorld("forest3")?.isAutoSave = false
        Bukkit.getWorld("forest4")?.isAutoSave = false
        Bukkit.getWorld("forest5")?.isAutoSave = false
        Bukkit.getWorld("forest6")?.isAutoSave = false
        Bukkit.getWorld("forest7")?.isAutoSave = false
        Bukkit.getWorld("forest8")?.isAutoSave = false
        Bukkit.getWorld("forest9")?.isAutoSave = false
    }

    private fun setupCommands() {
        getCommand("test")!!.setExecutor(TestCMD())
        getCommand("rebirth")!!.setExecutor(RebirthCMD())
        getCommand("level")!!.setExecutor(LevelCMD())
        getCommand("upgrade")!!.setExecutor(UpgradeCMD())
        getCommand("st")!!.setExecutor(ActionCMD())
        getCommand("et")!!.setExecutor(ActionCMD())
        getCommand("sound")!!.setExecutor(ActionCMD())
        getCommand("chancecmd")!!.setExecutor(ActionCMD())
        getCommand("scrmsg")!!.setExecutor(ActionCMD())
        getCommand("toast")!!.setExecutor(ActionCMD())
        getCommand("morestats")!!.setExecutor(ActionCMD())
        getCommand("menu")!!.setExecutor(MenuCMD())
        getCommand("sudo")!!.setExecutor(ActionCMD())
        getCommand("locations")!!.setExecutor(LocationsCMD())
        getCommand("loctp")!!.setExecutor(ActionCMD())
        getCommand("getitem")!!.setExecutor(ActionCMD())
        getCommand("booster")!!.setExecutor(ActionCMD())
        getCommand("thanks")!!.setExecutor(ThanksCMD())
        getCommand("broadlocal")!!.setExecutor(ActionCMD())
        getCommand("softeco")!!.setExecutor(ActionCMD())
        getCommand("text")!!.setExecutor(ActionCMD())
        getCommand("bits")!!.setExecutor(BitsCMD())
        getCommand("donate")!!.setExecutor(DonateCMD())
        getCommand("sellall")!!.setExecutor(SellAllCMD())
        getCommand("pets")!!.setExecutor(PetsCMD())
        getCommand("eco")!!.setExecutor(me.topilov.Utils.Economy())
    }

    private fun setupEvents() {
        server.pluginManager.registerEvents(TreeBreakEvent(), this)
        server.pluginManager.registerEvents(UpgradeCMD(), this)
        server.pluginManager.registerEvents(LevelCMD(), this)
        server.pluginManager.registerEvents(JoinEvent(), this)
        server.pluginManager.registerEvents(RebirthCMD(), this)
        server.pluginManager.registerEvents(MenuCMD(), this)
        server.pluginManager.registerEvents(LocationsCMD(), this)
        server.pluginManager.registerEvents(ChatListener(), this)
        server.pluginManager.registerEvents(ScoreBoard(), this)
        server.pluginManager.registerEvents(MainListeners(), this)
        server.pluginManager.registerEvents(BossBar(), this)
        server.pluginManager.registerEvents(BitsCMD(), this)
        server.pluginManager.registerEvents(PetsCMD(), this)
        server.pluginManager.registerEvents(PetListener(), this)
    }

    private fun setupDatabase() {
        sql = MySQL()
        data = SQLGetter()
        try {
            sql!!.connect()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        if (sql!!.isConnected()) {
            Bukkit.getLogger().info("Database is connected!")
            data!!.createTablePlayers()
            data!!.createTablePets()
        }
    }

    private fun setupEconomy(): Boolean {
        val economyProvider: RegisteredServiceProvider<Economy> =
            server.servicesManager.getRegistration(net.milkbowl.vault.economy.Economy::class.java) as RegisteredServiceProvider<Economy>
        app.economy = economyProvider.provider
        return app.economy != null
    }



    fun getLocalBoosterMap(booster: String): MutableMap<Player, Double>? {
        if (booster == "stats_player_exp") {
            return localExpBooster
        }

        if (booster == "stats_money") {
            return localMoneyBooster
        }

        if (booster == "stats_broke_blocks") {
            return localBlocksBooster
        }

        if (booster == "stats_kills_mobs") {
            return localMobsBooster
        }

        return null
    }

    fun getPetBoosterHashMap(hashMap: String): MutableMap<Player, Double>? {
        if (hashMap == "stats_money") {
            return petMoneyBooster
        }

        if (hashMap == "stats_player_exp") {
            return petExpBooster
        }

        if (hashMap == "stats_broke_blocks") {
            return petBlocksBooster
        }

        if (hashMap == "stats_kills_mobs") {
            return petMobsBooster
        }

        return null
    }

    fun getTotalBooster(booster: String, player: Player): Double {

        return data?.getDouble(booster, "players", player.name)!! +
                globalBoosters[booster]!! +
                getLocalBoosterMap(booster)?.get(player)!! +
                petExpBooster.get(player)!!
    }

}