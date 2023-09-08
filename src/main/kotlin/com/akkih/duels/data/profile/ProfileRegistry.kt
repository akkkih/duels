package com.akkih.duels.data.profile

import com.akkih.duels.Duels
import com.akkih.duels.data.Config
import com.mongodb.client.model.Filters.eq
import gg.flyte.twilight.scheduler.repeat
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class ProfileRegistry(private val config: Config, private val duels: Duels) {
    private val profiles: MutableMap<UUID, Profile> = mutableMapOf()

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
        player.walkSpeed = 0F
        player.teleport(config.lobbyLocation)
        player.activePotionEffects.clear()
        player.inventory.clear()

        return duels.database.profiles.find(eq("uuid", player.uniqueId.toString())).first()?.run {
            duels.database.from(player.uniqueId)
        } ?: create(player)
    }

    fun handleQuit(player: Player) {
        if (player.world == config.arenaWorld) config.arenaWorld.players.forEach {
            it.sendMessage(
                Component.text(
                    "${player.name} has left the game, so you've been teleported to the lobby.",
                    NamedTextColor.RED
                )
            )
            it.teleport(config.lobbyLocation)
            it.inventory.clear()
        }

        val profile = findByPlayer(player)

        handleDeath(player)
        profiles.remove(player.uniqueId, profile)
        duels.database.save(profile)
    }

    fun handleKill(player: Player) {
        player.inventory.clear()
        player.sendMessage(Component.text("You've won!", NamedTextColor.GREEN))
        player.teleport(config.lobbyLocation)

        findByPlayer(player).apply {
            kills += 1
            wins += 1
            winstreak += 1
            duels.database.save(this)
        }
    }

    fun handleDeath(player: Player) {
        player.inventory.clear()
        if (player.isOnline) player.sendMessage(Component.text("You've lost. :(", NamedTextColor.RED))

        findByPlayer(player).apply {
            losses += 1
            deaths += 1
            winstreak = 0
            duels.database.save(this)
        }
    }

    private fun create(player: Player): Profile {
        val profile = duels.database.from(player.uniqueId)
        duels.database.save(profile)
        profiles[player.uniqueId] = profile
        return profile
    }

    fun saveAll() {
        profiles.forEach { duels.database.save(it.value) }
        println("Saved ${profiles.size} profile(s).")
    }
}