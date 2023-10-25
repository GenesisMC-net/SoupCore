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

public class KitFisherman {
    public static void giveItems(Player p) {
        PlayerInventory inv = p.getInventory();
        inv.clear();

        ItemStack helmet = new ItemStack(Material.GOLD_HELMET);
        helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        helmet.addEnchantment(Enchantment.DURABILITY, 3);
        inv.setHelmet(helmet);

        inv.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        inv.setLeggings(new ItemStack(Material.IRON_LEGGINGS));

        ItemStack boots = new ItemStack(Material.GOLD_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        boots.addEnchantment(Enchantment.DURABILITY, 3);
        inv.setBoots(boots);

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));

        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        sword.addEnchantment(Enchantment.DURABILITY, 3);

        inv.setItem(0, sword);

        // Ability
        ItemStack fishingRod = new ItemStack(Material.FISHING_ROD, 1);
        ItemMeta fishingRodMeta = fishingRod.getItemMeta();

        fishingRodMeta.spigot().setUnbreakable(true);
        fishingRodMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        ArrayList<String> fishingRodLore = new ArrayList<>();
        fishingRodLore.add("");
        fishingRodLore.add(ChatColor.GRAY + "Can be used normally when ability is on cooldown (30s)");
        fishingRodLore.add("");
        fishingRodLore.add(ChatColor.WHITE + "On Reel: " + ChatColor.RED + "Player Reel");
        fishingRodLore.add(ChatColor.GRAY + "Reel in your rod while attached to");
        fishingRodLore.add(ChatColor.GRAY + "a player to teleport them to you");
        fishingRodMeta.setLore(fishingRodLore);

        fishingRodMeta.setDisplayName(ChatColor.DARK_GREEN + "Fishing Rod");

        fishingRod.setItemMeta(fishingRodMeta);
        inv.setItem(1, fishingRod);
    }

    public static ItemStack guiAppearance(Player player, Inventory inv) {
        int highlightedKit = Methods_Kits.getActiveKit(player);

        ItemStack item = new ItemStack((Material.FISHING_ROD), 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GREEN + "Fisherman");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Reel in players using your fishing rod");
        lore.add("");
        lore.add(ChatColor.WHITE + "1/2 Iron, 1/2 Gold Armour");
        lore.add(ChatColor.RED + "Sharpness I" + ChatColor.WHITE + " Iron Sword");
        lore.add(ChatColor.WHITE + "Permanent " + ChatColor.RED + "Speed II" + ChatColor.WHITE);
        lore.add(ChatColor.WHITE + "Fishing rod allows you to " + ChatColor.RED + "Teleport Players" + ChatColor.WHITE + "to your location");
        lore.add("");

        if (highlightedKit == 3) {
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
