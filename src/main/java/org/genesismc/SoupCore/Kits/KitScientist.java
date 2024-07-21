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

public class KitScientist {
    public static ItemStack HELMET;
    public static ItemStack CHESTPLATE;
    public static ItemStack LEGGINGS;
    public static ItemStack BOOTS;
    public static ItemStack SWORD;
    public static ItemStack ABILITY_ITEM_1;
    public static ItemStack ABILITY_ITEM_2;

    public static void setKitItems() {
        HELMET = new ItemStack(Material.CHAINMAIL_HELMET);
        HELMET.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        HELMET.addEnchantment(Enchantment.DURABILITY, 2);

        CHESTPLATE = new ItemStack(Material.IRON_CHESTPLATE);
        CHESTPLATE.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        CHESTPLATE.addEnchantment(Enchantment.DURABILITY, 2);

        LEGGINGS = new ItemStack(Material.IRON_LEGGINGS);
        LEGGINGS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        LEGGINGS.addEnchantment(Enchantment.DURABILITY, 2);

        BOOTS = new ItemStack(Material.IRON_BOOTS);
        BOOTS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        BOOTS.addEnchantment(Enchantment.DURABILITY, 2);

        SWORD = new ItemStack(Material.DIAMOND_SWORD);
        SWORD.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        SWORD.addEnchantment(Enchantment.DURABILITY, 2);

        ABILITY_ITEM_1 = new ItemStack(Material.POTION, 3, (short)16428);
        ABILITY_ITEM_2 = new ItemStack(Material.POTION, 1, (short)16388);
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
        inv.setItem(1, ABILITY_ITEM_1);
        inv.setItem(2, ABILITY_ITEM_2);

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
        inv.setItem(15, ABILITY_ITEM_1);
        inv.setItem(16, ABILITY_ITEM_2);
    }

    public static ItemStack guiAppearance(Player player) {
        String highlightedKit = ChatColor.stripColor(Methods_Kits.getActiveKit(player));

        ItemStack item = new ItemStack(Material.POTION, 1, (short)8196);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Scientist");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Kill another player to earn back your potions!");
        lore.add(ChatColor.GRAY + "(Max 3 instant damage & 1 poison potion)");
        lore.add("");
        lore.add(ChatColor.WHITE + "3/4 Iron Armour");
        lore.add(ChatColor.RED + "Sharpness I" + ChatColor.WHITE + " Diamond Sword");
        lore.add(ChatColor.WHITE + "Permanent " + ChatColor.RED + "Speed I" + ChatColor.WHITE + " and " + ChatColor.RED + "Resistance I" + ChatColor.WHITE);
        lore.add(ChatColor.WHITE + "Spawn with" + ChatColor.RED + " 3 Instant Damage " + ChatColor.WHITE + "and" + ChatColor.RED + " 1 Poison " + ChatColor.WHITE + "potion");
        lore.add("");

        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        if (Objects.equals(highlightedKit, "Scientist")) {
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

