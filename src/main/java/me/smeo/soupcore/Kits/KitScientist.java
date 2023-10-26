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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KitScientist {
    public static void giveItems(Player p) {
        PlayerInventory inv = p.getInventory();
        inv.clear();

        ItemStack helmet = new ItemStack(Material.CHAINMAIL_HELMET);
        helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        helmet.addEnchantment(Enchantment.DURABILITY, 2);
        inv.setHelmet(helmet);

        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
        chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        chestplate.addEnchantment(Enchantment.DURABILITY, 2);
        inv.setChestplate(chestplate);

        ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
        leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        leggings.addEnchantment(Enchantment.DURABILITY, 2);
        inv.setLeggings(leggings);

        ItemStack boots = new ItemStack(Material.IRON_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        boots.addEnchantment(Enchantment.DURABILITY, 2);
        inv.setBoots(boots);

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        sword.addEnchantment(Enchantment.DURABILITY, 2);
        inv.setItem(0, sword);

        ItemStack dmgPotion = new ItemStack(Material.POTION, 3, (short)16428);
        inv.setItem(1, dmgPotion);

        ItemStack poisonPotion = new ItemStack(Material.POTION, 1, (short)16388);
        inv.setItem(2, poisonPotion);

    }

    public static ItemStack guiAppearance(Player player, Inventory inv) {
        int highlightedKit = Methods_Kits.getActiveKit(player);

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
        if (highlightedKit == 7) {
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

