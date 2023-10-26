package me.smeo.soupcore.Kits;

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
import org.bukkit.material.Dye;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class KitTank {
    public static void giveItems(Player p) {
        PlayerInventory inv = p.getInventory();
        inv.clear();
        inv.setArmorContents(new ItemStack[]{null, null, null, null});

        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2));

        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        inv.setItem(0, sword);

        // Ability
        ItemStack tankAbility = new ItemStack((Material.INK_SACK), 1, (short) (15 - DyeColor.CYAN.getData()));

        ItemMeta tankAbilityMeta = tankAbility.getItemMeta();

        ArrayList<String> tankAbilityLore = new ArrayList<>();
        tankAbilityLore.add("");
        tankAbilityLore.add(ChatColor.WHITE + "Right Click: " + ChatColor.RED + "Silverfish Army");
        tankAbilityLore.add(ChatColor.GRAY + "Summon 6 silverfish that will target");
        tankAbilityLore.add(ChatColor.GRAY + "nearby players dealing 1.5 hearts");
        tankAbilityMeta.setLore(tankAbilityLore);

        tankAbilityMeta.setDisplayName(ChatColor.DARK_RED + "Silverfish Army");

        tankAbility.setItemMeta(tankAbilityMeta);
        inv.setItem(1, tankAbility);
    }

    public static ItemStack guiAppearance(Player player, Inventory inv) {
        int highlightedKit = Methods_Kits.getActiveKit(player);

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

        if (highlightedKit == 12) {
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
