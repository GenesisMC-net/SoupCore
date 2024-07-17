package org.genesismc.SoupCore.commands;

import org.genesismc.SoupCore.Database.Database;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class balCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;

            if (args.length >= 1) {
                Player target;
                try {
                    target = p.getServer().getOfflinePlayer(args[0]).getPlayer();
                } catch (NullPointerException exc) {
                    p.sendMessage(ChatColor.RED + "There is no player with the name: " + ChatColor.RESET + args[0]);
                    return true;
                }
                String balance = (String) Database.getPlayerData(p, "soupData", "credits");

                p.sendMessage(ChatColor.GREEN + target.getDisplayName() + ChatColor.GRAY + " has " + ChatColor.GREEN + balance + ChatColor.GRAY + " credits");
                return true;
            }

            String balance = (String) Database.getPlayerData(p, "soupData", "credits");

            p.sendMessage(ChatColor.GRAY + "You have " + ChatColor.GREEN + balance + ChatColor.GRAY + " credits");
            return true;
        }
        return false;
    }
}
