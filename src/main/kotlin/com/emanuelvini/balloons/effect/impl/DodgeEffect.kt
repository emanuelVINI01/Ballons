package com.emanuelvini.balloons.effect.impl

import com.emanuelvini.balloons.Balloons
import com.emanuelvini.balloons.effect.Effect
import com.emanuelvini.balloons.effect.EffectType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent


class DodgeEffect(plugin: Balloons, private val chance : Double, private val cooldown : Double, private val message : String?) : Effect(EffectType.DODGE, plugin, ) {

    private val cooldownPlayers = mutableListOf<String>()

    override fun onEquipped(player: Player) {
        registerWithFilterListener<EntityDamageByEntityEvent>({ event ->
            cooldownPlayers.add(player.name)
            plugin.server.scheduler.scheduleAsyncDelayedTask(plugin, {
                cooldownPlayers.remove(player.name)
            }, (20L * cooldown).toLong())
            event.isCancelled = true
            player.sendMessage(message)
        }, {
           !cooldownPlayers.contains(player.name) && it.entity.name == player.name
        }, player)
    }



    override fun execute(player: Player) {

    }
}