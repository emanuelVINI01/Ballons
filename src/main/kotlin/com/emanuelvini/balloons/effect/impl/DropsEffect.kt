package com.emanuelvini.balloons.effect.impl

import com.emanuelvini.balloons.Balloons
import com.emanuelvini.balloons.effect.Effect
import com.emanuelvini.balloons.effect.EffectType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDeathEvent

class DropsEffect(
    plugin: Balloons,
    private val multiplier: Double,
    private val chance: Double?,
    private val message: String?
) : Effect(EffectType.DROPS, plugin) {
    override fun onEquipped(player: Player) {
        registerWithFilterListener<EntityDeathEvent>({ event ->
            event.drops.map {
                it.amount *= (1 + multiplier).toInt()
                return@map it
            }
            player.sendMessage(message)

        },
            {
                return@registerWithFilterListener it.entity.killer is Player && (chance == null || (
                        Math.random() < chance
                        )) && it.entity.killer.name == player.name
            }, player
        )
    }

    override fun execute(player: Player) {

    }
}