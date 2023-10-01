package com.emanuelvini.balloons.effect.impl

import com.emanuelvini.balloons.Balloons
import com.emanuelvini.balloons.effect.Effect
import com.emanuelvini.balloons.effect.EffectType
import org.bukkit.Material
import org.bukkit.entity.Player

class SuffocateEffect(plugin: Balloons, private val chance : Double, private val radius : Double, private val damage : Double) : Effect(EffectType.SUFFOCATE, plugin) {
    override fun execute(player: Player) {
        if (
            Math.random() < chance
        ) {
            player.getNearbyEntities(radius, radius, radius).forEach {
                if (it is Player) {
                    val blockFalling = player.world.spawnFallingBlock(
                        it.location.add(0.0, 10.0, 0.0),
                        Material.SAND,
                        0
                    )
                    it.damage(damage, blockFalling)
                }
            }
        }
    }
}