package com.emanuelvini.balloons.listener

import com.emanuelvini.balloons.configuration.ConfigurationValue
import com.emanuelvini.balloons.configuration.LanguageValue
import com.emanuelvini.balloons.manager.BalloonManager
import com.emanuelvini.balloons.util.ItemUtil
import de.tr7zw.changeme.nbtapi.NBTItem
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack


class BalloonListener(
    private val balloonManager: BalloonManager
) : Listener {


    @EventHandler
    fun onLogout(event: PlayerQuitEvent) {
        val player = event.player
        event.player.server.scheduler.scheduleSyncDelayedTask(
            balloonManager.plugin,
            {
                val activeBalloons = balloonManager.getActiveBalloons(player)
                activeBalloons.forEach { b ->
                    b.effects.forEach { it.onUnequipped(player) }
                    balloonManager.removeBalloon(player, b)
                }
                activeBalloons.clear()
            },
            10L
        )

    }
    @EventHandler
    fun onMoveInventory(event : InventoryClickEvent) {
        val balloons = balloonManager.getActiveBalloons(event.whoClicked as Player)
        if (balloons.isNotEmpty()) {
            val balloonInItemHand = balloonManager.getAppliedBalloon(event.whoClicked.itemInHand)
            if (balloons.contains(balloonInItemHand)) {
                balloonManager.unequip(event.whoClicked as Player, balloonInItemHand!!)
            }
        }
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val player = event.entity
            player.server.scheduler.scheduleSyncDelayedTask(
            balloonManager.plugin,
            {
                val activeBalloons = balloonManager.getActiveBalloons(player)
                activeBalloons.forEach { b ->
                    b.effects.forEach { it.onUnequipped(player) }
                    balloonManager.removeBalloon(player, b)
                }
            },
            10L
        )
    }

    @EventHandler
    fun onEquipArmor(event: InventoryClickEvent) {
        if (event.slotType != InventoryType.SlotType.ARMOR) return
        if (event.currentItem == event.cursor) return
        if (event.whoClicked !is Player) return
        val player = event.whoClicked as Player
        val oldBalloon = balloonManager.getAppliedBalloon(
            event.currentItem
        )
        if (oldBalloon != null) balloonManager.unequip(player, oldBalloon)

        val b = balloonManager.getAppliedBalloon(event.cursor)
        if (b != null) {
            if (balloonManager.getActiveBalloons(player).contains(b)) {
                event.isCancelled = true
                player.sendMessage(
                    LanguageValue.get(
                        LanguageValue::alreadyHaveBalloon
                    )!!
                )
                return
            }
            balloonManager.equip(player, b)
        }
    }


    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val handItem = event.player.itemInHand
        val player = event.player
        val b = balloonManager.getAppliedBalloon(handItem)
        if (b != null) {
            if (balloonManager.getActiveBalloons(player).contains(b)) {
                player.sendMessage(
                    LanguageValue.get(
                        LanguageValue::alreadyHaveBalloon
                    )!!
                )
            } else {
                balloonManager.equip(player, b)
            }
        }
        player.inventory.armorContents.forEach {
            val b = balloonManager.getAppliedBalloon(it)
            if (ItemUtil.isArmor(it.type)) return
            if (b != null) {
                if (balloonManager.getActiveBalloons(player).contains(b)) {
                    player.sendMessage(
                        LanguageValue.get(
                            LanguageValue::alreadyHaveBalloon
                        )!!
                    )
                } else {
                    balloonManager.equip(player, b)
                }
            }
        }
    }

    @EventHandler
    fun onHeld(event: PlayerItemHeldEvent) {
        val player = event.player

        try {
            val i: ItemStack? = player.inventory.getItem(
                event.previousSlot
            )
            val oldBalloon = balloonManager.getAppliedBalloon(
                i
                    ?: ItemStack(Material.AIR)
            )
            if (oldBalloon != null) {
                balloonManager.unequip(player, oldBalloon)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        val item = player.inventory.getItem(event.newSlot) ?: ItemStack(Material.AIR)
        if (ItemUtil.isArmor(item.type)) return
        val b = balloonManager.getAppliedBalloon(item)
        if (b != null) {
            if (balloonManager.getActiveBalloons(player).contains(b)) {
                event.isCancelled = true
                player.sendMessage(
                    LanguageValue.get(
                        LanguageValue::alreadyHaveBalloon
                    )!!
                )
            } else {
                balloonManager.equip(player, b)
            }
        }


    }

    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        if (event.click != ClickType.LEFT) return
        val balloonItem = event.cursor
        val targetItem = event.currentItem
        if (targetItem == null || balloonItem == null) return
        if (targetItem.type == balloonItem.type) {
            return
        }
        if (balloonItem.type != Material.AIR && targetItem.type != Material.AIR) {
            if (ConfigurationValue.get(
                    ConfigurationValue::equipable
                )!!.stream().anyMatch {
                    return@anyMatch it.uppercase() == targetItem.type.toString()
                }
            ) {
                val balloonNbt = NBTItem(balloonItem)
                if (!balloonNbt.hasTag("name")) {
                    return
                }
                val targetNbt = NBTItem(targetItem)
                if (targetNbt.hasTag("balloon")) {
                    event.whoClicked.sendMessage(
                        LanguageValue.get(
                            LanguageValue::alreadyHaveBalloon
                        )
                    )
                    return
                }
                val balloonName = balloonNbt.getString("name")
                targetNbt.setString("balloon", balloonName)
                val lore = targetItem.itemMeta.lore ?: mutableListOf<String>()
                lore.add(balloonManager.balloons[balloonName]!!.name)
                targetNbt.modifyMeta { nbt, meta ->
                    meta.lore = lore
                }
                event.currentItem = targetNbt.item
                if (balloonItem.amount == 1) {
                    event.cursor = null
                } else {
                    balloonItem.amount -= 1
                    event.cursor = balloonItem
                }
                event.whoClicked.sendMessage(
                    LanguageValue.get(
                        LanguageValue::balloonApplied
                    )!!.replace("{balloon}", balloonName)
                )
            }

        }
    }


}