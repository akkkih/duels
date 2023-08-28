package com.akkih.duels

import com.akkih.duels.command.DuelCommand
import com.akkih.duels.command.StatsCommand
import com.akkih.duels.data.Config
import com.akkih.duels.data.profile.ProfileListener
import com.akkih.duels.data.profile.ProfileRegistry
import gg.flyte.twilight.twilight
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.plugin.java.JavaPlugin
import revxrsal.commands.bukkit.BukkitCommandHandler

class Duels : JavaPlugin() {
    private lateinit var profileRegistry: ProfileRegistry

    override fun onEnable() {
        initialize()
    }

    override fun onDisable() {
        profileRegistry.saveAll()
    }

    private fun initialize() {
        val config = Config(this)

        twilight(this) {
            mongo {
                uri = config.mongoURI
                database = config.mongoDatabase
            }
        }

        profileRegistry = ProfileRegistry(config)
        val commandHandler = BukkitCommandHandler.create(this)

        commandHandler.register(StatsCommand(profileRegistry), DuelCommand(profileRegistry, config))
        commandHandler.registerBrigadier()

        Bukkit.getPluginManager().registerEvents(ProfileListener(profileRegistry, config), this)
    }
}