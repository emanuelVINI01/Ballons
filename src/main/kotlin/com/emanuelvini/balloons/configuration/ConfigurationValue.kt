package com.emanuelvini.balloons.configuration

import com.henryfabio.minecraft.configinjector.common.annotations.ConfigField
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigFile
import com.henryfabio.minecraft.configinjector.common.injector.ConfigurationInjectable
import java.util.function.Function

@ConfigFile("config.yml")
class ConfigurationValue : ConfigurationInjectable {

    companion object {
        val instance : ConfigurationValue = ConfigurationValue()

        operator fun <T> get(function : Function<ConfigurationValue, T>) : T {
            return function.apply(instance)
        }
    }
    @ConfigField("equipable")
    val equipable: List<String>? = null
    @ConfigField("bad_effects")
    val badEffects: List<String>? = null

    @ConfigField("lead")
    val lead : Boolean? = null


}