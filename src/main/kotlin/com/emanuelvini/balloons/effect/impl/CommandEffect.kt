package com.emanuelvini.balloons.effect.impl

import com.emanuelvini.balloons.Balloons
import com.emanuelvini.balloons.effect.Effect
import com.emanuelvini.balloons.effect.EffectType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

class CommandEffect(
    plugin: Balloons,
    private val command: String,
    private val applyType: CommandApplyType,
    private val targetType: CommandTargetType,
    private val chance: Double?,
    private val message: String?,
    private val messageTarget: String?
) : Effect(EffectType.COMMAND, plugin) {

    enum class CommandApplyType {
        ATTACK,
        DEFENSE,
        APPLY,
        UNAPPLY
    }

    enum class CommandTargetType {
        PLAYER,
        TARGET
    }

    override fun onEquipped(player: Player) {
        when (applyType) {
            CommandApplyType.APPLY -> plugin.server.dispatchCommand(
                plugin.server.consoleSender,
                command.replace("{PLAYER}", player.name)
            )

            CommandApplyType.ATTACK -> registerWithFilterListener<EntityDamageByEntityEvent>({
                player.sendMessage(message)
                it.entity.sendMessage(messageTarget)
                plugin.server.dispatchCommand(
                    plugin.server.consoleSender,
                    command.replace("{PLAYER}", it.entity.name)
                )
            }, {
                it.damager.name == player.name && (chance == null || Math.random() < chance)
            }, player)
            CommandApplyType.DEFENSE -> registerWithFilterListener<EntityDamageByEntityEvent>({
                player.sendMessage(message)
                it.entity.sendMessage(messageTarget)
                plugin.server.dispatchCommand(
                    plugin.server.consoleSender,
                    command.replace("{PLAYER}", it.entity.name)
                )
            }, {
                it.entity.name == player.name && (chance == null || Math.random() < chance)
            }, player)

            else -> {}
        }

    }

    override fun onUnequipped(player: Player) {
        if (applyType == CommandApplyType.UNAPPLY) {
            plugin.server.dispatchCommand(plugin.server.consoleSender, command.replace("{PLAYER}", player.name))
        }
    }

    override fun execute(player: Player) {

    }
}