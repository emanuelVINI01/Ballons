package com.emanuelvini.balloons.effect.impl

import com.emanuelvini.balloons.Balloons
import com.emanuelvini.balloons.effect.Effect
import com.emanuelvini.balloons.effect.EffectType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDeathEvent

class ExpEffect(
    plugin: Balloons,
    private val multiplier: Double,
    private val chance: Double?,
    private val message: String?
) : Effect(EffectType.EXP, plugin) {

    override fun execute(player: Player) {}

    override fun onEquipped(player: Player) {
        registerWithFilterListener<EntityDeathEvent>({ event ->
            event.droppedExp *= (multiplier + 1).toInt()
            player.sendMessage(message)
        }, {
            return@registerWithFilterListener it.entity.killer != null && (it.entity.killer.name == player.name) && (chance == null || (Math.random() < chance))
        }, player)
    }


}