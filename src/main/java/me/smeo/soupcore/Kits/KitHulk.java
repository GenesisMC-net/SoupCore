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

public class KitHulk {
    public static void giveItems(Player p) {
        PlayerInventory inv = p.getInventory();
        inv.clear();

        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        helmet.addUnsafeEnchantment(Enchantment.DURABILITY, 20);
        inv.setHelmet(helmet);

        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
        chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        chestplate.addEnchantment(Enchantment.DURABILITY, 3);
        inv.setChestplate(chestplate);

        ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
        leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        leggings.addEnchantment(Enchantment.DURABILITY, 3);
        inv.setLeggings(leggings);

        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        boots.addUnsafeEnchantment(Enchantment.DURABILITY, 20);
        inv.setBoots(boots);

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));

        inv.setItem(0, new ItemStack(Material.DIAMOND_SWORD));

        // Ability
        ItemStack iceBlock = new ItemStack(Material.ANVIL, 1);

        ItemMeta iceBlockMeta = iceBlock.getItemMeta();

        ArrayList<String> iceBlockLore = new ArrayList<>();
        iceBlockLore.add("");
        iceBlockLore.add(ChatColor.WHITE + "Right Click: " + ChatColor.RED + "Hulk Smash");
        iceBlockLore.add(ChatColor.GRAY + "Launches you 10 blocks into the air and deals");
        iceBlockLore.add(ChatColor.GRAY + "5 hearts to nearby players when you land");
        iceBlockMeta.setLore(iceBlockLore);

        iceBlockMeta.setDisplayName(ChatColor.DARK_GREEN + "Hulk Smash");

        iceBlock.setItemMeta(iceBlockMeta);
        inv.setItem(1, iceBlock);
    }

    public static ItemStack guiAppearance(Player player, Inventory inv) {
        int highlightedKit = Methods_Kits.getActiveKit(player);

        ItemStack item = new ItemStack((Material.ANVIL));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GREEN + "Hulk");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "HULK SMASH!");
        lore.add("");
        lore.add(ChatColor.WHITE + "1/2 Iron, 1/2 Leather Armour");
        lore.add(ChatColor.RED + "Unenchanted" + ChatColor.WHITE + " Diamond Sword");
        lore.add(ChatColor.WHITE + "Permanent " + ChatColor.RED + "Speed I" + ChatColor.WHITE + " and " + ChatColor.RED + "Resistance I" + ChatColor.WHITE);
        lore.add(ChatColor.WHITE + "Hulk smash deals " + ChatColor.RED + " Launches You " + ChatColor.WHITE + "and deals" + ChatColor.RED + " 5 Hearts");
        lore.add("");

        if (highlightedKit == 11) {
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


