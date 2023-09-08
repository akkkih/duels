package com.akkih.duels.util

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

fun createItemStack(material: String?, amount: Int = 1) =
    material?.let { name -> Material.matchMaterial(name)?.let { ItemStack(it, amount) } }