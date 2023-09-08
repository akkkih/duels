package com.akkih.duels.data.profile

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

data class Profile(
    val uuid: UUID,
    var wins: Int,
    var losses: Int,
    var kills: Int,
    var deaths: Int,
    var winstreak: Int,
) {
    val player: Player?
        get() = Bukkit.getPlayer(uuid)
}
