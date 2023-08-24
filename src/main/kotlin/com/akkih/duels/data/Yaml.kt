package com.akkih.duels.data

import com.akkih.duels.Duels
import com.akkih.duels.extension.createItemStack
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

        fun setKit(player: Player, kit: Kit) {
            val kit = kit.name.lowercase()

            player.inventory.clear()

            kits.getConfigurationSection("kits.$kit.armor-content")?.apply {
                player.inventory.helmet = createItemStack(getString("helmet.material"), getInt("helmet.amount"))
                player.inventory.chestplate = createItemStack(getString("chestplate.material"), getInt("chestplate.amount"))
                player.inventory.leggings = createItemStack(getString("leggings.material"), getInt("leggings.amount"))
                player.inventory.boots = createItemStack(getString("boots.material"), getInt("boots.amount"))
            }

            // Logic for setting up player inventory
        }
    }

    object Arena {
        val WORLD by lazy { arena.getString("world") }

        val PLAYER_ONE_X by lazy { arena.getDouble("spawn.player-one.x") }
        val PLAYER_ONE_Y by lazy { arena.getDouble("spawn.player-one.y") }
        val PLAYER_ONE_Z by lazy { arena.getDouble("spawn.player-one.z") }

        val PLAYER_TWO_X by lazy { arena.getDouble("spawn.player-two.x") }
        val PLAYER_TWO_Y by lazy { arena.getDouble("spawn.player-two.y") }
        val PLAYER_TWO_Z by lazy { arena.getDouble("spawn.player-two.z") }
    }

    val MONGO_URI by lazy { config.getString("mongo-uri")!! }
    val MONGO_DATABASE by lazy { config.getString("mongo-database")!! }
}