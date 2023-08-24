package com.akkih.duels.extension

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

fun createItemStack(material: String?, amount: Int): ItemStack {
    return ItemStack(Material.getMaterial(material!!) ?: Material.AIR, amount)
}