package com.akkih.duels.command

import com.akkih.duels.data.profile.ProfileRegistry
import gg.flyte.twilight.string.smallText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Cooldown
import revxrsal.commands.annotation.Named
import revxrsal.commands.annotation.Optional
import java.util.concurrent.TimeUnit

class StatsCommand(private val profileRegistry: ProfileRegistry) {
    @Command("stats")
    @Cooldown(value = 3, unit = TimeUnit.SECONDS)
    fun onViewStats(player: Player, @Named("name") @Optional target: String?) {
        val profile = profileRegistry.findByName(target ?: player.name) ?: run {
            player.sendMessage(Component.text("Couldn't find this player!", NamedTextColor.RED))
            return
        }

        val message = Component.empty()
            .append(Component.newline())
            .append(
                Component.text(
                    "${profile.player?.name}'s Stats",
                    NamedTextColor.GOLD,
                    TextDecoration.BOLD
                )
            )
            .append(Component.newline())
            .append(Component.text(" - "))
            .append(Component.text("Kills: ".smallText(), NamedTextColor.GREEN))
            .append(Component.text(profile.kills, NamedTextColor.RED))
            .append(Component.newline())
            .append(Component.text(" - "))
            .append(Component.text("Deaths: ".smallText(), NamedTextColor.GREEN))
            .append(Component.text(profile.deaths, NamedTextColor.RED))
            .append(Component.newline())
            .append(Component.text(" - "))
            .append(Component.text("Wins: ".smallText(), NamedTextColor.GREEN))
            .append(Component.text(profile.wins, NamedTextColor.RED))
            .append(Component.newline())
            .append(Component.text(" - "))
            .append(Component.text("Losses: ".smallText(), NamedTextColor.GREEN))
            .append(Component.text(profile.losses, NamedTextColor.RED))
            .append(Component.newline())
            .append(Component.text(" - "))
            .append(Component.text("Win Streak: ".smallText(), NamedTextColor.GREEN))
            .append(Component.text(profile.winstreak, NamedTextColor.RED))
            .append(Component.newline())

        player.sendMessage(message)
    }
}