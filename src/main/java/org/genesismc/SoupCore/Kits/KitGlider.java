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

import java.util.*;

public class KitGlider {
    public static ItemStack HELMET;
    public static ItemStack CHESTPLATE;
    public static ItemStack LEGGINGS;
    public static ItemStack BOOTS;
    public static ItemStack SWORD;
    public static ItemStack ABILITY_ITEM;

    public static void setKitItems() {
        HELMET = new ItemStack(Material.LEATHER_HELMET);
        HELMET.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        HELMET.addUnsafeEnchantment(Enchantment.DURABILITY, 20);

        CHESTPLATE = new ItemStack(Material.IRON_CHESTPLATE);
        CHESTPLATE.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        CHESTPLATE.addEnchantment(Enchantment.DURABILITY, 3);

        LEGGINGS = new ItemStack(Material.IRON_LEGGINGS);
        LEGGINGS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        LEGGINGS.addEnchantment(Enchantment.DURABILITY, 3);

        BOOTS = new ItemStack(Material.LEATHER_BOOTS);
        BOOTS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        BOOTS.addUnsafeEnchantment(Enchantment.DURABILITY, 20);

        SWORD = new ItemStack(Material.DIAMOND_SWORD);
        SWORD.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        SWORD.addEnchantment(Enchantment.DURABILITY, 2);

        // Ability
        ABILITY_ITEM = new ItemStack(Material.ENDER_PEARL, 4);
        ItemMeta gliderPearlMeta = ABILITY_ITEM.getItemMeta();

        ArrayList<String> gliderPearlLore = new ArrayList<>();
        gliderPearlLore.add("");
        gliderPearlLore.add(ChatColor.WHITE + "Right Click: " + ChatColor.RED + "Pearl Ride");
        gliderPearlLore.add(ChatColor.GRAY + "Throw the pearl like normal but instead");
        gliderPearlLore.add(ChatColor.GRAY + "you ride the pearl! +1 Pearl per Kill");
        gliderPearlMeta.setLore(gliderPearlLore);

        gliderPearlMeta.setDisplayName(ChatColor.YELLOW + "Glider");

        ABILITY_ITEM.setItemMeta(gliderPearlMeta);
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

        ItemStack item = new ItemStack(Material.ENDER_PEARL, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Glider");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Kill another player to earn back your pearl!");
        lore.add("");
        lore.add(ChatColor.WHITE + "1/2 Iron, 1/2 Leather Armour");
        lore.add(ChatColor.RED + "Sharpness I" + ChatColor.WHITE + " Diamond Sword");
        lore.add(ChatColor.WHITE + "Permanent " + ChatColor.RED + "Speed I");
        lore.add(ChatColor.WHITE + "Throw a pearl to" + ChatColor.RED + " Ride Your Pearl ");
        lore.add("");

        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        if (Objects.equals(highlightedKit, "Glider")) {
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

