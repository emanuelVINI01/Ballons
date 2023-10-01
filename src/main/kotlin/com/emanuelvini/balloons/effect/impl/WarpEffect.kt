package com.emanuelvini.balloons.effect.impl

import com.emanuelvini.balloons.Balloons
import com.emanuelvini.balloons.effect.Effect
import com.emanuelvini.balloons.effect.EffectType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import kotlin.math.cos
import kotlin.math.sin


class WarpEffect(plugin: Balloons, private val chance: Double) : Effect(EffectType.WARP, plugin) {

    override fun onEquipped(player: Player) {
        registerWithFilterListener<EntityDamageByEntityEvent>({
            val location = it.damager.location
            var nang: Float = location.yaw - 180
            if (nang < 0) nang += 360f
            val nX = cos(Math.toRadians(nang.toDouble()))
            val nZ = sin(Math.toRadians(nang.toDouble()))
            val l = location.clone()
            l.x = nX
            l.z = nZ
            player.teleport(l)
        }, {
            it.entity.name == player.name && Math.random() < chance
        }, player)
    }

    override fun execute(player: Player) {

    }
}