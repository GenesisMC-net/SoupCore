package org.genesismc.SoupCore.commands;

import org.genesismc.SoupCore.Credits;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static org.genesismc.SoupCore.SoupCore.playerInSpawn;

public class refillCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) { return false; }
        Player p = ((Player) sender).getPlayer();

        if (playerInSpawn(p)) {
            p.sendMessage(ChatColor.RED + "You cannot use this command in spawn");
            return true;
        }

        if(Credits.checkCreditBalance(p, 200)) // Costs 200
        {
            for (ItemStack item : p.getInventory().getContents())
            {
                if ((item == null) || item.getType() == Material.AIR)
                {
                    p.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP, 1));
                }
            }
            Credits.chargeCredits(p, 200);
            p.sendMessage(ChatColor.GREEN + "Refilled Soup " + ChatColor.GRAY + "[" + ChatColor.RED + "-200" + ChatColor.GRAY + "]");
        } else {
            p.sendMessage(ChatColor.RED + "You require 200 credits to complete this action!");
        }
        return false;
    }
}
