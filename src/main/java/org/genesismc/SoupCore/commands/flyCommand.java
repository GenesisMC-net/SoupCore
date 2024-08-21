package org.genesismc.SoupCore.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class flyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        if (p.getAllowFlight()) {
            p.setAllowFlight(false);
            p.sendMessage(ChatColor.RED + "Disabled flight");
        } else {
            p.setAllowFlight(true);
            p.sendMessage(ChatColor.GREEN + "Enabled flight");
        }
        return true;
    }
}
