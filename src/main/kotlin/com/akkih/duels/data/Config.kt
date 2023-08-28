package com.akkih.duels.data

import com.akkih.duels.Duels
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.WorldCreator
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class Config(plugin: Duels) {
    private val config: FileConfiguration
    val kits: YamlConfiguration
    private val arena: YamlConfiguration

    init {
        // config.yml logic
        plugin.saveDefaultConfig()
        config = plugin.config
        config.options().copyDefaults(true)
        plugin.saveConfig()

        // kits.yml logic
        plugin.saveResource("kits.yml", false)
        kits = YamlConfiguration.loadConfiguration(File(plugin.dataFolder, "kits.yml"))

        // arena.yml logic
        plugin.saveResource("arena.yml", false)
        arena = YamlConfiguration.loadConfiguration(File(plugin.dataFolder, "arena.yml"))
    }

    val arenaWorld = run {
        val worldName = arena.getString("arena.world") ?: throw IllegalArgumentException("\"arena.world\" in arena.yml is not defined")
        Bukkit.createWorld(WorldCreator(worldName))
        Bukkit.getWorld(worldName)
    }

    private val lobbyWorld = run {
        val worldName = arena.getString("lobby.world") ?: throw IllegalArgumentException("\"lobby.world\" in arena.yml is not defined")
        Bukkit.createWorld(WorldCreator(worldName))
        Bukkit.getWorld(worldName)
    }

    val firstPlayerLocation = Location(
        arenaWorld,
        arena.getDouble("arena.player-one.x"),
        arena.getDouble("arena.player-one.y"),
        arena.getDouble("arena.player-one.z"),
        arena.getDouble("arena.player-one.yaw").toFloat(),
        arena.getDouble("arena.player-one.pitch").toFloat()
    )

    val secondPlayerLocation = Location(
        arenaWorld,
        arena.getDouble("arena.player-two.x"),
        arena.getDouble("arena.player-two.y"),
        arena.getDouble("arena.player-two.z"),
        arena.getDouble("arena.player-two.yaw").toFloat(),
        arena.getDouble("arena.player-two.pitch").toFloat()
    )

    val lobbyLocation = Location(
        lobbyWorld,
        arena.getDouble("lobby.x"),
        arena.getDouble("lobby.y"),
        arena.getDouble("lobby.z"),
        arena.getDouble("lobby.yaw").toFloat(),
        arena.getDouble("lobby.pitch").toFloat(),
    )

    val defaultKit = kits.getString("default-kit")?.uppercase() ?: throw IllegalArgumentException("\"default-kit\" in kits.yml is not defined")

    val mongoURI = config.getString("mongo-uri") ?: throw IllegalArgumentException("\"mongo-uri\" in config.yml is not defined")
    val mongoDatabase = config.getString("mongo-database") ?: throw IllegalArgumentException("\"mongo-uri\" in config.yml is not defined")
}