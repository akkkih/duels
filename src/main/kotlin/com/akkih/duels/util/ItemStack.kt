package com.akkih.duels.util

import org.bukkit.inventory.ItemStack

fun createItemStack(material: String?, amount: Int = 1): ItemStack? {
    val material = material?.toMaterialOrNull() ?: return null
    return ItemStack(material, amount)
}