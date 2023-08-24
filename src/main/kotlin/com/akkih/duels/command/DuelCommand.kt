package com.akkih.duels.command

import com.akkih.duels.data.Kit
import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Named
import revxrsal.commands.annotation.NotSender
import revxrsal.commands.annotation.Optional

class DuelCommand {
    @Command("duel")
    fun onDuel(player: Player, @Named("name") @NotSender target: Player, @Named("kit") @Optional kit: Kit) {

    }

    @Command("accept")
    fun onAcceptDuel(player: Player, @Named("name") @NotSender target: Player) {

    }
}