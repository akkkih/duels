package com.akkih.duels.data.profile

import com.akkih.duels.data.Database
import com.mongodb.client.model.Filters.*
import org.bukkit.entity.Player
import java.util.UUID

class ProfileRegistry {
    private val profiles: MutableMap<UUID, Profile> = mutableMapOf()
    private val collection = Database.PROFILES

    fun findByUUID(uuid: UUID): Profile? {
        return profiles[uuid]
    }

    fun findByPlayer(player: Player): Profile {
        return findByUUID(player.uniqueId) ?: create(player)
    }

    fun handleJoin(player: Player): Profile {
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

    private fun create(player: Player): Profile {
        val profile = Profile.from(player.uniqueId)
        profile.save()
        profiles[player.uniqueId] = profile
        return profile
    }
}