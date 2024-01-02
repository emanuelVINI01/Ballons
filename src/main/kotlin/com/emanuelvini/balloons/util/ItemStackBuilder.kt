package com.emanuelvini.feastcore.bukkit.api.common

import de.tr7zw.changeme.nbtapi.NBTItem
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.stream.Collectors


/**
 * A simple builder class for the ItemStack
 * by *@ExpDev* and *@emanuelVINI*
 *
 *
 * **Note:** Colors are parsed, so you can use & and § for the name and lore
 *
 *
 * *Uses **Java 8**, so go download it if you ain't already!*
 */
class ItemStackBuilder {
    // Fundamentals
    private var material = Material.AIR
    private var amount = 1
    private var durability: Short = 0 // id -> 324:2 <- durability

    // Meta
    private var localizedName: String? = null
    private var name: String? = null
    private var lore: List<String>? = null

    // Features
    private var unbreakable = false

    // Enchantments and flags
    private var enchantments: MutableMap<Enchantment, Int>? = null
    private var itemFlags: Set<ItemFlag>? = null

    // Construction of a new builder
    constructor()
    constructor(material: Material) {
        this.material = material
    }

    fun asMaterial(material: Material): ItemStackBuilder {
        this.material = material
        return this
    }

    fun withAmount(amount: Int): ItemStackBuilder {
        this.amount = amount
        return this
    }

    fun withData(data: Short): ItemStackBuilder {
        durability = data
        return this
    }

    // In case you are too lazy to cast (if you're using int)
    fun withData(data: Int): ItemStackBuilder {
        return withData(data.toShort())
    }

    // Meta
    fun withLocalizedName(localizedName: String?): ItemStackBuilder {
        this.localizedName = localizedName
        return this
    }

    fun withName(name: String?): ItemStackBuilder {
        this.name = name
        return this
    }

    // Multiple ways you can set the lore
    // I prefer #withLore("&1Line 1", "&2Line 2", "&3Etc...")
    fun withLore(lines: List<String>?): ItemStackBuilder {
        lore = lines
        return this
    }

    fun withLore(vararg lines: String): ItemStackBuilder {
        return withLore(lines.toList())
    }

    // Just calls ItemMeta#setUnbreakable(true), don't know if compatible with old versions
    fun makeUnbreakable(): ItemStackBuilder {
        unbreakable = true
        return this
    }

    // Enchantments
    fun withEnchantments(enchantments: MutableMap<Enchantment, Int>?): ItemStackBuilder {
        this.enchantments = enchantments
        return this
    }

    fun addEnchantment(enchantment: Enchantment, level: Int): ItemStackBuilder {
        // Make sure we have something to add the enchantment to
        if (enchantments == null) {
            enchantments = HashMap()
        }
        enchantments!![enchantment] = level
        return this
    }

    // Flags
    fun withItemFlags(flags: Set<ItemFlag>?): ItemStackBuilder {
        itemFlags = flags
        return this
    }

    // Can be used to add only 1 ItemFlag (#withItemFlags(ItemFlag.HIDE_ENCHANTMENTS))
    fun withItemFlags(vararg flags: ItemFlag?): ItemStackBuilder {
        return withItemFlags(HashSet(Arrays.asList(*flags)))
    }

    fun toSkullBuilder(): SkullBuilder {
        return SkullBuilder(this)
    }

    /**
     * Builds the ItemStack with durability from this instance
     *
     * @return ItemStack with meta
     */
    fun buildStack(): ItemStack {
        // Creating a new ItemStack
        val itemStack = ItemStack(material, amount, durability)

        // Getting the stack's meta
        val itemMeta = itemStack.itemMeta

        // Meta
        // Set localized name if not null
        // Set displayname if name is not null
        if (name != null) {
            itemMeta.displayName = parseColor(name!!)
        }
        // Set lore if it is not null nor empty
        if (lore != null && !lore!!.isEmpty()) {
            itemMeta.lore =
                lore!!.stream().map { string: String ->
                    parseColor(
                        string
                    )
                }.collect(Collectors.toList())
        }
        // Add enchantments if any
        if (enchantments != null && !enchantments!!.isEmpty()) {
            // Doing this so I don't have to keep unsafe and safe enchantments separately
            // Ignore all stupid enchantment restrictions ;)
            enchantments!!.forEach { (ench: Enchantment?, lvl: Int?) ->
                itemMeta.addEnchant(
                    ench,
                    lvl, true
                )
            }
        }
        // Add flags if any
        if (itemFlags != null && !itemFlags!!.isEmpty()) {
            itemMeta.addItemFlags(*arrayOfNulls(itemFlags!!.size))
        }
        // Deprecated in newer versions, but newer method does not exist in older
        // Only call when unbreakable is true, to refrain from calling as much as possible
        // You could of course always implement your own unbreakable method here


        // Set the new ItemMeta
        itemStack.setItemMeta(itemMeta)

        // Lastly, return the stack
        return itemStack
    }

    /**
     * A simple builder for a skull with owner
     *
     *
     * **Note:** Uses the ItemStackBuilder builder ;)
     */
    class SkullBuilder(// Fundamentals
        private val stackBuilder: ItemStackBuilder
    ) {
        // Meta
        private var owner: String? = null
        private var texture: String? = null

        // Meta
        fun withOwner(ownerName: String?): SkullBuilder {
            owner = ownerName
            return this
        }

        fun withTexture(texture: String?): SkullBuilder {
            this.texture = texture
            return this
        }

        /**
         * Builds a skull from a owner
         *
         * @return ItemStack skull with owner
         */
        fun buildSkull(): ItemStack {
            val m: Material = try {
                Material.matchMaterial("PLAYER_HEAD")!!
            } catch (ex: Exception) {
                Material.SKULL_ITEM
            }
            val skull = if (m.toString() == "SKULL_ITEM") ItemStack(m, stackBuilder.amount, 3.toShort()) else ItemStack(m, stackBuilder.amount)

            val meta = skull.itemMeta
            meta.lore = stackBuilder.lore
            meta.displayName = stackBuilder.name
            skull.itemMeta = meta

            val nbti = NBTItem(skull)
            val skullNBT =
                nbti.addCompound("SkullOwner") // Getting the compound, that way we can set the skin information
            skullNBT.setString("Name", "Dragao banana")
            skullNBT.setString("Id", UUID.randomUUID().toString())
            val textureCompound = skullNBT.addCompound("Properties").getCompoundList("textures").addCompound()
            textureCompound.setString(
                "Value", Base64.getEncoder().encodeToString(
                    String.format("{\"textures\":{\"SKIN\":{\"url\":\"%s\"}}}", texture).toByteArray(
                        StandardCharsets.UTF_8
                    )
                )
            )
            return nbti.item
        }

    }

    companion object {
        // Utility methods for smooth and extensive color parsing
        private fun parseColor(string: String): String {
            var string = string
            string = parseColorAmp(string)
            return ChatColor.translateAlternateColorCodes('&', string)
        }

        private fun parseColorAmp(string: String): String {
            var string = string
            string = string.replace("(§([a-z0-9]))".toRegex(), "\u00A7$2")
            string = string.replace("(&([a-z0-9]))".toRegex(), "\u00A7$2")
            string = string.replace("&&", "&")
            return string
        }
    }
}