package me.topilov.Utils

import me.clip.placeholderapi.PlaceholderAPI
import me.topilov.app
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class ChatListener : Listener {

    val manager = app.manager

    @EventHandler
    fun on(e: AsyncPlayerChatEvent) {
        e.format = PlaceholderAPI.setPlaceholders(e.player,
            manager?.getChatConfig()?.getString("msg")?.replace("%player%", e.player.name)?.replace("%msg%", e.message)!!)
    }
}