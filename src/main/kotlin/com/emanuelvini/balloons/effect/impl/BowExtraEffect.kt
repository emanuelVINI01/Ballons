package com.emanuelvini.balloons.effect.impl

import com.emanuelvini.balloons.Balloons
import com.emanuelvini.balloons.effect.Effect
import com.emanuelvini.balloons.effect.EffectType
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

class BowExtraEffect(
    plugin: Balloons,
    private val percent: Double,
    private val chance: Double?,
    private val message: String?,
    private val messageTarget: String?
) : Effect(EffectType.BOW_DAMAGE, plugin) {


    override fun onEquipped(player: Player) {
        registerListener<EntityDamageByEntityEvent>(player) { event ->
            if (event.cause == EntityDamageEvent.DamageCause.PROJECTILE) {
                if (event.damager is Arrow) {
                    val arrow = event.damager as Arrow
                    if (arrow.shooter is Player) {
                        val shooter = arrow.shooter as Player
                        if (shooter.name == player.name) {
                            if (
                                chance == null || (Math.random() < chance)
                            ) {
                                event.entity.sendMessage(
                                    messageTarget
                                )
                                shooter.sendMessage(message)
                                event.damage *= 1 + percent
                            }
                        }
                    }
                }
            }
        }
    }


    override fun execute(player: Player) {

    }
}