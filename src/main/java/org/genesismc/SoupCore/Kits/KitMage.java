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

public class KitMage {
    public static void giveItems(Player p) {
        PlayerInventory inv = p.getInventory();
        inv.clear();

        ItemStack helmet = new ItemStack(Material.IRON_HELMET);
        helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        helmet.addEnchantment(Enchantment.DURABILITY, 3);
        inv.setHelmet(helmet);

        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
        chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        chestplate.addEnchantment(Enchantment.DURABILITY, 3);
        inv.setChestplate(chestplate);

        ItemStack leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        leggings.addEnchantment(Enchantment.DURABILITY, 3);
        inv.setLeggings(leggings);

        ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        boots.addEnchantment(Enchantment.DURABILITY, 3);
        inv.setBoots(boots);

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));

        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        sword.addEnchantment(Enchantment.DURABILITY, 3);

        inv.setItem(0, sword);

        // Ability
        ItemStack mageItem = new ItemStack(Material.INK_SACK, 1);
        mageItem.setDurability((short) 12);

        ItemMeta mageItemMeta = mageItem.getItemMeta();

        ArrayList<String> mageItemLore = new ArrayList<>();
        mageItemLore.add("");
        mageItemLore.add(ChatColor.WHITE + "Right Click: " + ChatColor.RED + "Water Attack");
        mageItemLore.add(ChatColor.GRAY + "Launch a water attack that slows");
        mageItemLore.add(ChatColor.GRAY + "players down when they are hit");
        mageItemLore.add("");
        mageItemLore.add(ChatColor.WHITE + "Shift-Right Click: " + ChatColor.RED + "Fire Jump");
        mageItemLore.add(ChatColor.GRAY + "Launch yourself into the air and");
        mageItemLore.add(ChatColor.GRAY + "set enemies below you on fire!");
        mageItemMeta.setLore(mageItemLore);

        mageItemMeta.setDisplayName(ChatColor.BLUE + "Mage Abilities");

        mageItem.setItemMeta(mageItemMeta);
        inv.setItem(1, mageItem);
    }

    public static ItemStack guiAppearance(Player player) {
        String highlightedKit = ChatColor.stripColor(Methods_Kits.getActiveKit(player));

        ItemStack item = new ItemStack((Material.INK_SACK), 1, (short) 12);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Mage");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Shoot water and launch with fire!");
        lore.add("");
        lore.add(ChatColor.WHITE + "1/2 Iron, 1/2 Chainmail Armour");
        lore.add(ChatColor.RED + "Sharpness II" + ChatColor.WHITE + " Iron Sword");
        lore.add(ChatColor.WHITE + "Permanent " + ChatColor.RED + "Speed I" + ChatColor.WHITE + " and " + ChatColor.RED + "Resistance I");
        lore.add(ChatColor.WHITE + "Fire a " + ChatColor.RED + "block of water" + ChatColor.WHITE + " that will slow down players");
        lore.add(ChatColor.RED + "Leap forwards" + ChatColor.WHITE + " with no fall damage");
        lore.add("");

        if (Objects.equals(highlightedKit, "Mage")) {
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

