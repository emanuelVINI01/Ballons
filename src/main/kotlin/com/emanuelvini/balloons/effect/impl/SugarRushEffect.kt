package com.emanuelvini.balloons.effect.impl

import com.emanuelvini.balloons.Balloons
import com.emanuelvini.balloons.effect.Effect
import com.emanuelvini.balloons.effect.EffectType
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class SugarRushEffect(plugin: Balloons, private val enchantment: Enchantment, private val chance : Double?) : Effect(EffectType.SUGARRUSH, plugin) {


    override fun onEquipped(player: Player) {
        player.addPotionEffect(
            PotionEffect(
                PotionEffectType.SPEED,
                Int.MAX_VALUE,
                4,
                false,
                true
            )
        )
    }

    override fun onUnequipped(player: Player) {
        player.removePotionEffect(PotionEffectType.SPEED)
    }

    override fun execute(player: Player) {

    }
}