package me.smeo.soupcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class stats implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player)
        {
            Player player = Bukkit.getPlayer(((Player) sender).getUniqueId());

            sender.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + "Your Stats");
            sender.sendMessage("");
            sender.sendMessage(ChatColor.GOLD + "Kills: " + ChatColor.AQUA + Database.getPlayerData(player, "kills").toString());
            sender.sendMessage(ChatColor.GOLD + "Kill Streak: " + ChatColor.AQUA + Database.getPlayerData(player, "killStreak").toString());
            sender.sendMessage(ChatColor.GOLD + "Deaths: " + ChatColor.AQUA + Database.getPlayerData(player, "deaths").toString());
            float kdr = (float) Database.getPlayerData(player, "kills") / (float) Database.getPlayerData(player, "deaths");
            final DecimalFormat df = new DecimalFormat("0.00");
            sender.sendMessage(ChatColor.GOLD + "KDR: " + ChatColor.AQUA + df.format(kdr));
            return true;
        }
        return false;
    }
}
