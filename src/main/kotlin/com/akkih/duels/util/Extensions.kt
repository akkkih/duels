package com.akkih.duels.util

import com.akkih.duels.data.Config
import com.akkih.duels.data.Kit
import org.bukkit.entity.Player

fun Player.setKit(config: Config, kit: Kit) {
    inventory.clear()
    config.kits.getConfigurationSection("kits.${kit.name.lowercase()}.armor-content")?.apply {
        inventory.helmet = createItemStack(getString("helmet"))
        inventory.chestplate = createItemStack(getString("chestplate"))
        inventory.leggings = createItemStack(getString("leggings"))
        inventory.boots = createItemStack(getString("boots"))
    }
    config.kits.getConfigurationSection("kits.${kit.name.lowercase()}.inventory-content")?.apply {
        for (i in 0..<35) {
            inventory.setItem(i, createItemStack(getString("$i.material"), getInt("$i.amount")))
        }
    }
}