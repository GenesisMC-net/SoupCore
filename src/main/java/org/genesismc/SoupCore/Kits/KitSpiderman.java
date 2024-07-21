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

public class KitSpiderman {
    public static ItemStack HELMET;
    public static ItemStack CHESTPLATE;
    public static ItemStack LEGGINGS;
    public static ItemStack BOOTS;
    public static ItemStack SWORD;
    public static ItemStack ABILITY_ITEM;

    public static void setKitItems() {
        HELMET = new ItemStack(Material.CHAINMAIL_HELMET);
        HELMET.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        HELMET.addEnchantment(Enchantment.DURABILITY, 3);

        CHESTPLATE = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        CHESTPLATE.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        CHESTPLATE.addEnchantment(Enchantment.DURABILITY, 3);

        LEGGINGS = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        LEGGINGS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        LEGGINGS.addEnchantment(Enchantment.DURABILITY, 3);

        BOOTS = new ItemStack(Material.LEATHER_BOOTS);
        BOOTS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        BOOTS.addUnsafeEnchantment(Enchantment.DURABILITY, 20);


        SWORD = new ItemStack(Material.DIAMOND_SWORD);
        SWORD.addEnchantment(Enchantment.DAMAGE_ALL, 1);

        // Ability
        ABILITY_ITEM = new ItemStack(Material.WEB);
        ItemMeta webMeta = ABILITY_ITEM.getItemMeta();

        ArrayList<String> webLore = new ArrayList<>();
        webLore.add("");
        webLore.add(ChatColor.WHITE + "Right Click: " + ChatColor.RED + "Web Attack");
        webLore.add(ChatColor.GRAY + "Throw your web at an enemy");
        webLore.add(ChatColor.GRAY + "to trap them in webs for 10s!");
        webMeta.setLore(webLore);

        webMeta.setDisplayName(ChatColor.WHITE + "Spider Webs");

        ABILITY_ITEM.setItemMeta(webMeta);
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

        ItemStack item = new ItemStack((Material.WEB));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Spiderman");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Pew Pew! Shoot webs at your enemies");
        lore.add("");
        lore.add(ChatColor.WHITE + "3/4 Chainmail Armour");
        lore.add(ChatColor.RED + "Sharpness I" + ChatColor.WHITE + " Diamond Sword");
        lore.add(ChatColor.WHITE + "Permanent " + ChatColor.RED + "Speed I" + ChatColor.WHITE + " and " + ChatColor.RED + "Resistance I" + ChatColor.WHITE);
        lore.add(ChatColor.WHITE + "Webs that hit players slow them down in a" + ChatColor.RED + " ring of cobwebs");
        lore.add("");

        if (Objects.equals(highlightedKit, "Spiderman")) {
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

