package com.emanuelvini.balloons

import com.emanuelvini.balloons.command.BalloonCommand
import com.emanuelvini.balloons.configuration.ConfigurationValue
import com.emanuelvini.balloons.configuration.LanguageValue
import com.emanuelvini.balloons.listener.BalloonListener
import com.emanuelvini.balloons.manager.BalloonManager
import com.henryfabio.minecraft.configinjector.bukkit.injector.BukkitConfigurationInjector
import me.saiintbrisson.bukkit.command.BukkitFrame
import me.saiintbrisson.minecraft.command.message.MessageType
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin

class Balloons : JavaPlugin() {

    companion object {
        var instance : Balloons? = null
    }

    val balloonManager: BalloonManager = BalloonManager(this)

    fun log(m: String) {
        Bukkit.getConsoleSender().sendMessage("${ChatColor.GREEN}[Balloons] ${ChatColor.GRAY}${m}")
    }

    override fun onEnable() {
        instance = this

        val configurationInjector = BukkitConfigurationInjector(this)


        configurationInjector.saveDefaultConfiguration(
            this,
            "config.yml",
            "lang.yml"
        )

        configurationInjector.injectConfiguration(
            LanguageValue.instance,
            ConfigurationValue.instance
        )

        balloonManager.loadBalloons()

        val commandFrame = BukkitFrame(this)

        commandFrame.messageHolder.setMessage(
            MessageType.NO_PERMISSION, LanguageValue.get(
                LanguageValue::notHavePermission
            )
        )

        commandFrame.registerCommands(
            BalloonCommand(balloonManager)
        )

        server.pluginManager.registerEvents(
            BalloonListener(balloonManager),
            this
        )

        server.scheduler.scheduleSyncRepeatingTask(this, {
            server.onlinePlayers.forEach { player ->
                val balloons = balloonManager.getActiveBalloons(player)
                balloons.forEach {
                    it.effects.forEach {effect -> effect.execute(player)}
                }
            }
        }, 20L, 20L)

        log("${ChatColor.GREEN}Plugin successfully started.")
    }

}