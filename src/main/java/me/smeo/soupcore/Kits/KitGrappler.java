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

public class KitGrappler {
    public static void giveItems(Player p) {
        PlayerInventory inv = p.getInventory();
        inv.clear();

        ItemStack helmet = new ItemStack(Material.IRON_HELMET);
        helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        helmet.addEnchantment(Enchantment.DURABILITY, 2);
        inv.setHelmet(helmet);

        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
        chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        chestplate.addEnchantment(Enchantment.DURABILITY, 2);
        inv.setChestplate(chestplate);

        ItemStack leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        leggings.addEnchantment(Enchantment.DURABILITY, 2);
        inv.setLeggings(leggings);

        ItemStack boots = new ItemStack(Material.IRON_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        boots.addEnchantment(Enchantment.DURABILITY, 2);
        inv.setBoots(boots);

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));

        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        sword.addEnchantment(Enchantment.DURABILITY, 1);

        inv.setItem(0, sword);

        // Ability
        ItemStack fishingRod = new ItemStack(Material.FISHING_ROD, 1);
        ItemMeta fishingRodMeta = fishingRod.getItemMeta();

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

        fishingRod.setItemMeta(fishingRodMeta);
        inv.setItem(1, fishingRod);
    }

    public static ItemStack guiAppearance(Player player, Inventory inv) {
        int highlightedKit = Methods_Kits.getActiveKit(player);

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

        if (highlightedKit == 5) {
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
