package com.akkih.duels

import com.akkih.duels.command.StatsCommand
import com.akkih.duels.data.Yaml
import com.akkih.duels.data.profile.ProfileListener
import com.akkih.duels.data.profile.ProfileRegistry
import gg.flyte.twilight.twilight
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import revxrsal.commands.bukkit.BukkitCommandHandler

class Duels : JavaPlugin() {
    override fun onEnable() {
        initialize()
    }

    private fun initialize() {
        Yaml.init(this)

        twilight(this) {
            mongo {
                uri = Yaml.MONGO_URI
                database = Yaml.MONGO_DATABASE
            }
        }

        val profileRegistry = ProfileRegistry()
        val commandHandler = BukkitCommandHandler.create(this)

        commandHandler.register(StatsCommand(profileRegistry))
        commandHandler.registerBrigadier()

        Bukkit.getPluginManager().registerEvents(ProfileListener(profileRegistry), this)
    }
}