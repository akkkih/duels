package com.akkih.duels.data.duel

import com.akkih.duels.Duels
import com.akkih.duels.data.Config
import com.akkih.duels.data.Kit
import com.akkih.duels.util.setKit
import gg.flyte.twilight.scheduler.repeat
import gg.flyte.twilight.time.TimeUnit
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor.*
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import java.time.Duration
import java.util.*

data class Duel(
    val uuid: UUID = UUID.randomUUID(),
    val playerOne: UUID,
    val playerTwo: UUID
) {
    fun sendInvite(kit: Kit) {
        val player = Bukkit.getPlayer(playerOne)!!
        val other = Bukkit.getPlayer(playerTwo)!!

        Duels.invites.put(Pair(player.uniqueId, other.uniqueId), kit)

        player.sendMessage(Component.text("You sent a Duel invite to ${other.name}.", GREEN))
        other.sendMessage(
            Component.newline()
                .append(Component.text("${player.name} sent you a Duel invite.", GREEN))
                .append(Component.newline())
                .append(
                    Component.text("[CLICK HERE TO ACCEPT]", YELLOW, TextDecoration.BOLD)
                        .clickEvent(ClickEvent.runCommand("/accept ${player.name}"))
                        .hoverEvent(HoverEvent.showText(Component.text("Click to accept the Duel invite.", GRAY)))
                )
                .append(Component.newline())
        )
    }

    fun acceptInvite(kit: Kit, config: Config) {
        val player = Bukkit.getPlayer(playerTwo)!!
        val other = Bukkit.getPlayer(playerOne)!!

        player.sendMessage(Component.text("You accepted the Duel invite.", GREEN))
        other.sendMessage(Component.text("${player.name} has accepted the Duel invite.", GREEN))

        player.teleport(config.firstPlayerLocation)
        other.teleport(config.secondPlayerLocation)

        player.gameMode = GameMode.SURVIVAL
        other.gameMode = GameMode.SURVIVAL

        val kit = Duels.invites.asMap().getValue(Pair(other.uniqueId, player.uniqueId))

        player.setKit(config, kit)
        other.setKit(config, kit)

        object : BukkitRunnable() {
            var i = 5

            override fun run() {
                if (i == 0) {
                    player.showTitle(Title.title(
                        Component.text("FIGHT!", RED, TextDecoration.BOLD), Component.empty(), Title.Times.times(
                            Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1)
                        )
                    ))

                    other.showTitle(Title.title(
                        Component.text("FIGHT!", RED, TextDecoration.BOLD), Component.empty(), Title.Times.times(
                            Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1)
                        )
                    ))

                    player.walkSpeed = 0F
                    player.activePotionEffects.clear()
                    other.walkSpeed = 0F
                    other.activePotionEffects.clear()

                    cancel()
                }

                player.walkSpeed = -1F
                player.addPotionEffect(PotionEffect(PotionEffectType.JUMP, 999, 0))
                other.walkSpeed = -1F
                other.addPotionEffect(PotionEffect(PotionEffectType.JUMP, 999, 0))

                player.showTitle(Title.title(
                    Component.text(i, GOLD, TextDecoration.BOLD), Component.text("seconds until start", GRAY)
                ))

                other.showTitle(Title.title(
                    Component.text(i, GOLD, TextDecoration.BOLD), Component.text("seconds until start", GRAY)
                ))

                i -= 1
            }
        }.runTaskTimer(JavaPlugin.getPlugin(Duels::class.java), 20, 20)

        Duels.invites.invalidate(Pair(other.uniqueId, player.uniqueId))
    }

    val kit = Duels.invites.asMap()[Pair(playerOne, playerTwo)]
}
