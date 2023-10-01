package com.emanuelvini.balloons.effect.impl

import com.emanuelvini.balloons.Balloons
import com.emanuelvini.balloons.effect.Effect
import com.emanuelvini.balloons.effect.EffectType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent

class ReductionEffect(
    plugin: Balloons,
    private val percent: Double,
    private val chance: Double?,
    private val message: String?,
    private val messageTarget: String?
) : Effect(EffectType.REDUCTION, plugin) {

    override fun onEquipped(player: Player) {
        registerWithFilterListener<EntityDamageEvent>({
            it.damage *= percent
        }, { it.entity.name == player.name && (chance == null || (Math.random() < chance)) }, player)
    }

    override fun execute(player: Player) {

    }
}