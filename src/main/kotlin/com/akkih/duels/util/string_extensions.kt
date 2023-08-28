package com.akkih.duels.util

import org.bukkit.Material

fun String.toMaterialOrNull() = runCatching { Material.matchMaterial(this) }.getOrNull()