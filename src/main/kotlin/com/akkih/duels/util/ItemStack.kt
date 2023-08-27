package com.akkih.duels.util

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

fun createItemStack(material: String?, amount: Int?): ItemStack? {
    if (material.isNullOrEmpty() || amount == null) return null
    return ItemStack(Material.getMaterial(material)!!, amount)
}