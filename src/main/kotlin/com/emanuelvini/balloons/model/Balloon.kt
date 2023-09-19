package com.emanuelvini.balloons.model

import com.emanuelvini.balloons.effect.Effect
import org.bukkit.inventory.ItemStack

data class Balloon(
    val name : String,
    val item : ItemStack,
    val effects : List<Effect>
)
