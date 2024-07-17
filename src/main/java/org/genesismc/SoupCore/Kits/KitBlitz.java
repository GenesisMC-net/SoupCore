package org.genesismc.SoupCore.Kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
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
    public static void giveItems(Player p) {
        PlayerInventory inv = p.getInventory();
        inv.clear();

        ItemStack helmet = new ItemStack(Material.CHAINMAIL_HELMET);
        helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        helmet.addEnchantment(Enchantment.DURABILITY, 3);
        inv.setHelmet(helmet);

        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
        chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        chestplate.addEnchantment(Enchantment.DURABILITY, 3);
        inv.setChestplate(chestplate);

        ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
        leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        leggings.addEnchantment(Enchantment.DURABILITY, 3);
        inv.setLeggings(leggings);

        ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        boots.addEnchantment(Enchantment.DURABILITY, 3);
        inv.setBoots(boots);

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));

        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        sword.addEnchantment(Enchantment.DURABILITY, 3);

        inv.setItem(0, sword);

        // Ability
        ItemStack blitzPearl = new ItemStack(Material.ENDER_PEARL, 1);
        ItemMeta blitzPearlMeta = blitzPearl.getItemMeta();

        ArrayList<String> blitzPearlLore = new ArrayList<>();
        blitzPearlLore.add("");
        blitzPearlLore.add(ChatColor.GRAY + "Acts like a normal ender pearl");
        blitzPearlLore.add("");
        blitzPearlLore.add(ChatColor.WHITE + "Every Kill as Blitz: " + ChatColor.RED + "+1 Pearl");
        blitzPearlLore.add(ChatColor.GRAY + "With every kill you also get a");
        blitzPearlLore.add(ChatColor.GRAY + "boost of Speed III for 15s");
        blitzPearlMeta.setLore(blitzPearlLore);

        blitzPearlMeta.setDisplayName(ChatColor.YELLOW + "Blitz Pearl");

        blitzPearl.setItemMeta(blitzPearlMeta);
        inv.setItem(1, blitzPearl);
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
            lore.add(ChatColor.YELLOW + "Click to activate the kit!");
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
