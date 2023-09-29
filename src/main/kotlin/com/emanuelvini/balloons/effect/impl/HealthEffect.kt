package com.emanuelvini.balloons.effect.impl

import com.emanuelvini.balloons.Balloons
import com.emanuelvini.balloons.effect.Effect
import com.emanuelvini.balloons.effect.EffectType
import org.bukkit.entity.Player

class HealthEffect(plugin: Balloons, private val amount : Double) : Effect(
    EffectType.HEALTH, plugin
) {

    override fun onEquipped(player: Player) {
        player.maxHealth += amount
    }

    override fun onUnequipped(player: Player) {
        player.maxHealth = 20.0
    }

    override fun execute(player: Player) {}
}