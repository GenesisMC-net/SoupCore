package org.genesismc.SoupCore.Kits;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
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

public class KitTank {
    public static ItemStack HELMET;
    public static ItemStack CHESTPLATE;
    public static ItemStack LEGGINGS;
    public static ItemStack BOOTS;
    public static ItemStack SWORD;
    public static ItemStack ABILITY_ITEM;

    public static void setKitItems() {
        HELMET = null;
        CHESTPLATE = null;
        LEGGINGS = null;
        BOOTS = null;

        SWORD = new ItemStack(Material.STONE_SWORD);
        SWORD.addEnchantment(Enchantment.DAMAGE_ALL, 3);
        SWORD.addEnchantment(Enchantment.DURABILITY, 3);

        // Ability
        ABILITY_ITEM = new ItemStack((Material.INK_SACK), 1, (short) (15 - DyeColor.CYAN.getData()));

        ItemMeta tankAbilityMeta = ABILITY_ITEM.getItemMeta();

        ArrayList<String> tankAbilityLore = new ArrayList<>();
        tankAbilityLore.add("");
        tankAbilityLore.add(ChatColor.WHITE + "Right Click: " + ChatColor.RED + "Silverfish Army");
        tankAbilityLore.add(ChatColor.GRAY + "Summon 6 silverfish that will target");
        tankAbilityLore.add(ChatColor.GRAY + "nearby players dealing 1.5 hearts");
        tankAbilityMeta.setLore(tankAbilityLore);

        tankAbilityMeta.setDisplayName(ChatColor.DARK_RED + "Silverfish Army");

        ABILITY_ITEM.setItemMeta(tankAbilityMeta);
    }

    public static void giveItems(Player p)
    {
        PlayerInventory inv = p.getInventory();
        inv.clear();
        setKitItems();

        inv.setArmorContents(new ItemStack[]{null, null, null, null});

        inv.setItem(0, SWORD);
        inv.setItem(1, ABILITY_ITEM);

        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2));
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

        ItemStack item = new ItemStack((Material.INK_SACK), 1, (short) (15 - DyeColor.CYAN.getData()));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_RED + "Tank");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Nobody is getting through you!");
        lore.add("");
        lore.add(ChatColor.WHITE + "No Armour");
        lore.add(ChatColor.RED + "Sharpness III" + ChatColor.WHITE + " Stone Sword");
        lore.add(ChatColor.WHITE + "Permanent " + ChatColor.RED + "Resistance III");
        lore.add(ChatColor.WHITE + "Summon a silver fish army that deal" + ChatColor.RED + " 1.5 Hearts " + ChatColor.WHITE + "each");
        lore.add("");

        if (Objects.equals(highlightedKit, "Tank")) {
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
