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

public class KitFisherman {

    public static ItemStack HELMET;
    public static ItemStack CHESTPLATE;
    public static ItemStack LEGGINGS;
    public static ItemStack BOOTS;
    public static ItemStack SWORD;
    public static ItemStack ABILITY_ITEM;

    public static void setKitItems() {
        HELMET = new ItemStack(Material.GOLD_HELMET);
        HELMET.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        HELMET.addEnchantment(Enchantment.DURABILITY, 3);

        CHESTPLATE = new ItemStack(Material.GOLD_CHESTPLATE);
        CHESTPLATE.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        CHESTPLATE.addEnchantment(Enchantment.DURABILITY, 3);

        LEGGINGS = new ItemStack(Material.IRON_LEGGINGS);
        BOOTS = new ItemStack(Material.IRON_BOOTS);

        SWORD = new ItemStack(Material.IRON_SWORD);
        SWORD.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        SWORD.addEnchantment(Enchantment.DURABILITY, 3);

        // Ability
        ABILITY_ITEM = new ItemStack(Material.FISHING_ROD, 1);
        ItemMeta fishingRodMeta = ABILITY_ITEM.getItemMeta();

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

        ABILITY_ITEM.setItemMeta(fishingRodMeta);
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
        inv.setItem(1, ABILITY_ITEM);
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
    }

    public static void preview(Inventory inv) {
        setKitItems();
        inv.setItem(10, HELMET);
        inv.setItem(11, CHESTPLATE);
        inv.setItem(12, LEGGINGS);
        inv.setItem(13, BOOTS);

        inv.setItem(14, SWORD);
        inv.setItem(15, ABILITY_ITEM);
    }

    public static ItemStack guiAppearance(Player player) {
        String highlightedKit = ChatColor.stripColor(Methods_Kits.getActiveKit(player));

        ItemStack item = new ItemStack((Material.FISHING_ROD), 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GREEN + "Fisherman");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Reel in players using your fishing rod");
        lore.add("");
        lore.add(ChatColor.WHITE + "1/2 Iron, 1/2 Gold Armour");
        lore.add(ChatColor.RED + "Sharpness II" + ChatColor.WHITE + " Iron Sword");
        lore.add(ChatColor.WHITE + "Permanent " + ChatColor.RED + "Speed II" + ChatColor.WHITE);
        lore.add(ChatColor.WHITE + "Fishing rod allows you to " + ChatColor.RED + "Teleport Players" + ChatColor.WHITE + " to your location");
        lore.add("");

        if (Objects.equals(highlightedKit, "Fisherman")) {
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
