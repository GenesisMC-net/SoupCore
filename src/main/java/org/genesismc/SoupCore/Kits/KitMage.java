package org.genesismc.SoupCore.Kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KitMage {
    public static ItemStack HELMET;
    public static ItemStack CHESTPLATE;
    public static ItemStack LEGGINGS;
    public static ItemStack BOOTS;
    public static ItemStack SWORD;
    public static ItemStack ABILITY_ITEM;

    public static void setKitItems() {
        HELMET = new ItemStack(Material.IRON_HELMET);
        HELMET.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        HELMET.addEnchantment(Enchantment.DURABILITY, 3);

        CHESTPLATE = new ItemStack(Material.IRON_CHESTPLATE);
        CHESTPLATE.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        CHESTPLATE.addEnchantment(Enchantment.DURABILITY, 3);

        LEGGINGS = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        LEGGINGS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        LEGGINGS.addEnchantment(Enchantment.DURABILITY, 3);

        BOOTS = new ItemStack(Material.CHAINMAIL_BOOTS);
        BOOTS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        BOOTS.addEnchantment(Enchantment.DURABILITY, 3);

        SWORD = new ItemStack(Material.IRON_SWORD);
        SWORD.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        SWORD.addEnchantment(Enchantment.DURABILITY, 3);

        // Ability
        ABILITY_ITEM = new ItemStack(Material.INK_SACK, 1);
        ABILITY_ITEM.setDurability((short) 12);

        ItemMeta mageItemMeta = ABILITY_ITEM.getItemMeta();

        ArrayList<String> mageItemLore = new ArrayList<>();
        mageItemLore.add("");
        mageItemLore.add(ChatColor.WHITE + "Right Click: " + ChatColor.RED + "Water Attack");
        mageItemLore.add(ChatColor.GRAY + "Launch a water attack that slows");
        mageItemLore.add(ChatColor.GRAY + "players down when they are hit");
        mageItemLore.add("");
        mageItemLore.add(ChatColor.WHITE + "Shift-Right Click: " + ChatColor.RED + "Fire Jump");
        mageItemLore.add(ChatColor.GRAY + "Launch yourself into the air and");
        mageItemLore.add(ChatColor.GRAY + "set enemies below you on fire!");
        mageItemMeta.setLore(mageItemLore);

        mageItemMeta.setDisplayName(ChatColor.BLUE + "Mage Abilities");

        ABILITY_ITEM.setItemMeta(mageItemMeta);
    }

    public static void giveItems(Player p)
    {
        PlayerInventory inv = p.getInventory();
        inv.clear();
        setKitItems();

        inv.setHelmet(HELMET);
        inv.setChestplate(CHESTPLATE);
        inv.setLeggings(LEGGINGS);
        inv.setBoots(BOOTS);

        inv.setItem(0, SWORD);
        inv.setItem(1, ABILITY_ITEM);

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
    }

    public static void preview(Inventory inv) {
        setKitItems();
        inv.setItem(10, HELMET);
        inv.setItem(11, CHESTPLATE);
        inv.setItem(12, LEGGINGS);
        inv.setItem(13, BOOTS);

        inv.setItem(14, SWORD);
        inv.setItem(15, ABILITY_ITEM);
    }

    public static ItemStack guiAppearance(Player player) {
        String highlightedKit = ChatColor.stripColor(Methods_Kits.getActiveKit(player));

        ItemStack item = new ItemStack((Material.INK_SACK), 1, (short) 12);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Mage");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Shoot water and launch with fire!");
        lore.add("");
        lore.add(ChatColor.WHITE + "1/2 Iron, 1/2 Chainmail Armour");
        lore.add(ChatColor.RED + "Sharpness II" + ChatColor.WHITE + " Iron Sword");
        lore.add(ChatColor.WHITE + "Permanent " + ChatColor.RED + "Speed I" + ChatColor.WHITE + " and " + ChatColor.RED + "Resistance I");
        lore.add(ChatColor.WHITE + "Fire a " + ChatColor.RED + "block of water" + ChatColor.WHITE + " that will slow down players");
        lore.add(ChatColor.RED + "Leap forwards" + ChatColor.WHITE + " with no fall damage");
        lore.add("");

        if (Objects.equals(highlightedKit, "Mage")) {
            lore.add(ChatColor.GREEN + "Kit Selected");
            meta.addEnchant(Enchantment.LUCK, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            lore.add(ChatColor.YELLOW + "Left-Click" + ChatColor.GRAY + " to activate");
            lore.add(ChatColor.YELLOW + "Right-Click" + ChatColor.GRAY + " to preview");
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}

