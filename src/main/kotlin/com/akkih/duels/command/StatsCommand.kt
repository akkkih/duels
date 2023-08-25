package com.akkih.duels.command

import com.akkih.duels.data.profile.ProfileRegistry
import com.akkih.duels.extension.color
import gg.flyte.twilight.string.smallText
import gg.flyte.twilight.string.translate
import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Default
import revxrsal.commands.annotation.Named
import revxrsal.commands.annotation.Optional
import revxrsal.commands.ktx.returnWithMessage

class StatsCommand(private val profileRegistry: ProfileRegistry) {
    @Command("stats")
    fun onViewStats(player: Player, @Named("name") @Optional target: String?) {
        val targetProfile = profileRegistry.findByName(target ?: player.name)
        val target = targetProfile?.player

        player.sendMessage(
            " ",
            "§a§l${target?.name}'s Stats".smallText(),
            " - §aKills: ${targetProfile?.kills}".smallText(),
            " - §aDeaths: ${targetProfile?.deaths}".smallText(),
            " - §aWins: ${targetProfile?.wins}".smallText(),
            " - §aLosses: ${targetProfile?.losses}".smallText(),
            " - §aCurrent WS: ${targetProfile?.winstreak}".smallText(),
            " "
        )
    }
}