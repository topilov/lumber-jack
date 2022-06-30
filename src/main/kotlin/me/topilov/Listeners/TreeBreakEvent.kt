package me.topilov.Listeners

import de.tr7zw.nbtapi.NBTItem
import me.topilov.Utils.ConfigItemStackAPI
import me.topilov.Utils.SendCommandsAPI
import me.topilov.app
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.scheduler.BukkitRunnable

class TreeBreakEvent : Listener {

    val manager = app.manager
    val sendCommandsAPI = SendCommandsAPI()
    private val configItemStackAPI = ConfigItemStackAPI()
    private val replaceBlocks = ArrayList<String>()

    @EventHandler
    fun onBreak(e: BlockBreakEvent) {

        if (e.player.inventory.itemInMainHand.type != Material.WOODEN_AXE
            && e.player.inventory.itemInMainHand.type != Material.IRON_AXE
            && e.player.inventory.itemInMainHand.type != Material.GOLDEN_AXE
            && e.player.inventory.itemInMainHand.type != Material.STONE_AXE
            && e.player.inventory.itemInMainHand.type != Material.DIAMOND_AXE) return

        val player = e.player
        val axe = NBTItem(player.inventory.itemInMainHand).getString("item")
        val world = player.world.name

    manager?.getLj()?.getConfigurationSection("axes.$axe.$world")?.getKeys(false)?.forEach{key ->
        if (e.block.type.name == key) {

            var time = 10
            val timeToReplace = manager.getLj()?.getInt("axes.$axe.$world.$key.replaceTime")!! * 20

            object : BukkitRunnable() {
                override fun run() {
                    e.block.type = Material.getMaterial(manager.getLj()?.getString("axes.$axe.$world.$key.replace")!!)!!

                    object : BukkitRunnable() {
                        override fun run() {
                            e.block.type = Material.getMaterial(key)!!
                        }
                    }.runTaskLater(app, timeToReplace.toLong())

                }
            }.runTaskLater(app, 5)

            for (blockList: Block in addNearbyBlocksToList(e.block.location)) {

                object : BukkitRunnable() {
                    override fun run() {

                        if (blockList.type.name == key) {

                            blockList.type =
                                Material.getMaterial(manager.getLj()?.getString("axes.$axe.$world.$key.replace")!!)!!

                            manager.getLj()?.getConfigurationSection("axes.$axe.$world.$key.drops")?.getKeys(false)
                                ?.forEach { key2 ->

                                    val chance = manager.getLj()?.getInt("axes.$axe.$world.$key.drops.$key2.chance")
                                    val commands =
                                        manager.getLj()?.getStringList("axes.$axe.$world.$key.drops.$key2.command")!!

                                    when ((Math.random() * (100 / chance!!) + 1).toInt()) {
                                        1 -> {
                                            sendCommandsAPI.sendFromConfig(player.name, commands)

                                            val itemStack = configItemStackAPI.getItemStack(
                                                "axes.$axe.$world.$key.drops.$key2",
                                                "axes.$axe.$world.$key",
                                                manager.getLj()!!, player
                                            )

                                            player.inventory.addItem(itemStack)

                                        }
                                    }
                                }
                        }

                        object : BukkitRunnable() {
                            override fun run() {
                                blockList.type = Material.getMaterial(key)!!
                            }
                        }.runTaskLater(app, timeToReplace.toLong())

                    }
                }.runTaskLater(app, time.toLong())

                time += 10
            }
        }
    }
}

    private fun addNearbyBlocksToList(location: Location): List<Block> {
        val blocks: MutableList<Block> = ArrayList()


        for (x in -1..1) {
            for (y in -3..3) {
                for (z in -1..1) {
                    if (location.clone().add(x.toDouble(), y.toDouble(), z.toDouble()).block.type == location.block.type)
                    blocks.add(location.clone().add(x.toDouble(), y.toDouble(), z.toDouble()).block)
                }
            }
        }

        return blocks
    }

}
