package com.akkih.duels

import com.akkih.duels.command.DuelCommand
import com.akkih.duels.command.StatsCommand
import com.akkih.duels.data.Config
import com.akkih.duels.data.Kit
import com.akkih.duels.data.database.MongoDB
import com.akkih.duels.data.profile.ProfileListener
import com.akkih.duels.data.profile.ProfileRegistry
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import gg.flyte.twilight.twilight
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import revxrsal.commands.bukkit.BukkitCommandHandler
import java.util.*

class Duels : JavaPlugin() {
    private lateinit var profileRegistry: ProfileRegistry
    lateinit var database: MongoDB

    override fun onEnable() {
        val config = Config(this)

        twilight(this) {}

        MongoDB(config).apply {
            create(config.mongoURI)
            this@Duels.database = this
        }

        profileRegistry = ProfileRegistry(config, this)

        BukkitCommandHandler.create(this).apply {
            register(StatsCommand(profileRegistry), DuelCommand(config))
            registerBrigadier()
        }

        Bukkit.getPluginManager().registerEvents(ProfileListener(profileRegistry, config), this)

        invites = CacheBuilder.newBuilder()
            .expireAfterWrite(5, java.util.concurrent.TimeUnit.MINUTES)
            .build()
    }

    override fun onDisable() {
        profileRegistry.saveAll()
    }

    companion object {
        lateinit var invites: Cache<Pair<UUID, UUID>, Kit>
    }
}