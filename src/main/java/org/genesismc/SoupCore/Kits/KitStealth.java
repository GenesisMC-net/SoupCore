package org.genesismc.SoupCore.Kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KitStealth {
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

        CHESTPLATE = new ItemStack(Material.LEATHER_CHESTPLATE);
        CHESTPLATE.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        CHESTPLATE.addUnsafeEnchantment(Enchantment.DURABILITY, 20);

        LEGGINGS = new ItemStack(Material.LEATHER_LEGGINGS);
        LEGGINGS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        LEGGINGS.addUnsafeEnchantment(Enchantment.DURABILITY, 20);

        BOOTS = new ItemStack(Material.LEATHER_BOOTS);
        BOOTS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        BOOTS.addUnsafeEnchantment(Enchantment.DURABILITY, 20);

        SWORD = new ItemStack(Material.IRON_SWORD);
        SWORD.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        SWORD.addEnchantment(Enchantment.DURABILITY, 3);

        // Ability
        ABILITY_ITEM = new ItemStack(Material.NETHER_STAR, 4);

        ItemMeta ninjaStarMeta = ABILITY_ITEM.getItemMeta();

        ArrayList<String> ninjaStarLore = new ArrayList<>();
        ninjaStarLore.add("");
        ninjaStarLore.add(ChatColor.WHITE + "Right Click: " + ChatColor.RED + "Star Throw");
        ninjaStarLore.add(ChatColor.GRAY + "Throw a ninja star that deals blindness");
        ninjaStarLore.add(ChatColor.GRAY + "for 5 seconds to any player it hits!");
        ninjaStarLore.add("");
        ninjaStarLore.add(ChatColor.WHITE + "Every kill with the Ninja Kit:" + ChatColor.GREEN + " +1 Ninja Star");
        ninjaStarMeta.setLore(ninjaStarLore);

        ninjaStarMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Ninja Star");

        ABILITY_ITEM.setItemMeta(ninjaStarMeta);
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

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
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

        ItemStack item = new ItemStack((Material.NETHER_STAR));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Stealth");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Speed around like a ninja");
        lore.add("");
        lore.add(ChatColor.WHITE + "Full Protection II Leather Armour");
        lore.add(ChatColor.RED + "Sharpness II" + ChatColor.WHITE + " Iron Sword");
        lore.add(ChatColor.WHITE + "Permanent " + ChatColor.RED + "Speed II" + ChatColor.WHITE + " and " + ChatColor.RED + "Resistance I" + ChatColor.WHITE);
        lore.add(ChatColor.WHITE + "Ninja Stars that deal" + ChatColor.RED + " 3 Hearts" + ChatColor.WHITE + " and " + ChatColor.RED + "Blindness for 5s");
        lore.add("");

        if (Objects.equals(highlightedKit, "Stealth")) {
            lore.add(ChatColor.GREEN + "Kit Selected");
        } else {
            lore.add(ChatColor.YELLOW + "Left-Click" + ChatColor.GRAY + " to activate");
            lore.add(ChatColor.YELLOW + "Right-Click" + ChatColor.GRAY + " to preview");
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

}


