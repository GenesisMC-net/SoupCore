package me.smeo.soupcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

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

                ArrayList<String> lore = new ArrayList<>();
                lore.add("");
                lore.add(ChatColor.RED + "Poison" + ChatColor.GRAY + " (30% chance)");
                lore.add(ChatColor.GRAY + "Inflicts poison I for 3 seconds");
                venomSwordMeta.setLore(lore);

                venomSwordMeta.setDisplayName(ChatColor.GREEN + "Dagger of Venom");

                venomSword.setItemMeta(venomSwordMeta);
                player.getInventory().addItem(venomSword);
        }



        return false;
    }
}
