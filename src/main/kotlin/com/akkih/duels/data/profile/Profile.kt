package com.akkih.duels.data.profile

import com.akkih.duels.data.Database
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.ReplaceOptions
import org.bson.Document
import org.bukkit.Bukkit
import java.util.*

data class Profile(
    val uuid: UUID,
    var wins: Int,
    var losses: Int,
    var kills: Int,
    var deaths: Int,
    var winstreak: Int,
) {
    val player = Bukkit.getOfflinePlayer(uuid).player

    fun save() {
        val document = Database.PROFILES
            .find(eq("uuid", uuid.toString()))
            .first() ?: Document()

        document["uuid"] = uuid.toString()
        document["wins"] = wins
        document["losses"] = losses
        document["kills"] = kills
        document["deaths"] = deaths
        document["winstreak"] = winstreak

        Database.PROFILES.replaceOne(
            eq("uuid", uuid.toString()),
            document,
            ReplaceOptions().upsert(true)
        )
    }

    companion object {
        fun from(uuid: UUID): Profile {
            val document = Database.PROFILES
                .find(eq("uuid", uuid.toString()))
                .first() ?: return Profile(uuid, 0, 0, 0, 0, 0)

            return Profile(
                uuid,
                document.getInteger("wins"),
                document.getInteger("losses"),
                document.getInteger("kills"),
                document.getInteger("deaths"),
                document.getInteger("winstreak")
            )
        }
    }
}
