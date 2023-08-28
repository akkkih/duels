package com.akkih.duels.data.profile

import com.akkih.duels.data.Config
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerRespawnEvent

class ProfileListener(private val profileRegistry: ProfileRegistry, private val config: Config) : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        profileRegistry.handleJoin(event.player)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        profileRegistry.handleQuit(event.player)
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        event.drops.clear()
        event.droppedExp = 0
        event.deathMessage(Component.empty())

        event.entity.killer?.let { profileRegistry.handleKill(it) }
        profileRegistry.handleDeath(event.entity)
    }

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        event.respawnLocation = config.lobbyLocation
    }
}