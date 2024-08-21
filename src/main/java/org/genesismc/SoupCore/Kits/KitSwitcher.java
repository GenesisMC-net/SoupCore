package org.genesismc.SoupCore.Kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KitSwitcher {
    public static ItemStack HELMET;
    public static ItemStack CHESTPLATE;
    public static ItemStack LEGGINGS;
    public static ItemStack BOOTS;
    public static ItemStack SWORD;
    public static ItemStack ABILITY_ITEM;

    public static void setKitItems() {
        HELMET = new ItemStack(Material.IRON_HELMET);
        CHESTPLATE = new ItemStack(Material.IRON_CHESTPLATE);
        LEGGINGS = new ItemStack(Material.IRON_LEGGINGS);
        BOOTS = new ItemStack(Material.IRON_BOOTS);

        SWORD = new ItemStack(Material.IRON_SWORD);
        SWORD.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        SWORD.addEnchantment(Enchantment.DURABILITY, 3);

        // Ability
        ABILITY_ITEM = getAbilityItem(4);
    }

    public static void giveItems(@NotNull Player p)
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

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
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

    @NotNull
    public static ItemStack getAbilityItem(int amount) {
        ItemStack snowball = new ItemStack(Material.SNOW_BALL, amount);

        ItemMeta snowballMeta = snowball.getItemMeta();

        ArrayList<String> snowballLore = new ArrayList<>();
        snowballLore.add("");
        snowballLore.add(ChatColor.WHITE + "Right Click: " + ChatColor.RED + "Switcher Balls");
        snowballLore.add(ChatColor.GRAY + "Throw a snowball at a player");
        snowballLore.add(ChatColor.GRAY + "to switch places with them");
        snowballLore.add("");
        snowballLore.add(ChatColor.WHITE + "Every kill with the Switcher Kit:" + ChatColor.GREEN + " +1 Switcher Ball");
        snowballMeta.setLore(snowballLore);

        snowballMeta.setDisplayName(ChatColor.AQUA + "Switcher Ball");

        snowball.setItemMeta(snowballMeta);
        return snowball;
    }

    public static ItemStack guiAppearance(Player player) {
        String highlightedKit = ChatColor.stripColor(Methods_Kits.getActiveKit(player));

        ItemStack item = new ItemStack((Material.SNOW_BALL));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Switcher");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Switch positions with other players");
        lore.add("");
        lore.add(ChatColor.WHITE + "Armour to be determined");
        lore.add(ChatColor.RED + "Sharpness I" + ChatColor.WHITE + " Iron Sword");
        lore.add(ChatColor.WHITE + "Permanent " + ChatColor.RED + "Speed I");
        lore.add(ChatColor.WHITE + "Switcher Balls that " + ChatColor.RED + "Swap Positions" + ChatColor.WHITE + " with other players");
        lore.add("");

        if (Objects.equals(highlightedKit, "Switcher")) {
            lore.add(ChatColor.GREEN + "Kit Selected");
        } else {
            lore.add(ChatColor.YELLOW + "Left-Click" + ChatColor.GRAY + " to activate");
            lore.add(ChatColor.YELLOW + "Right-Click" + ChatColor.GRAY + " to preview");
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
