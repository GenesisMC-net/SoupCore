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

public class KitSoldier {
    public static void giveItems(Player p) {
        PlayerInventory inv = p.getInventory();
        inv.clear();

        ItemStack helmet = new ItemStack(Material.GOLD_HELMET);
        helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        helmet.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
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
        ItemStack iceBlock = new ItemStack(Material.PACKED_ICE, 1);

        ItemMeta iceBlockMeta = iceBlock.getItemMeta();
        iceBlockMeta.addEnchant(Enchantment.LUCK, 1, true);
        iceBlockMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        ArrayList<String> iceBlockLore = new ArrayList<>();
        iceBlockLore.add("");
        iceBlockLore.add(ChatColor.WHITE + "Right Click: " + ChatColor.RED + "Ice Dome");
        iceBlockLore.add(ChatColor.GRAY + "Create a 5x5 ice dome with around you");
        iceBlockLore.add(ChatColor.GRAY + "The dome grants temporary strength II to");
        iceBlockMeta.setLore(iceBlockLore);

        iceBlockMeta.setDisplayName(ChatColor.DARK_AQUA + "Ice Dome");

        iceBlock.setItemMeta(iceBlockMeta);
        inv.setItem(1, iceBlock);
    }

    public static ItemStack guiAppearance(Player player, Inventory inv) {
        int highlightedKit = Methods_Kits.getActiveKit(player);

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

        if (highlightedKit == 8) {
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


