package com.akkih.duels.data.profile

import com.akkih.duels.data.Database
import com.akkih.duels.data.Kit
import com.akkih.duels.data.Config
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.mongodb.client.model.Filters.eq
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import gg.flyte.twilight.scheduler.repeat

class ProfileRegistry(private val config: Config) {
    private val profiles: MutableMap<UUID, Profile> = mutableMapOf()
    val invites: Cache<Pair<UUID, UUID>, Kit> = CacheBuilder.newBuilder()
        .expireAfterWrite(5, java.util.concurrent.TimeUnit.MINUTES)
        .build()
    private val collection = Database.PROFILES

    init {
        repeat(5, gg.flyte.twilight.time.TimeUnit.MINUTES, true) { saveAll() }
    }

    fun findByUUID(uuid: UUID): Profile? {
        return profiles[uuid]
    }

    fun findByName(name: String): Profile? {
        return Bukkit.getOfflinePlayer(name).player?.let { findByPlayer(it) }
    }

    fun findByPlayer(player: Player): Profile {
        return findByUUID(player.uniqueId) ?: create(player)
    }

    fun handleJoin(player: Player): Profile {
        player.teleport(config.lobbyLocation)
        player.inventory.clear()

        return collection.find(eq("uuid", player.uniqueId.toString())).first()?.run {
            Profile.from(player.uniqueId)
        } ?: create(player)
    }

    fun handleQuit(player: Player) {
        if (player.world == config.arenaWorld) config.arenaWorld.players.forEach {
            it.sendMessage(Component.text("${player.name} has left the game, so you've been teleported to the lobby.", NamedTextColor.RED))
            it.teleport(config.lobbyLocation)
            it.inventory.clear()
        }

        findByPlayer(player).apply {
            save()
            profiles.remove(player.uniqueId, this)
        }
    }

    fun handleKill(player: Player) {
        player.inventory.clear()
        player.sendMessage(Component.text("You've won!", NamedTextColor.GREEN))
        player.teleport(config.lobbyLocation)

        findByPlayer(player).apply {
            kills += 1
            wins += 1
            winstreak += 1
            save()
        }
    }

    fun handleDeath(player: Player) {
        player.inventory.clear()
        player.sendMessage(Component.text("You've lost. :(", NamedTextColor.RED))

        findByPlayer(player).apply {
            losses += 1
            deaths += 1
            winstreak = 0
            save()
        }
    }

    private fun create(player: Player): Profile {
        val profile = Profile.from(player.uniqueId)
        profile.save()
        profiles[player.uniqueId] = profile
        return profile
    }

    fun saveAll() {
        profiles.forEach { it.value.save() }
        println("Saved ${profiles.size} profile(s).")
    }
}