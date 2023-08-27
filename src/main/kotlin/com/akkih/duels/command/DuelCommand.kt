package com.akkih.duels.command

import com.akkih.duels.data.Kit
import com.akkih.duels.data.Yaml
import com.akkih.duels.data.profile.ProfileRegistry
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.Title.Times
import org.bukkit.entity.Player
import revxrsal.commands.annotation.*
import java.time.Duration
import java.util.concurrent.TimeUnit

class DuelCommand(private val profileRegistry: ProfileRegistry) {
    @Command("duel")
    @Cooldown(value = 1, unit = TimeUnit.MINUTES)
    fun onDuel(player: Player, @Named("name") @NotSender target: Player, @Named("kit") @Optional kit: Kit?) {
        if (!target.isOnline) {
            player.sendMessage(Component.text("Couldn't find this player!", NamedTextColor.RED))
            return
        }

        profileRegistry.invites.put(Pair(player.uniqueId, target.uniqueId), kit ?: Kit.valueOf(Yaml.Kits.DEFAULT_KIT))

        player.sendMessage(Component.text("You sent a Duel invite to ${target.name}", NamedTextColor.GREEN))
        target.sendMessage(
            Component.text("${player.name} sent you a Duel invite. ", NamedTextColor.GREEN).append(
                Component.text("[CLICK HERE TO ACCEPT]", NamedTextColor.YELLOW, TextDecoration.BOLD).clickEvent(
                    ClickEvent.runCommand("/accept ${player.name}")
                )
            ).hoverEvent(HoverEvent.showText(Component.text("Click to accept.", NamedTextColor.GRAY)))
        )
    }

    @Command("accept")
    @Cooldown(value = 1, unit = TimeUnit.MINUTES)
    fun onAcceptDuel(player: Player, @Named("name") @NotSender target: Player) {
        if (!target.isOnline) {
            player.sendMessage(Component.text("This player isn't online anymore!", NamedTextColor.RED))
            return
        }
        if (!profileRegistry.invites.asMap().containsKey(Pair(target.uniqueId, player.uniqueId))) {
            player.sendMessage(Component.text("You don't have a Duel invite from this player!", NamedTextColor.RED))
        }

        player.sendMessage(Component.text("You accepted the Duel invite.", NamedTextColor.GREEN))
        target.sendMessage(Component.text("${player.name} has accepted the Duel invite.", NamedTextColor.GREEN))

        player.teleport(Yaml.Arena.PLAYER_ONE)
        target.teleport(Yaml.Arena.PLAYER_TWO)

        Yaml.Kits.setKit(
            player,
            target,
            profileRegistry.invites.asMap().getValue(Pair(target.uniqueId, player.uniqueId))
        )

        player.showTitle(
            Title.title(
                Component.text("FIGHT!", NamedTextColor.RED, TextDecoration.BOLD), Component.empty(), Times.times(
                    Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1)
                )
            )
        )
        target.showTitle(
            Title.title(
                Component.text("FIGHT!", NamedTextColor.RED, TextDecoration.BOLD), Component.empty(), Times.times(
                    Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1)
                )
            )
        )
    }
}