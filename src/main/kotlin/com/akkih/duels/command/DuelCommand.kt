package com.akkih.duels.command

import com.akkih.duels.Duels
import com.akkih.duels.data.Config
import com.akkih.duels.data.Kit
import com.akkih.duels.data.duel.Duel
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor.RED
import org.bukkit.entity.Player
import revxrsal.commands.annotation.*
import java.util.concurrent.TimeUnit

class DuelCommand(private val config: Config) {
    @Command("duel")
    @Cooldown(value = 1, unit = TimeUnit.MINUTES)
    fun onDuel(player: Player, @Named("name") @NotSender other: Player, @Named("kit") @Optional kit: Kit?) {
        if (!other.isOnline) {
            player.sendMessage(Component.text("Couldn't find this player!", RED))
            return
        }
        if (player == other) {
            player.sendMessage(Component.text("You can't invite yourself to a Duel... are you okay?", RED))
            return
        }

        Duel(
            playerOne = player.uniqueId,
            playerTwo = other.uniqueId
        ).sendInvite(kit ?: Kit.valueOf(config.defaultKit))
    }

    @Command("accept")
    @Cooldown(value = 1, unit = TimeUnit.MINUTES)
    fun onAcceptDuel(player: Player, @Named("name") @NotSender other: Player) {
        if (!other.isOnline) {
            player.sendMessage(Component.text("This player isn't online anymore!", RED))
            return
        }
        if (!Duels.invites.asMap().containsKey(Pair(other.uniqueId, player.uniqueId))) {
            player.sendMessage(Component.text("You don't have a Duel invite from this player!", RED))
        }

        Duel(playerOne = other.uniqueId, playerTwo = player.uniqueId).apply {
            acceptInvite(kit ?: Kit.valueOf(config.defaultKit), config)
        }
    }
}