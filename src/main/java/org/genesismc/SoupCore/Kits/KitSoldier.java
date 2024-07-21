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

public class KitSoldier {
    public static ItemStack HELMET;
    public static ItemStack CHESTPLATE;
    public static ItemStack LEGGINGS;
    public static ItemStack BOOTS;
    public static ItemStack SWORD;
    public static ItemStack ABILITY_ITEM;

    public static void setKitItems() {
        HELMET = new ItemStack(Material.GOLD_HELMET);
        HELMET.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        HELMET.addUnsafeEnchantment(Enchantment.DURABILITY, 3);

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
        ABILITY_ITEM = new ItemStack(Material.PACKED_ICE, 1);

        ItemMeta iceBlockMeta = ABILITY_ITEM.getItemMeta();
        iceBlockMeta.addEnchant(Enchantment.LUCK, 1, true);
        iceBlockMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        ArrayList<String> iceBlockLore = new ArrayList<>();
        iceBlockLore.add("");
        iceBlockLore.add(ChatColor.WHITE + "Right Click: " + ChatColor.RED + "Ice Dome");
        iceBlockLore.add(ChatColor.GRAY + "Create a 5x5 ice dome with around you");
        iceBlockLore.add(ChatColor.GRAY + "which also grants temporary strength II");
        iceBlockMeta.setLore(iceBlockLore);

        iceBlockMeta.setDisplayName(ChatColor.DARK_AQUA + "Ice Dome");

        ABILITY_ITEM.setItemMeta(iceBlockMeta);
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

        ItemStack item = new ItemStack((Material.PACKED_ICE));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_AQUA + "Soldier");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Stand and fight private!");
        lore.add("");
        lore.add(ChatColor.WHITE + "3/4 Protection II Leather Armour");
        lore.add(ChatColor.RED + "Sharpness II" + ChatColor.WHITE + " Iron Sword");
        lore.add(ChatColor.WHITE + "Permanent " + ChatColor.RED + "Speed II" + ChatColor.WHITE + " and " + ChatColor.RED + "Resistance I" + ChatColor.WHITE);
        lore.add(ChatColor.RED + "5x5 Ice Dome" + ChatColor.WHITE + " that grants temporary " + ChatColor.RED + "Strength I");
        lore.add("");

        if (Objects.equals(highlightedKit, "Soldier")) {
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


