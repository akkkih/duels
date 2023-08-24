package com.akkih.duels.command

import com.akkih.duels.data.profile.ProfileRegistry
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Default
import revxrsal.commands.annotation.Named
import revxrsal.commands.annotation.NotSender
import revxrsal.commands.annotation.Optional

class StatsCommand(private val profileRegistry: ProfileRegistry) {
    @Command("stats")
    fun onViewStats(player: Player, @Named("name") @Optional @NotSender target: Player?) {
        val target = target ?: player
        val targetProfile = profileRegistry.findByPlayer(target)

        player.sendMessage(
            Component.newline()
                .append(Component.text("${target.name}'s stats:"))
                .append(Component.newline())
                .append(Component.text("- Wins: ${targetProfile.wins}"))
                .append(Component.newline())
                .append(Component.text("- Losses: ${targetProfile.losses}"))
                .append(Component.newline())
                .append(Component.text("- Kills: ${targetProfile.kills}"))
                .append(Component.newline())
                .append(Component.text("- Deaths: ${targetProfile.deaths}"))
                .append(Component.newline())
                .append(Component.text("- Win Streak: ${targetProfile.winstreak}"))
                .append(Component.newline())
                .color(NamedTextColor.GREEN)
                .decoration(TextDecoration.ITALIC, false)
        )
    }
}