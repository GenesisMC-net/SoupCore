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

public class KitSpiderman {
    public static void giveItems(Player p) {
        PlayerInventory inv = p.getInventory();
        inv.clear();

        ItemStack helmet = new ItemStack(Material.CHAINMAIL_HELMET);
        helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        helmet.addEnchantment(Enchantment.DURABILITY, 3);
        inv.setHelmet(helmet);

        ItemStack chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        chestplate.addEnchantment(Enchantment.DURABILITY, 3);
        inv.setChestplate(chestplate);

        ItemStack leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        leggings.addEnchantment(Enchantment.DURABILITY, 3);
        inv.setLeggings(leggings);

        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        boots.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        inv.setBoots(boots);

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));

        // Ability
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        inv.setItem(0, sword);

        ItemStack web = new ItemStack(Material.WEB);

        ItemMeta webMeta = web.getItemMeta();

        ArrayList<String> webLore = new ArrayList<>();
        webLore.add("");
        webLore.add(ChatColor.WHITE + "Right Click: " + ChatColor.RED + "Web Attack");
        webLore.add(ChatColor.GRAY + "Throw your web like an ender-pearl and trap players for 10s!");
        webMeta.setLore(webLore);

        webMeta.setDisplayName(ChatColor.WHITE + "Spider Webs");

        web.setItemMeta(webMeta);
        inv.setItem(1, web);
    }

    public static ItemStack guiAppearance(Player player, Inventory inv) {
        int highlightedKit = Methods_Kits.getActiveKit(player);

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

        if (highlightedKit == 2) {
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

