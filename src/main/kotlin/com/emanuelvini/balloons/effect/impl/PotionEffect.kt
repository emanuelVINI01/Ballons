package com.emanuelvini.balloons.effect.impl

import com.emanuelvini.balloons.Balloons
import com.emanuelvini.balloons.effect.Effect
import com.emanuelvini.balloons.effect.EffectType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType


class PotionEffect(
    plugin: Balloons,
    private val potion: PotionEffectType,
    private val level: Int,
    private val applyTo: ApplyType,
    private val applyType: PotionApplyType,
    private val time: Int,
    private val chance: Double?,
    private val message: String?,
    private val messageTarget: String?
) : Effect(EffectType.POTION, plugin) {

    private fun apply(player: Player) {
        player.addPotionEffect(
            PotionEffect(potion, time, level - 1, false, true)
        )
    }

    override fun onEquipped(player: Player) {
        when (applyType) {
            PotionApplyType.APPLY -> apply(player)
            PotionApplyType.ATTACK -> registerWithFilterListener<EntityDamageByEntityEvent>({
                apply(it.entity as Player)
            }, { it.damager.name == player.name && (chance == null || (Math.random() < chance)) && it.entity is Player}, player)
            PotionApplyType.DEFENSE -> registerWithFilterListener<EntityDamageByEntityEvent>({
                apply(it.entity as Player)
            }, { it.entity.name == player.name && (chance == null || (Math.random() < chance)) && it.entity is Player}, player)
        }
    }

    override fun execute(player: Player) {


    }

    enum class PotionApplyType {
        APPLY(),
        ATTACK(),
        DEFENSE()
    }

    enum class ApplyType {
        APPLY(),
        ATTACK(),
        DEFENSE(),
        PLAYER(),
        VICTIM()
    }
}