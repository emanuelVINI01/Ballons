package com.emanuelvini.balloons.effect.impl

import com.emanuelvini.balloons.Balloons
import com.emanuelvini.balloons.effect.Effect
import com.emanuelvini.balloons.effect.EffectType
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

class DamageEffect(plugin: Balloons, private val percent : Double, private val chance : Double?, private val message : String?, private val messageTarget : String?) : Effect(EffectType.DAMAGE, plugin) {


    override fun onEquipped(player: Player) {
        registerWithFilterListener<EntityDamageByEntityEvent>({ event ->
            event.damage *= percent + 1
        }, {
            return@registerWithFilterListener it.damager.name == player.name && (chance == null || (Math.random() < chance))
        }, player)
    }


    override fun execute(player: Player) {

    }


}