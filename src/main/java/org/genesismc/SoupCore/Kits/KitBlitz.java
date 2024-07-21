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

public class KitBlitz {
    public static ItemStack HELMET;
    public static ItemStack CHESTPLATE;
    public static ItemStack LEGGINGS;
    public static ItemStack BOOTS;
    public static ItemStack SWORD;
    public static ItemStack ABILITY_ITEM;

    public static void setKitItems() {
        HELMET = new ItemStack(Material.CHAINMAIL_HELMET);
        HELMET.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        HELMET.addEnchantment(Enchantment.DURABILITY, 3);

        CHESTPLATE = new ItemStack(Material.IRON_CHESTPLATE);
        CHESTPLATE.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        CHESTPLATE.addEnchantment(Enchantment.DURABILITY, 3);

        LEGGINGS = new ItemStack(Material.IRON_LEGGINGS);
        LEGGINGS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        LEGGINGS.addEnchantment(Enchantment.DURABILITY, 3);

        BOOTS = new ItemStack(Material.CHAINMAIL_BOOTS);
        BOOTS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        BOOTS.addEnchantment(Enchantment.DURABILITY, 3);

        SWORD = new ItemStack(Material.IRON_SWORD);
        SWORD.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        SWORD.addEnchantment(Enchantment.DURABILITY, 3);

        // Ability
        ABILITY_ITEM = new ItemStack(Material.ENDER_PEARL, 1);
        ItemMeta blitzPearlMeta = ABILITY_ITEM.getItemMeta();

        ArrayList<String> blitzPearlLore = new ArrayList<>();
        blitzPearlLore.add("");
        blitzPearlLore.add(ChatColor.GRAY + "Acts like a normal ender pearl");
        blitzPearlLore.add("");
        blitzPearlLore.add(ChatColor.WHITE + "Every Kill as Blitz: " + ChatColor.RED + "+1 Pearl");
        blitzPearlLore.add(ChatColor.GRAY + "With every kill you also get a");
        blitzPearlLore.add(ChatColor.GRAY + "boost of Speed III for 15s");
        blitzPearlMeta.setLore(blitzPearlLore);

        blitzPearlMeta.setDisplayName(ChatColor.YELLOW + "Blitz Pearl");

        ABILITY_ITEM.setItemMeta(blitzPearlMeta);
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

        ItemStack item = new ItemStack((Material.ENDER_PEARL), 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Blitz");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Zooooooooooooom!");
        lore.add("");
        lore.add(ChatColor.WHITE + "1/2 Iron, 1/2 Chainmail Armour");
        lore.add(ChatColor.RED + "Sharpness II" + ChatColor.WHITE + " Iron Sword");
        lore.add(ChatColor.WHITE + "Permanent " + ChatColor.RED + "Speed II" + ChatColor.WHITE);
        lore.add(ChatColor.WHITE + "Spawn with an ender pearl");
        lore.add(ChatColor.WHITE + "Every kill:" + ChatColor.RED + " +1 Ender Pearl" + ChatColor.WHITE + " & " + ChatColor.RED + "Speed III " + ChatColor.GRAY + "(15s)");
        lore.add("");

        if (Objects.equals(highlightedKit, "Blitz")) {
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
