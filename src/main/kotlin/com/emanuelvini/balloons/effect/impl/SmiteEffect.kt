package com.emanuelvini.balloons.effect.impl

import com.emanuelvini.balloons.Balloons
import com.emanuelvini.balloons.effect.Effect
import com.emanuelvini.balloons.effect.EffectType
import org.bukkit.entity.Player

class SmiteEffect(
    plugin: Balloons,
    private val chance: Double,
    private val radius: Double,
    private val damage: Double,
    private val cooldown: Double,
    private val message: String?,
    private val messageTarget: String?
) : Effect(EffectType.SMITE, plugin) {

    private val cooldownPlayers = mutableListOf<String>()

    override fun execute(player: Player) {
        if (!cooldownPlayers.contains(player.name) && Math.random() < chance) {
            player.sendMessage(message)
            player.getNearbyEntities(radius, radius, radius).forEach {
                if (it is Player) {
                    player.world.strikeLightningEffect(it.location)
                    it.damage(damage)
                    it.sendMessage(messageTarget)
                }
            }
            cooldownPlayers.add(player.name)
            plugin.server.scheduler.scheduleSyncDelayedTask(plugin, {
                cooldownPlayers.remove(player.name)
            }, (cooldown * 20L).toLong())
        }
    }
}