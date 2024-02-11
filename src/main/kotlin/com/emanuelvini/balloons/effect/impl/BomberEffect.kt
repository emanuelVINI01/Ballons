package com.emanuelvini.balloons.effect.impl

import com.emanuelvini.balloons.Balloons
import com.emanuelvini.balloons.effect.Effect
import com.emanuelvini.balloons.effect.EffectType
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.player.PlayerMoveEvent

class BomberEffect(
    plugin: Balloons,
    private val chance: Double,
    private val cooldown: Double,
    private val damage: Double,
    private val freeze: Boolean,
    private val freezeTime: Double,
    private val message: String?,
    private val messageTarget: String?
) : Effect(EffectType.BOMBER, plugin) {

    private val cooldownPlayers: MutableList<String> = mutableListOf()

    private val bombers = HashMap<String, ActiveBomber>()

    override fun execute(player: Player) {
        if (cooldownPlayers.contains(player.name)) return
        if (
            Math.random() < chance
        ) {
            cooldownPlayers.add(
                player.name
            )
            plugin.server.scheduler.scheduleAsyncDelayedTask(plugin, {
                cooldownPlayers.remove(player.name)
            }, (20L * cooldown).toLong())
            val location = player.location
            val tnt = location.world.spawn(player.location, TNTPrimed::class.java)
            tnt.fuseTicks = 20
            tnt.yield = 7.5F
            tnt.passenger = player
            val activeBomber = ActiveBomber(
                tnt,
                player,
                location
            )
            bombers[player.name] = activeBomber
            val listener = object : Listener {
                private val freezePlayers: MutableList<String> = mutableListOf()

                @EventHandler
                fun onDamage(event: EntityDamageByEntityEvent) {
                    if (event.cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION && event.damager == activeBomber.tntEntity && event.entity is Player) {
                        event.isCancelled = true
                        if (event.entity.name == player.name) {
                            return
                        }
                        (event.entity as Player).damage(damage, tnt)
                        freezePlayers.add(event.entity.name)
                        if (freeze) {
                            event.entity.sendMessage(
                                messageTarget
                            )
                            if (message != null) {
                                player.sendMessage(
                                    message.replace("{target}", event.entity.name)
                                )

                            }
                        }
                    }
                }
                @EventHandler
                fun onEntityExplode(event: EntityExplodeEvent) {
                    if (event.entity == activeBomber.tntEntity) {
                        event.blockList().clear()
                        plugin.server.scheduler.scheduleSyncDelayedTask(plugin, {
                            HandlerList.unregisterAll(this)
                        }, (20L * freezeTime).toLong())
                    }
                }
                @EventHandler
                fun onMove(event : PlayerMoveEvent) {
                    if (!freeze) return
                    if (freezePlayers.contains(event.player.name)) event.isCancelled = true
                }


            }
            plugin.server.pluginManager.registerEvents(listener, plugin)
        }
    }
    private data class ActiveBomber (
        val tntEntity : TNTPrimed,
        val player : Player,
        val location: Location
    )

}