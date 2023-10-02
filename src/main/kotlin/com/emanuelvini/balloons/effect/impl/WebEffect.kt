package com.emanuelvini.balloons.effect.impl

import com.emanuelvini.balloons.Balloons
import com.emanuelvini.balloons.effect.Effect
import com.emanuelvini.balloons.effect.EffectType
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import kotlin.random.Random

class WebEffect(plugin: Balloons, private val chance: Double, private val radius: Int, private val duration: Double) :
    Effect(EffectType.WEB, plugin) {


    override fun execute(player: Player) {
        if (Math.random() < chance) {
            val l = player.location
            val blocks = mutableListOf<Location>()
            for (i in 0..radius * radius) {
                val ll = l.clone().add(
                    (
                            Random.nextInt(
                                radius
                            ) * if (Math.random() < 0.5) 1 else -1).toDouble(),
                    0.0,
                    (
                            Random.nextInt(radius) * if (Math.random() < 0.5) 1 else -1
                            ).toDouble()
                )
                blocks.add(
                    ll
                )
            }
            blocks.forEach {
                if (it.block.type != Material.AIR)
                    it.block.type = Material.WEB
            }
            plugin.server.scheduler.scheduleSyncDelayedTask(plugin, {
                blocks.forEach {
                    it.block.type = Material.AIR
                }
            }, (duration * 20L).toLong())
        }
    }

}