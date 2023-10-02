package com.emanuelvini.balloons.effect.impl

import com.emanuelvini.balloons.Balloons
import com.emanuelvini.balloons.effect.Effect
import com.emanuelvini.balloons.effect.EffectType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerMoveEvent

class MidasTouch(plugin: Balloons, private val chance: Double?) : Effect(EffectType.MIDASTOUCH, plugin) {

    private val freeze = mutableListOf<String>()

    override fun setup() {
        registerListener<PlayerMoveEvent> {
            if (freeze.contains(it.player.name)) it.isCancelled = true
        }
    }

    override fun onEquipped(player: Player) {
        registerWithFilterListener<EntityDamageByEntityEvent>({
            freeze.add(it.entity.name)
            runAfter(2.5) {
                freeze.remove(it.entity.name)
            }
        }, {
            it.damager.name == player.name && (chance == null || Math.random() < chance)
        }, player)
    }

    override fun execute(player: Player) {

    }

}