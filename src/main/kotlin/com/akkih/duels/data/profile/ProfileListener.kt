package com.akkih.duels.data.profile

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class ProfileListener(private val profileRegistry: ProfileRegistry) : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        profileRegistry.handleJoin(event.player)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        profileRegistry.handleQuit(event.player)
    }
}