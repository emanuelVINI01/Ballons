package com.emanuelvini.balloons.manager

import com.emanuelvini.balloons.Balloons
import com.emanuelvini.balloons.configuration.ConfigurationValue
import com.emanuelvini.balloons.effect.Effect
import com.emanuelvini.balloons.effect.EffectType
import com.emanuelvini.balloons.hex.Skull
import com.emanuelvini.balloons.model.Balloon
import com.emanuelvini.feastcore.bukkit.api.common.ItemStackBuilder
import de.tr7zw.changeme.nbtapi.NBT
import de.tr7zw.changeme.nbtapi.NBTItem
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.Pig
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.io.File
import java.nio.file.Files


class BalloonManager(
    val plugin: Balloons
) {

    val balloons: HashMap<String, Balloon> = HashMap()


    val batEntities = HashMap<String, Entity>()

    private val activeBalloons = HashMap<String, MutableList<Balloon>>()

    fun getActiveBalloons(player: Player): MutableList<Balloon> {
        return activeBalloons[player.name] ?: mutableListOf()
    }

    private fun summonBalloon(player: Player, balloon: Balloon, leash: Boolean) {
        val world = player.world

        val batEntity = batEntities[player.name]
        if (batEntity == null) {
            val nbtItem = NBTItem(balloon.item)
            nbtItem.setString("name", balloon.name)
            val armorStand = world.spawn(player.location, ArmorStand::class.java)
            armorStand.customName = balloon.item.itemMeta.displayName
            armorStand.helmet = nbtItem.item
            armorStand.isCustomNameVisible = true
            armorStand.isVisible = false
            armorStand.isSmall = true
            armorStand.setGravity(false)
            if (leash) {
                val mobLoc = player.location.add(
                    player.location.direction.multiply(-2)
                )
                    .add(.0, 1.5, .0)
                val bat = world.spawn(mobLoc, Pig::class.java)
                bat.addPotionEffect(
                    PotionEffect(
                        PotionEffectType.INVISIBILITY,
                        Int.MAX_VALUE,
                        1,
                        false
                    )
                )
                NBT.modify(bat) {
                    it.setBoolean("NoAI", true)
                }
                val handler = bat.javaClass.getMethod("getHandle").invoke(bat)
                val handlerClazz = handler.javaClass


                bat.leashHolder = player
                bat.setPassenger(armorStand)
                var ix = 0
                ix = plugin.server.scheduler.scheduleSyncRepeatingTask(plugin, {
                    try {
                        if (activeBalloons[player.name]!!.contains(balloon)) {
                            handlerClazz.getMethod(
                                "setPositionRotation",
                                Double::class.javaPrimitiveType,
                                Double::class.javaPrimitiveType,
                                Double::class.javaPrimitiveType,
                                Float::class.javaPrimitiveType,
                                Float::class.javaPrimitiveType
                            ).invoke(
                                handler,
                                player.location.x + 0.3,
                                player.location.y + 0.25,
                                player.location.z + 0.25,
                                0.0f, 0.0f
                            )

                        } else {
                            player.server.scheduler.cancelTask(ix)
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        player.server.scheduler.cancelTask(ix)
                    }
                }, 20L, 1L)
                batEntities[player.name] = bat
            } else {
                batEntities[player.name] = armorStand
                var ix = 0
                ix = plugin.server.scheduler.scheduleSyncRepeatingTask(plugin, {
                    try {
                        if (activeBalloons[player.name]!!.contains(balloon)) {
                            armorStand.teleport(
                                Location(
                                    player.world,
                                    player.location.x + 0.3,
                                    player.location.y + 0.25,
                                    player.location.z + 0.25,
                                    0.0f, 0.0f
                                )
                            )

                        } else {
                            player.server.scheduler.cancelTask(ix)
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        player.server.scheduler.cancelTask(ix)
                    }
                }, 20L, 1L)
            }
        }
    }

    private fun loadTask() {
        //TODO : Not needed more because item when spawned will be renamed
    }

    fun loadBalloons() {
        balloons.clear()
        loadTask()
        val balloonsDir = File(plugin.dataFolder, "balloons")

        if (!balloonsDir.exists()) {
            balloonsDir.mkdirs()
            Files.copy(
                plugin.getResource("balloons/test.yml"),
                File(balloonsDir, "test.yml").toPath()
            )
        }

        balloonsDir.listFiles()?.forEach {
            try {
                val configuration = YamlConfiguration.loadConfiguration(it)
                val name = it.name.split(".")[0]
                val balloon = loadBalloon(configuration, name)
                balloons[name] = balloon
                plugin.log("${ChatColor.AQUA}Successfully loaded balloon ${ChatColor.GREEN}${name}${ChatColor.AQUA}!")
            } catch (ex: Exception) {
                plugin.log("${ChatColor.DARK_RED}Failed to load balloon ${ChatColor.WHITE}${it.name}${ChatColor.DARK_RED}, check the error below:")
                ex.printStackTrace()
            }
        }

        plugin.log("${ChatColor.AQUA}Successfully loaded all balloons")
    }


    fun giveBalloon(player: Player, balloon: Balloon, amount: Int) {
        val item = balloon.item.clone()
        val nbti = NBTItem(item)
        nbti.setString("name", balloon.name)
        val itt = nbti.item
        itt.amount = amount
        player.inventory.addItem(
            itt
        )

    }

    fun equip(player: Player, b: Balloon) {
        b.effects.forEach { it.onEquipped(player) }
        if (activeBalloons[player.name] == null) {
            activeBalloons[player.name] = mutableListOf()
        }
        activeBalloons[player.name]!!.add(b)
        summonBalloon(
            player,
            b,
            ConfigurationValue[ConfigurationValue::lead]!!
        )
    }

    fun getAppliedBalloon(item: ItemStack): Balloon? {
        return try {
            val nbt = NBTItem(item)
            return balloons[
                nbt.getString("balloon")
            ]
        } catch (ex: Exception) {
            null
        }
    }

    fun unequip(player: Player, b: Balloon) {
        b.effects.forEach { it.onUnequipped(player) }
        if (activeBalloons[player.name] == null) {
            activeBalloons[player.name] = mutableListOf()
        }
        removeBalloon(player, b)
        activeBalloons[player.name]!!.remove(b)
    }

    fun removeBalloon(player: Player, balloon: Balloon) {
        val entity = batEntities[player.name]
        if (entity != null) {



                if (ConfigurationValue[ConfigurationValue::lead] == true) {
                    val p = entity.passenger as ArmorStand
                    val t = NBTItem(p.helmet).getString("name")
                    if (t != balloon.name) return;
                    (entity as Pig).remove()
                    entity.leashHolder = null
                    p.isCustomNameVisible = false
                    p.eject()
                    p.damage(p.maxHealth + 1)
                    p.remove()
                    batEntities.remove(player.name)
                }
                else {
                    val armorStand = entity as ArmorStand
                    val helmet = armorStand.helmet
                    val t = NBTItem(helmet).getString("name")
                    if (t != balloon.name) return;
                    armorStand.remove()
                    armorStand.damage(10000.0)
                    batEntities.remove(player.name)

            }
        }

    }

    private fun loadBalloon(section: ConfigurationSection, name: String): Balloon {
        val m: Material = try {
            Material.matchMaterial("PLAYER_HEAD")!!
        } catch (ex: Exception) {
            Material.SKULL_ITEM
        }

        val effects: MutableList<Effect> = mutableListOf()

        section.getStringList("effects").forEach {
            val props = it.split(":")
            try {
                val effectType = EffectType.valueOf(props[0].uppercase())
                props.map { l ->
                    ChatColor.translateAlternateColorCodes('&', l)
                }
                val effect = effectType.asEffect.apply(
                    (props.subList(
                        1,
                        props.size
                    )).map { s -> ChatColor.translateAlternateColorCodes('&', s) })!!
                effect.setup()
                effects.add(effect)

            } catch (ex: Exception) {
                plugin.log("${ChatColor.DARK_RED}Failed to load effect ${ChatColor.WHITE}${props[0]}${ChatColor.DARK_RED}:")
                ex.printStackTrace()
            }
        }

        return Balloon(
            name,
            ItemStackBuilder()
                .withName(ChatColor.translateAlternateColorCodes('&', section.getString("item.name")))
                .withLore(section.getStringList("item.lore").map {
                    ChatColor.translateAlternateColorCodes('&', it)
                })
                .toSkullBuilder()
                .withTexture(section.getString("item.head_texture"))
                .buildSkull(),
            effects
        )
    }

}