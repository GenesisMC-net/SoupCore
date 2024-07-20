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

public class KitTurbo {
    public static void giveItems(Player p) {
        PlayerInventory inv = p.getInventory();
        inv.clear();

        ItemStack helmet = new ItemStack(Material.IRON_HELMET);
        inv.setHelmet(helmet);

        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
        inv.setChestplate(chestplate);

        ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
        inv.setLeggings(leggings);

        ItemStack boots = new ItemStack(Material.IRON_BOOTS);
        inv.setBoots(boots);

        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        sword.addEnchantment(Enchantment.DURABILITY, 3);

        ItemMeta swordMeta = sword.getItemMeta();

        ArrayList<String> swordLore = new ArrayList<>();
        swordLore.add("");
        swordLore.add(ChatColor.WHITE + "Every Kill as Turbo: " + ChatColor.RED + "Super Speed");
        swordLore.add(ChatColor.GRAY + "Every grants you Speed V and");
        swordLore.add(ChatColor.GRAY + "Regeneration III for 8 seconds");
        swordMeta.setLore(swordLore);

        sword.setItemMeta(swordMeta);

        inv.setItem(0, sword);

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
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
            lore.add(ChatColor.YELLOW + "Click to activate the kit!");
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
