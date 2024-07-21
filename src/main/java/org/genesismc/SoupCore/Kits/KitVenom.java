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

public class KitVenom {
    public static ItemStack HELMET;
    public static ItemStack CHESTPLATE;
    public static ItemStack LEGGINGS;
    public static ItemStack BOOTS;
    public static ItemStack SWORD;

    public static void setKitItems() {
        HELMET = new ItemStack(Material.IRON_HELMET);
        HELMET.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        HELMET.addEnchantment(Enchantment.DURABILITY, 2);

        CHESTPLATE = new ItemStack(Material.IRON_CHESTPLATE);
        CHESTPLATE.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        CHESTPLATE.addEnchantment(Enchantment.DURABILITY, 2);

        LEGGINGS = new ItemStack(Material.IRON_LEGGINGS);
        LEGGINGS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        LEGGINGS.addEnchantment(Enchantment.DURABILITY, 2);

        BOOTS = new ItemStack(Material.LEATHER_BOOTS);
        BOOTS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        BOOTS.addUnsafeEnchantment(Enchantment.DURABILITY, 20);

        // Ability
        SWORD = new ItemStack(Material.IRON_SWORD);
        SWORD.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        SWORD.addEnchantment(Enchantment.DURABILITY, 3);

        ItemMeta venomSwordMeta = SWORD.getItemMeta();

        ArrayList<String> venomSwordLore = new ArrayList<>();
        venomSwordLore.add("");
        venomSwordLore.add(ChatColor.WHITE + "On Attack: " + ChatColor.RED + "Poison" + ChatColor.GRAY + " (30% chance)");
        venomSwordLore.add(ChatColor.GRAY + "Inflicts poison I for 3 seconds");
        venomSwordMeta.setLore(venomSwordLore);

        venomSwordMeta.setDisplayName(ChatColor.GREEN + "Dagger of Venom");

        SWORD.setItemMeta(venomSwordMeta);
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

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
    }

    public static void preview(Inventory inv) {
        setKitItems();
        inv.setItem(10, HELMET);
        inv.setItem(11, CHESTPLATE);
        inv.setItem(12, LEGGINGS);
        inv.setItem(13, BOOTS);

        inv.setItem(14, SWORD);
    }

    public static ItemStack guiAppearance(Player player) {
        String highlightedKit = ChatColor.stripColor(Methods_Kits.getActiveKit(player));

        ItemStack item = new ItemStack((Material.IRON_SWORD));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Venom");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Poison your enemy and deal extra damage!");
        lore.add("");
        lore.add(ChatColor.WHITE + "3/4 Iron Armour");
        lore.add(ChatColor.RED + "Sharpness I" + ChatColor.WHITE + " Iron Sword");
        lore.add(ChatColor.WHITE + "Permanent " + ChatColor.RED + "Speed I" + ChatColor.WHITE);
        lore.add(ChatColor.WHITE + "Sword deals 3 seconds of" + ChatColor.RED + " Poison III " + ChatColor.GRAY + "(30% chance)");
        lore.add("");

        if (Objects.equals(highlightedKit, "Venom")) {
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

