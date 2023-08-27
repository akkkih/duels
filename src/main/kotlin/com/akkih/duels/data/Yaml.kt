package com.akkih.duels.data

import com.akkih.duels.Duels
import com.akkih.duels.util.createItemStack
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.WorldCreator
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File

object Yaml {
    private lateinit var config: FileConfiguration
    private lateinit var kits: YamlConfiguration
    private lateinit var arena: YamlConfiguration

    fun init(plugin: Duels) {
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

    object Kits {
        val DEFAULT_KIT by lazy { kits.getString("default-kit")!! }

        fun setKit(player1: Player, player2: Player, kit: Kit) {
            setItems(player1, kit)
            setItems(player2, kit)
        }

        private fun setItems(player: Player, kit: Kit) {
            player.inventory.clear()
            kits.getConfigurationSection("kits.${kit.name.lowercase()}.armor-content")?.apply {
                player.inventory.helmet = createItemStack(getString("helmet.material"), getInt("helmet.amount"))
                player.inventory.chestplate =
                    createItemStack(getString("chestplate.material"), getInt("chestplate.amount"))
                player.inventory.leggings = createItemStack(getString("leggings.material"), getInt("leggings.amount"))
                player.inventory.boots = createItemStack(getString("boots.material"), getInt("boots.amount"))
            }
            kits.getConfigurationSection("kits.${kit.name.lowercase()}.inventory-content")?.apply {
                for (i in 0..<35) {
                    player.inventory.setItem(i, createItemStack(getString("$i.material"), getInt("$i.amount")))
                }
            }
        }
    }

    object Arena {
        val ARENA_WORLD by lazy {
            val worldName = arena.getString("arena.world")!!
            Bukkit.createWorld(WorldCreator(worldName))
            Bukkit.getWorld(worldName)
        }
        val LOBBY_WORLD by lazy { Bukkit.getWorld(arena.getString("lobby.world")!!) }

        val PLAYER_ONE by lazy {
            Location(
                ARENA_WORLD,
                arena.getDouble("arena.player-one.x"),
                arena.getDouble("arena.player-one.y"),
                arena.getDouble("arena.player-one.z"),
                arena.getDouble("arena.player-one.yaw").toFloat(),
                arena.getDouble("arena.player-one.pitch").toFloat()
            )
        }

        val PLAYER_TWO by lazy {
            Location(
                ARENA_WORLD,
                arena.getDouble("arena.player-two.x"),
                arena.getDouble("arena.player-two.y"),
                arena.getDouble("arena.player-two.z"),
                arena.getDouble("arena.player-two.yaw").toFloat(),
                arena.getDouble("arena.player-two.pitch").toFloat()
            )
        }

        val LOBBY by lazy {
            Location(
                LOBBY_WORLD,
                arena.getDouble("lobby.x"),
                arena.getDouble("lobby.y"),
                arena.getDouble("lobby.z"),
                arena.getDouble("lobby.yaw").toFloat(),
                arena.getDouble("lobby.pitch").toFloat(),
            )
        }
    }

    val MONGO_URI by lazy { config.getString("mongo-uri")!! }
    val MONGO_DATABASE by lazy { config.getString("mongo-database")!! }
}