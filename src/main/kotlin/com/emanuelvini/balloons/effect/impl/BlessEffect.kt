package com.emanuelvini.balloons.effect.impl

import com.emanuelvini.balloons.Balloons
import com.emanuelvini.balloons.configuration.ConfigurationValue
import com.emanuelvini.balloons.effect.Effect
import com.emanuelvini.balloons.effect.EffectType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType

class BlessEffect(plugin: Balloons) : Effect( EffectType.BLESS, plugin) {
    override fun execute(player: Player) {

        player.activePotionEffects.filter {
            return@filter ConfigurationValue[ConfigurationValue::badEffects]!!
                .contains(it.type.name)
        }.forEach {
            player.removePotionEffect(it.type)
        }
    }
}