package com.emanuelvini.balloons.hex

import com.emanuelvini.balloons.hex.Reflections.getField
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import org.apache.commons.codec.binary.Base64
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

/**
 * Represents some special mob heads, also support creating player skulls and custom skulls.
 *
 * @author xigsag, SBPrime
 */
enum class Skull(
    /**
     * Return the skull's id.
     *
     * @return id
     */
    val id: String
) {
    ARROW_LEFT("MHF_ArrowLeft"),
    ARROW_RIGHT("MHF_ArrowRight"),
    ARROW_UP("MHF_ArrowUp"),
    ARROW_DOWN("MHF_ArrowDown"),
    QUESTION("MHF_Question"),
    EXCLAMATION("MHF_Exclamation"),
    CAMERA("FHG_Cam"),
    ZOMBIE_PIGMAN("MHF_PigZombie"),
    PIG("MHF_Pig"),
    SHEEP("MHF_Sheep"),
    BLAZE("MHF_Blaze"),
    CHICKEN("MHF_Chicken"),
    COW("MHF_Cow"),
    SLIME("MHF_Slime"),
    SPIDER("MHF_Spider"),
    SQUID("MHF_Squid"),
    VILLAGER("MHF_Villager"),
    OCELOT("MHF_Ocelot"),
    HEROBRINE("MHF_Herobrine"),
    LAVA_SLIME("MHF_LavaSlime"),
    MOOSHROOM("MHF_MushroomCow"),
    GOLEM("MHF_Golem"),
    GHAST("MHF_Ghast"),
    ENDERMAN("MHF_Enderman"),
    CAVE_SPIDER("MHF_CaveSpider"),
    CACTUS("MHF_Cactus"),
    CAKE("MHF_Cake"),
    CHEST("MHF_Chest"),
    MELON("MHF_Melon"),
    LOG("MHF_OakLog"),
    PUMPKIN("MHF_Pumpkin"),
    TNT("MHF_TNT"),
    DYNAMITE("MHF_TNT2");

    val skull: ItemStack
        /**
         * Return the skull of the enum.
         *
         * @return itemstack
         */
        get() {
            val itemStack = ItemStack(Material.SKULL_ITEM, 1, 3.toShort())
            val meta = itemStack.itemMeta as SkullMeta
            meta.setOwner(id)
            itemStack.setItemMeta(meta)
            return itemStack
        }

    companion object {
        private val base64 = Base64()

        /**
         * Return a skull that has a custom texture specified by url.
         *
         * @param url skin url
         * @return itemstack
         */
        fun getCustomSkull(url: String, name : String, lore : List<String>): ItemStack {
            val profile = GameProfile(UUID.randomUUID(), null)
            val propertyMap = profile.properties
                ?: throw IllegalStateException("Profile doesn't contain a property map")
            val encodedData = base64.encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).toByteArray())
            propertyMap.put("textures", Property("textures", String(encodedData)))
            val m: Material = try {
                Material.matchMaterial("PLAYER_HEAD")!!
            } catch (ex: Exception) {
                Material.SKULL_ITEM
            }
            val head = if (m == Material.SKULL_ITEM) ItemStack(Material.SKULL_ITEM, 1, 3.toShort()) else ItemStack(Material.matchMaterial("PLAYER_HEAD"))
            val headMeta = head.itemMeta
            headMeta.lore = lore
            headMeta.displayName = name
            val headMetaClass: Class<*> = headMeta.javaClass
            getField(headMetaClass, "profile", GameProfile::class.java)[headMeta] = profile
            head.setItemMeta(headMeta)
            return head
        }

        /**
         * Return a skull of a player.
         *
         * @param name player's name
         * @return itemstack
         */
        fun getPlayerSkull(name: String?): ItemStack {
            val itemStack = ItemStack(Material.SKULL_ITEM, 1, 3.toShort())
            val meta = itemStack.itemMeta as SkullMeta
            meta.setOwner(name)
            itemStack.setItemMeta(meta)
            return itemStack
        }
    }
}
