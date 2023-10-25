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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class KitStealth {
    public static void giveItems(Player p) {
        PlayerInventory inv = p.getInventory();
        inv.clear();

        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        helmet.addUnsafeEnchantment(Enchantment.DURABILITY, 20);
        inv.setHelmet(helmet);

        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        chestplate.addUnsafeEnchantment(Enchantment.DURABILITY, 20);
        inv.setChestplate(chestplate);

        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        leggings.addUnsafeEnchantment(Enchantment.DURABILITY, 20);
        inv.setLeggings(leggings);

        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        boots.addUnsafeEnchantment(Enchantment.DURABILITY, 20);
        inv.setBoots(boots);

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));

        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        inv.setItem(0, sword);

        // Ability
        ItemStack ninjaStar = new ItemStack(Material.NETHER_STAR, 4);

        ItemMeta ninjaStarMeta = ninjaStar.getItemMeta();

        ArrayList<String> ninjaStarLore = new ArrayList<>();
        ninjaStarLore.add("");
        ninjaStarLore.add(ChatColor.WHITE + "Right Click: " + ChatColor.RED + "Star Throw");
        ninjaStarLore.add(ChatColor.GRAY + "Throw a ninja star that deals blindness");
        ninjaStarLore.add(ChatColor.GRAY + "for 5 seconds to any player it hits!");
        ninjaStarLore.add("");
        ninjaStarLore.add(ChatColor.WHITE + "Every kill with the Ninja Kit:" + ChatColor.GREEN + " +1 Ninja Star");
        ninjaStarMeta.setLore(ninjaStarLore);

        ninjaStarMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Ninja Star");

        ninjaStar.setItemMeta(ninjaStarMeta);
        inv.setItem(1, ninjaStar);
    }

    public static ItemStack guiAppearance(Player player, Inventory inv) {
        int highlightedKit = Methods_Kits.getActiveKit(player);

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

        if (highlightedKit == 2) {
            lore.add(ChatColor.GREEN + "Kit Selected");
        } else {
            lore.add(ChatColor.YELLOW + "Click to activate the kit!");
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

}


