package com.akkih.duels.data.database

import com.akkih.duels.data.Config
import com.akkih.duels.data.profile.Profile
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.ReplaceOptions
import org.bson.Document
import java.util.*

class MongoDB(private val config: Config) : Database {
    lateinit var client: MongoClient
    lateinit var database: MongoDatabase
    lateinit var profiles: MongoCollection<Document>

    override fun create(connectionURI: String) {
        client = MongoClients.create(connectionURI)
        database = client.getDatabase(config.mongoDatabase)
        profiles = database.getCollection("profiles")
    }

    override fun save(profile: Profile) {
        val document = database.getCollection("profiles")
            .find(eq("uuid", profile.uuid.toString()))
            .first() ?: Document()

        document["uuid"] = profile.uuid.toString()
        document["wins"] = profile.wins
        document["losses"] = profile.losses
        document["kills"] = profile.kills
        document["deaths"] = profile.deaths
        document["winstreak"] = profile.winstreak

        database.getCollection("profiles").replaceOne(
            eq("uuid", profile.uuid.toString()),
            document,
            ReplaceOptions().upsert(true)
        )
    }

    override fun from(uuid: UUID): Profile {
        val document = database.getCollection("profiles")
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