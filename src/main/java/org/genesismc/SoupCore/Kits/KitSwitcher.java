package org.genesismc.SoupCore.Kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
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
        inv.setItem(0, sword);

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));

        // Ability
        ItemStack snowball = getAbilityItem(4);
        inv.setItem(1, snowball);
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
            lore.add(ChatColor.YELLOW + "Click to activate the kit!");
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
