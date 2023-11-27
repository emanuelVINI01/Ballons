package com.emanuelvini.balloons.effect

import com.emanuelvini.balloons.Balloons
import com.emanuelvini.balloons.effect.impl.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.potion.PotionEffectType
import java.util.function.Function


enum class EffectType(
    val asEffect: Function<List<String>, Effect?>
) {

    BLESS({
        BlessEffect(
            Balloons.instance!!
        )
    }),
    BOMBER({
        BomberEffect(
            Balloons.instance!!,
            it[0].toDouble(),
            it[1].toDouble(),
            it[2].toDouble(),
            it[3].uppercase() == "TRUE",
            it[4].toDouble(),
            try {
                it[5]
            } catch (ex: Exception) {
                null
            },
            try {
                it[6]
            } catch (ex: Exception) {
                null
            }
        )
    }),
    BOW_DAMAGE({
        BowExtraEffect(
            Balloons.instance!!,
            it[0].toDouble() / 100,
            try {
                it[1].toDouble() / 100
            } catch (ex: Exception) {
                null
            },
            try {
                it[2]
            } catch (ex: Exception) {
                null
            },
            try {
                it[3]
            } catch (ex: Exception) {
                null
            },
        )
    }),
    BOW_REDUCTION({
        BowExtraEffect(
            Balloons.instance!!,
            it[0].toDouble() / 100,
            try {
                it[1].toDouble() / 100
            } catch (ex: Exception) {
                null
            },
            try {
                it[2]
            } catch (ex: Exception) {
                null
            },
            try {
                it[3]
            } catch (ex: Exception) {
                null
            },
        )
    }),
    FLY({
        FlyEffect(
            Balloons.instance!!
        )
    }),
    DAMAGE({
        DamageEffect(
            Balloons.instance!!,
            it[0].toDouble() / 100,
            try {
                it[1].toDouble() / 100
            } catch (ex: Exception) {
                null
            },
            try {
                it[2]
            } catch (ex: Exception) {
                null
            },
            try {
                it[3]
            } catch (ex: Exception) {
                null
            },
        )
    }),
    DODGE({
        DodgeEffect(
            Balloons.instance!!,
            it[0].toDouble() / 100,
            it[1].toDouble(),
            try {
                it[2]
            } catch (ex: Exception) {
                null
            },
        )
    }),
    DROPS({
        DropsEffect(
            Balloons.instance!!,
            it[0].toDouble(),
            try {
                it[1].toDouble() / 100
            } catch (ex: Exception) {
                null
            },
            try {
                it[2]
            } catch (ex: Exception) {
                null
            },
        )
    }),
    DURABILITY({
        DurabilityEffect(
            Balloons.instance!!,
            it[0].toDouble(),
            try {
                it[1].toDouble()
            } catch (ex: Exception) {
                null
            },
            try {
                it[2]
            } catch (ex: Exception) {
                null
            },
        )
    }),
    HEALTH({
        HealthEffect(
            Balloons.instance!!,
            it[0].toDouble()
        )
    }),
    EXP({
        ExpEffect(
            Balloons.instance!!,
            it[0].toDouble(),
            try {
                it[1].toDouble() / 100
            } catch (ex: Exception) {
                null
            },
            try {
                it[2]
            } catch (ex: Exception) {
                null
            },
        )
    }),
    FALL_DAMAGE({
        FallNegateEffect(
            Balloons.instance!!,
            try {
                it[1].toDouble() / 100
            } catch (ex: Exception) {
                null
            },
            try {
                it[2]
            } catch (ex: Exception) {
                null
            }
        )
    }),
    POTION({
        PotionEffect(
            Balloons.instance!!,
            PotionEffectType.getByName(it[0]),
            it[1].toInt(),
            PotionEffect.ApplyType.valueOf(it[2]),
            PotionEffect.PotionApplyType.valueOf(it[3]),
            it[4].toInt(),
            try {
                it[5].toDouble() / 100
            } catch (ex: Exception) {
                null
            },
            try {
                it[7]
            } catch (ex: Exception) {
                null
            },
            try {
                it[6]
            } catch (ex: Exception) {
                null
            }
        )
    }),
    REDUCTION({
        ReductionEffect(
            Balloons.instance!!,
            it[0].toDouble() / 100,
            try {
                it[1].toDouble() / 100
            } catch (ex: Exception) {
                null
            },
            try {
                it[2]
            } catch (ex: Exception) {
                null
            },
            try {
                it[3]
            } catch (ex: Exception) {
                null
            },
        )
    }),
    SMITE({
        SmiteEffect(
            Balloons.instance!!,
            it[0].toDouble() / 100,
            it[1].toDouble(),
            it[2].toDouble(),
            it[3].toDouble(),
            try {
                it[4]
            } catch (ex: Exception) {
                null
            },
            try {
                it[5]
            } catch (ex: Exception) {
                null
            },
        )
    }),
    SUFFOCATE({
        SuffocateEffect(
            Balloons.instance!!,
            it[0].toDouble() / 100,
            it[1].toDouble(),
            it[2].toDouble(),
        )
    }),
    STORM({
        StormEffect(
            Balloons.instance!!,
            it[0].toDouble() / 100,
            it[1].toDouble(),
            PotionEffectType.getByName(it[0]),
            it[1].toInt()
        )
    }),
    COMMAND({
        CommandEffect(
            Balloons.instance!!,
            it[0],
            CommandEffect.CommandApplyType.valueOf(it[1]),
            CommandEffect.CommandTargetType.valueOf(it[2]),
            try {
                it[3].toDouble() / 100
            } catch (ex: Exception) {
                null
            },
            try {
                it[4]
            } catch (ex: Exception) {
                null
            },
            try {
                it[5]
            } catch (ex: Exception) {
                null
            },
        )
    }),
    WARP({
        WarpEffect(
            Balloons.instance!!,
            it[0].toDouble() / 100
        )
    }),
    SUGARRUSH({
        SugarRushEffect(
            Balloons.instance!!,
            Enchantment.getByName(it[0]),
            try {
                it[1].toDouble() / 100
            } catch (ex: Exception) {
                null
            }
        )
    }),
    WEB({
        WebEffect(
            Balloons.instance!!,
            it[0].toDouble()/100,
            it[1].toInt(),
            it[2].toDouble()
        )
    }),
    MIDASTOUCH({
        MidasTouch(
            Balloons.instance!!,
            try {it[0].toDouble()/100} catch(ex : Exception) {null}
        )
    }),
    CAKEMASH({
        MidasTouch(
            Balloons.instance!!,
            try {it[0].toDouble()/100} catch(ex : Exception) {null}
        )
    }),
    DISABLE_GROUP({
        DisableGroup(
            Balloons.instance!!,
            it[0].toDouble()/100,
            it[1].toDouble(),
            it[2],
            it.getOrNull(3),
            it.getOrNull(4)
        )

    })

}