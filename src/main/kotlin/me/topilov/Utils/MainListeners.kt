package me.topilov.Utils

import de.tr7zw.nbtapi.NBTItem
import me.topilov.app
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

class MainListeners : Listener {

    val manager = app.manager

    @EventHandler
    fun onDrop(e: PlayerDropItemEvent) {

        val itemStack = e.itemDrop.itemStack
        val nbt = NBTItem(itemStack)

        if (nbt.getInteger(manager?.getDisableItemDrop()?.getString("nbt")) != 1) return

        e.player.sendMessage(manager?.getDisableItemDrop()?.getString("message")!!)
        e.isCancelled = true
    }
}