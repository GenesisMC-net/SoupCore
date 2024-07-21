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

public class KitTurbo {
    public static ItemStack HELMET;
    public static ItemStack CHESTPLATE;
    public static ItemStack LEGGINGS;
    public static ItemStack BOOTS;
    public static ItemStack SWORD;

    public static void setKitItems() {
        HELMET = new ItemStack(Material.IRON_HELMET);
        CHESTPLATE = new ItemStack(Material.IRON_CHESTPLATE);
        LEGGINGS = new ItemStack(Material.IRON_LEGGINGS);
        BOOTS = new ItemStack(Material.IRON_BOOTS);

        SWORD = new ItemStack(Material.IRON_SWORD);
        SWORD.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        SWORD.addEnchantment(Enchantment.DURABILITY, 3);

        ItemMeta swordMeta = SWORD.getItemMeta();

        ArrayList<String> swordLore = new ArrayList<>();
        swordLore.add("");
        swordLore.add(ChatColor.WHITE + "Every Kill as Turbo: " + ChatColor.RED + "Super Speed");
        swordLore.add(ChatColor.GRAY + "Every grants you Speed V and");
        swordLore.add(ChatColor.GRAY + "Regeneration III for 8 seconds");
        swordMeta.setLore(swordLore);

        SWORD.setItemMeta(swordMeta);
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

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
    }

    public static void preview(Inventory inv) {
        setKitItems();
        inv.setItem(10, HELMET);
        inv.setItem(11, CHESTPLATE);
        inv.setItem(12, LEGGINGS);
        inv.setItem(13, BOOTS);

        inv.setItem(14, SWORD);
    }

    public static ItemStack guiAppearance(Player player) {
        String highlightedKit = ChatColor.stripColor(Methods_Kits.getActiveKit(player));

        ItemStack item = new ItemStack((Material.FEATHER), 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Turbo");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Gain super speed!");
        lore.add("");
        lore.add(ChatColor.WHITE + "Armour to be decided");
        lore.add(ChatColor.RED + "Sharpness I" + ChatColor.WHITE + " Iron Sword");
        lore.add(ChatColor.WHITE + "Permanent " + ChatColor.RED + "Speed I" + ChatColor.WHITE);
        lore.add(ChatColor.WHITE + "Every kill:" + ChatColor.RED + " Speed V " + ChatColor.WHITE + "&" + ChatColor.RED + " Regen III " + ChatColor.GRAY + "(8s)");
        lore.add("");

        if (Objects.equals(highlightedKit, "Turbo")) {
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
