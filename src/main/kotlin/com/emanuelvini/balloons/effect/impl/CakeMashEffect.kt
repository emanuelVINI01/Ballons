package com.emanuelvini.balloons.effect.impl

import com.emanuelvini.balloons.Balloons
import com.emanuelvini.balloons.effect.Effect
import com.emanuelvini.balloons.effect.EffectType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

class CakeMashEffect(plugin: Balloons, private val chance: Double?) : Effect(EffectType.CAKEMASH, plugin) {

    override fun onEquipped(player: Player) {
        registerWithFilterListener<EntityDamageByEntityEvent>({}, {
            it.entity.name == player.name && (chance == null || Math.random() < chance)
        }, player)
    }

    override fun execute(player: Player) {

    }

}