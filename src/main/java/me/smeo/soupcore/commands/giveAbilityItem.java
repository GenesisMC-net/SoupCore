package me.smeo.soupcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class giveAbilityItem implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        switch (args[0]){
            case "Venom":
                ItemStack venomSword = new ItemStack(Material.IRON_SWORD, 1);
                venomSword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
                venomSword.addEnchantment(Enchantment.DURABILITY, 2);
                ItemMeta venomSwordMeta = venomSword.getItemMeta();

                ArrayList<String> venomSwordLore = new ArrayList<>();
                venomSwordLore.add("");
                venomSwordLore.add(ChatColor.WHITE + "On Attack: " + ChatColor.RED + "Poison" + ChatColor.GRAY + " (30% chance)");
                venomSwordLore.add(ChatColor.GRAY + "Inflicts poison I for 3 seconds");
                venomSwordMeta.setLore(venomSwordLore);

                venomSwordMeta.setDisplayName(ChatColor.GREEN + "Dagger of Venom");

                venomSword.setItemMeta(venomSwordMeta);
                player.getInventory().addItem(venomSword);
                break;
            case "Mage":
                ItemStack mageItem = new ItemStack(Material.INK_SACK, 1);
                mageItem.setDurability((short) 12);

                ItemMeta mageItemMeta = mageItem.getItemMeta();

                ArrayList<String> mageItemLore = new ArrayList<>();
                mageItemLore.add("");
                mageItemLore.add(ChatColor.WHITE + "Right Click: " + ChatColor.RED + "Water Attack");
                mageItemLore.add(ChatColor.GRAY + "Launch a water attack that slows");
                mageItemLore.add(ChatColor.GRAY + "players down when they are hit");
                mageItemLore.add("");
                mageItemLore.add(ChatColor.WHITE + "Shift-Right Click: " + ChatColor.RED + "Fire Jump");
                mageItemLore.add(ChatColor.GRAY + "Launch yourself forwards into the");
                mageItemLore.add(ChatColor.GRAY + "the direction you are facing");
                mageItemMeta.setLore(mageItemLore);

                mageItemMeta.setDisplayName(ChatColor.BLUE + "Mage Abilities");

                mageItem.setItemMeta(mageItemMeta);
                player.getInventory().addItem(mageItem);
                break;
            case "Ninja":
                ItemStack ninjaStar = new ItemStack(Material.NETHER_STAR, 4);

                ItemMeta ninjaStarMeta = ninjaStar.getItemMeta();

                ArrayList<String> ninjaStarLore = new ArrayList<>();
                ninjaStarLore.add("");
                ninjaStarLore.add(ChatColor.WHITE + "Right Click: " + ChatColor.RED + "Star Throw");
                ninjaStarLore.add(ChatColor.GRAY + "Throw a ninja star that deals blindness");
                ninjaStarLore.add(ChatColor.GRAY + "for 5 seconds to any player it hits!");
                ninjaStarLore.add("");
                ninjaStarLore.add(ChatColor.WHITE + "Every kill with the Ninja Kit:" + ChatColor.GREEN + " +1 Ninja Star");
                ninjaStarMeta.setLore(ninjaStarLore);

                ninjaStarMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Ninja Star");

                ninjaStar.setItemMeta(ninjaStarMeta);
                player.getInventory().addItem(ninjaStar);
                break;
        }
        return false;
    }
}
