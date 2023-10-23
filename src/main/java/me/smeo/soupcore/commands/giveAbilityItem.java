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
            case "Water":
                ItemStack waterAttack = new ItemStack(Material.INK_SACK, 1);
                waterAttack.setDurability((short) 12);

                ItemMeta waterAttackMeta = waterAttack.getItemMeta();

                ArrayList<String> waterAttackLore = new ArrayList<>();
                waterAttackLore.add("");
                waterAttackLore.add(ChatColor.WHITE + "Right Click: " + ChatColor.RED + "Water Attack");
                waterAttackLore.add(ChatColor.GRAY + "Launch a water attack that slows");
                waterAttackLore.add(ChatColor.GRAY + "players down when they are hit");
                waterAttackLore.add("");
                waterAttackLore.add(ChatColor.WHITE + "Shift-Right Click: " + ChatColor.RED + "Fire Jump");
                waterAttackLore.add(ChatColor.GRAY + "Boost yourself into the sky towards");
                waterAttackLore.add(ChatColor.GRAY + "the direction you are facing");
                waterAttackMeta.setLore(waterAttackLore);

                waterAttackMeta.setDisplayName(ChatColor.BLUE + "Mage Abilities");

                waterAttack.setItemMeta(waterAttackMeta);
                player.getInventory().addItem(waterAttack);
        }



        return false;
    }
}
