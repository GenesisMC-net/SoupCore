package me.smeo.soupcore.commands;

import me.smeo.soupcore.Database.Database;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ping implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player)
        {
            Player p = (Player) sender;
            p.sendMessage(ChatColor.LIGHT_PURPLE + "Pong!");
            Integer bounty = Integer.valueOf((String) Database.getPlayerData(p, "soupData", "credits"));
            p.sendMessage(bounty.toString());
            //p.sendMessage(Integer.getInteger((String) Database.getPlayerData(p, "soupData", "bounty")).toString());
        }
        return false;
    }
}
