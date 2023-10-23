package com.emanuelvini.balloons.effect.impl

import com.emanuelvini.balloons.Balloons
import com.emanuelvini.balloons.effect.Effect
import com.emanuelvini.balloons.effect.EffectType
import net.splodgebox.eliteapi.EliteAPI
import org.bukkit.entity.Player
import net.splodgebox.eliteapi.eliteenchantments.EliteEnchantmentsAPI
class DisableGroup(plugin: Balloons, private val chance : Double, private val cooldown : Double, private val group : String, private val message : String?, private val messageTarget : String?) : Effect(EffectType.DISABLE_GROUP, plugin) {

    private val disableEnchant :(() -> Void)? = null

    override fun setup() {
        try {
            val api = null
        } catch (_: Exception) {}
        if (disableEnchant == null) {
            plugin.log("§cGroup $group was not found, please check")
        }
    }

    override fun execute(player: Player) {

    }
}