package com.emanuelvini.balloons.effect.impl

import com.emanuelvini.balloons.Balloons
import com.emanuelvini.balloons.effect.Effect
import com.emanuelvini.balloons.effect.EffectType
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class StormEffect(
    plugin: Balloons,
    private val chance: Double,
    private val cooldown: Double,
    private val potion: PotionEffectType,
    private val level: Int
) : Effect(EffectType.STORM, plugin) {

    private val cooldownPlayers = mutableListOf<String>()

    override fun onEquipped(player: Player) {
        registerWithFilterListener<EntityDamageByEntityEvent>({
            val loc = it.damager.location
            loc.y = 255.0
            val entity = player.world.spawnEntity(loc, EntityType.SNOWBALL)
            registerListener<EntityDamageByEntityEvent>(player) { e ->
                if (e.damager == entity) {
                    if (e.entity is Player) {
                        (e.entity as Player).addPotionEffect(
                            PotionEffect(potion, 10, level - 1, false, true)
                        )
                    }
                }
            }
        }, {
            Math.random() < chance && it.damager.name == player.name && !cooldownPlayers.contains(player.name)
        }, player)
    }

    override fun execute(player: Player) {

    }
}