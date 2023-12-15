package com.emanuelvini.balloons.command

import com.emanuelvini.balloons.manager.BalloonManager
import me.saiintbrisson.minecraft.command.annotation.Command
import me.saiintbrisson.minecraft.command.command.Context
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BalloonCommand (
    private val balloonManager: BalloonManager
) {

    @Command(
        name = "balloons",
        permission = "balloons.admin"
    )
    fun onHelp(context : Context<CommandSender>) {

    }

    @Command(
        name = "balloons.give"
    )
    fun onGive(context : Context<CommandSender>, player : Player,  balloonName : String, amount : Int) {
        val balloon = balloonManager.balloons[balloonName]
        if (balloon == null) {
            context.sendMessage("§cBalloon $balloonName not exists.")
            return
        }
        if (amount < 1) {
            context.sendMessage("§cAmount $amount is invalid.")
            return
        }
        balloonManager.giveBalloon(
            player,
            balloon,
            amount
        )
    }

    @Command(
        name = "balloons.reload"
    )
    fun onReload(context : Context<CommandSender>) {
        balloonManager.loadBalloons()
        context.sendMessage("§aSuccessfully reloaded.")
    }



}