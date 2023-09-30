package com.emanuelvini.balloons.effect.impl

import com.emanuelvini.balloons.Balloons
import com.emanuelvini.balloons.effect.Effect
import com.emanuelvini.balloons.effect.EffectType
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerItemDamageEvent

class DurabilityEffect(
    plugin: Balloons,
    private val chance: Double, private val cooldown: Double?, private val message: String?
) : Effect(EffectType.DURABILITY, plugin) {

    private val cooldownPlayers = mutableListOf<String>()

    override fun onEquipped(player: Player) {
        registerWithFilterListener<PlayerItemDamageEvent>({ event ->
            if (cooldown != null) {
                cooldownPlayers.add(player.name)
                plugin.server.scheduler.scheduleAsyncDelayedTask(plugin, {
                    cooldownPlayers.remove(player.name)
                }, (20L * cooldown).toLong())
            }
            event.isCancelled = true
            player.sendMessage(message)

        }, {
            return@registerWithFilterListener !cooldownPlayers.contains(player.name)
                    && it.player.name == player.name
                    && Math.random() < chance
        }, player)
    }

    override fun execute(player: Player) {

    }
}