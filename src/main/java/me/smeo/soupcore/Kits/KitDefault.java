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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KitDefault {

    public static void giveItems(Player p)
    {
        PlayerInventory inv = p.getInventory();
        inv.clear();
        inv.setHelmet(new ItemStack(Material.IRON_HELMET));
        inv.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        inv.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        inv.setBoots(new ItemStack(Material.IRON_BOOTS));

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        inv.setItem(0, sword);
    }

    public static ItemStack guiAppearance(Player player, Inventory inv) {
        String highlightedKit = ChatColor.stripColor(Methods_Kits.getActiveKit(player));

        ItemStack item = new ItemStack((Material.DIAMOND_SWORD));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Default");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "The perfect starter kit for all players!");
        lore.add("");
        lore.add(ChatColor.WHITE + "Full Iron Armour");
        lore.add(ChatColor.WHITE + "Sharpness I Diamond Sword");
        lore.add("");

        if (Objects.equals(highlightedKit, "Default")) {
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
