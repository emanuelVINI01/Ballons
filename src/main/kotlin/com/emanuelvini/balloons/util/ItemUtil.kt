package com.emanuelvini.balloons.util

import org.bukkit.Material

object ItemUtil {

    fun isArmor(material : Material) : Boolean {
        val n = material.name
        return n
            .contains("HELMET")
                ||
                n.contains("CHESTPLATE")
                || n.contains("LEGGINGS")
                || n.contains("BOOTS")
    }

}