package me.smeo.soupcore.commands;

import me.smeo.soupcore.SoupCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class kitsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player)
        {
            Player player = (Player) sender;
            player.openInventory(SoupCore.kits);

        }
        return false;
    }
}
