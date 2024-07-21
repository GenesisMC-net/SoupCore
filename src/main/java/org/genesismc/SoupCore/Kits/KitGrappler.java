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

public class KitGrappler {
    public static ItemStack HELMET;
    public static ItemStack CHESTPLATE;
    public static ItemStack LEGGINGS;
    public static ItemStack BOOTS;
    public static ItemStack SWORD;
    public static ItemStack ABILITY_ITEM;

    public static void setKitItems() {
        HELMET = new ItemStack(Material.IRON_HELMET);
        HELMET.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        HELMET.addEnchantment(Enchantment.DURABILITY, 2);

        CHESTPLATE = new ItemStack(Material.IRON_CHESTPLATE);
        CHESTPLATE.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        CHESTPLATE.addEnchantment(Enchantment.DURABILITY, 2);

        LEGGINGS = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        LEGGINGS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        LEGGINGS.addEnchantment(Enchantment.DURABILITY, 2);

        BOOTS = new ItemStack(Material.IRON_BOOTS);
        BOOTS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        BOOTS.addEnchantment(Enchantment.DURABILITY, 2);

        SWORD = new ItemStack(Material.IRON_SWORD);
        SWORD.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        SWORD.addEnchantment(Enchantment.DURABILITY, 1);

        // Ability
        ABILITY_ITEM = new ItemStack(Material.FISHING_ROD, 1);
        ItemMeta fishingRodMeta = ABILITY_ITEM.getItemMeta();

        fishingRodMeta.spigot().setUnbreakable(true);
        fishingRodMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        ArrayList<String> fishingRodLore = new ArrayList<>();
        fishingRodLore.add("");
        fishingRodLore.add(ChatColor.GRAY + "Can be used normally when ability is on cooldown (20s)");
        fishingRodLore.add("");
        fishingRodLore.add(ChatColor.WHITE + "On Reel: " + ChatColor.RED + "Grapple");
        fishingRodLore.add(ChatColor.GRAY + "Right click while the rod is");
        fishingRodLore.add(ChatColor.GRAY + "in mid-air to be launched!");
        fishingRodMeta.setLore(fishingRodLore);

        fishingRodMeta.setDisplayName(ChatColor.DARK_GRAY + "Grappling Hook");

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
        meta.setDisplayName(ChatColor.DARK_GRAY + "Grappler");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Grapple forwards using your rod");
        lore.add("");
        lore.add(ChatColor.WHITE + "3/4 Iron Armour");
        lore.add(ChatColor.RED + "Sharpness II" + ChatColor.WHITE + " Iron Sword");
        lore.add(ChatColor.WHITE + "Permanent " + ChatColor.RED + "Speed II" + ChatColor.WHITE);
        lore.add(ChatColor.RED + "Fishing rod" + ChatColor.WHITE + " that launches you forwards");
        lore.add("");

        if (Objects.equals(highlightedKit, "Grappler")) {
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
