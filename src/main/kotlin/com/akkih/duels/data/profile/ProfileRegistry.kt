package com.akkih.duels.data.profile

import com.akkih.duels.data.Database
import com.akkih.duels.data.Kit
import com.akkih.duels.data.Yaml
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.mongodb.client.model.Filters.eq
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.TimeUnit

class ProfileRegistry {
    private val profiles: MutableMap<UUID, Profile> = mutableMapOf()
    val invites: Cache<Pair<UUID, UUID>, Kit> = CacheBuilder.newBuilder()
        .expireAfterWrite(5, TimeUnit.MINUTES)
        .build()
    private val collection = Database.PROFILES

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
        player.teleport(Yaml.Arena.LOBBY)

        return collection.find(eq("uuid", player.uniqueId.toString())).first()?.run {
            Profile.from(player.uniqueId)
        } ?: create(player)
    }

    fun handleQuit(player: Player) {
        findByPlayer(player).apply {
            save()
            profiles.remove(player.uniqueId, this)
        }
    }

    fun handleKill(player: Player) {
        player.sendMessage(Component.text("You've won!", NamedTextColor.GREEN))
        player.teleport(Yaml.Arena.LOBBY)

        findByPlayer(player).apply {
            kills += 1
            winstreak += 1
            save()
        }
    }

    fun handleDeath(player: Player) {
        player.sendMessage(Component.text("You've lost. :(", NamedTextColor.RED))
        player.teleport(Yaml.Arena.LOBBY)

        findByPlayer(player).apply {
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
}