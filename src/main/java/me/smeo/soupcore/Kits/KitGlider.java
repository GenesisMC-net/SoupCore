package me.smeo.soupcore.Kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.*;

public class KitGlider {
    public static void giveItems(Player p) {
        PlayerInventory inv = p.getInventory();
        inv.clear();

        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        helmet.addUnsafeEnchantment(Enchantment.DURABILITY, 20);
        inv.setHelmet(helmet);

        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
        chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        chestplate.addEnchantment(Enchantment.DURABILITY, 3);
        inv.setChestplate(chestplate);

        ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
        leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        leggings.addEnchantment(Enchantment.DURABILITY, 3);
        inv.setLeggings(leggings);

        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        boots.addUnsafeEnchantment(Enchantment.DURABILITY, 20);
        inv.setBoots(boots);

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        sword.addEnchantment(Enchantment.DURABILITY, 2);
        inv.setItem(0, sword);

        // Ability
        ItemStack gliderPearl = new ItemStack(Material.ENDER_PEARL, 4);
        ItemMeta gliderPearlMeta = gliderPearl.getItemMeta();

        ArrayList<String> gliderPearlLore = new ArrayList<>();
        gliderPearlLore.add("");
        gliderPearlLore.add(ChatColor.WHITE + "Right Click: " + ChatColor.RED + "Pearl Ride");
        gliderPearlLore.add(ChatColor.GRAY + "Throw the pearl like normal but instead");
        gliderPearlLore.add(ChatColor.GRAY + "you ride the pearl! +1 Pearl per Kill");
        gliderPearlMeta.setLore(gliderPearlLore);

        gliderPearlMeta.setDisplayName(ChatColor.YELLOW + "Glider");

        gliderPearl.setItemMeta(gliderPearlMeta);
        inv.setItem(1, gliderPearl);

    }

    public static ItemStack guiAppearance(Player player, Inventory inv) {
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
            lore.add(ChatColor.YELLOW + "Click to activate the kit!");
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

}

