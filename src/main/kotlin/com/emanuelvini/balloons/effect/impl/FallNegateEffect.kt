package com.emanuelvini.balloons.effect.impl

import com.emanuelvini.balloons.Balloons
import com.emanuelvini.balloons.effect.Effect
import com.emanuelvini.balloons.effect.EffectType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent

class FallNegateEffect(plugin: Balloons, private val chance: Double?, private val message: String?) :
    Effect(EffectType.FALL_DAMAGE, plugin) {
    override fun onEquipped(player: Player) {
        registerWithFilterListener<EntityDamageEvent>({ event ->
            event.isCancelled = true
            player.sendMessage(message)
        }, {
            return@registerWithFilterListener it.entity.name == player.name
                    && it.cause == EntityDamageEvent.DamageCause.FALL
                    && (chance == null || (Math.random() < chance))
        }, player)
    }

    override fun execute(player: Player) {

    }
}