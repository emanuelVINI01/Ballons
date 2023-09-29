package com.emanuelvini.balloons.effect.impl

import com.emanuelvini.balloons.Balloons
import com.emanuelvini.balloons.effect.Effect
import com.emanuelvini.balloons.effect.EffectType
import org.bukkit.entity.Player

class FlyEffect(plugin: Balloons) : Effect(EffectType.FLY, plugin) {
    override fun onEquipped(player: Player) {
        player.allowFlight = true
    }

    override fun onUnequipped(player: Player) {
        player.allowFlight = false
    }
    override fun execute(player: Player) {

    }
}