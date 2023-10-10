package com.emanuelvini.balloons.configuration

import com.henryfabio.minecraft.configinjector.common.annotations.ConfigField
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigFile
import com.henryfabio.minecraft.configinjector.common.annotations.TranslateColors
import com.henryfabio.minecraft.configinjector.common.injector.ConfigurationInjectable
import java.util.function.Function


@ConfigFile("lang.yml")
@TranslateColors
class LanguageValue : ConfigurationInjectable {

    companion object {
        val instance : LanguageValue = LanguageValue()

        fun <T> get(function : Function<LanguageValue, T>) : T {
            return function.apply(instance)
        }
    }

    @ConfigField("not_have_permission")
    val notHavePermission : String? = null

    @ConfigField("already_have_active")
    val alreadyHaveActive : String? = null
    @ConfigField("balloon_applied")
    val balloonApplied : String? = null

    @ConfigField("already_have_balloon")
    val alreadyHaveBalloon : String? = null

}