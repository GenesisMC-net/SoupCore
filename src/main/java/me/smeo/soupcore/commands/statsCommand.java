package me.smeo.soupcore.commands;

import me.smeo.soupcore.Database.Database;
import me.smeo.soupcore.Stats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Objects;

public class statsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player)
        {
            Player player = Bukkit.getPlayer(((Player) sender).getUniqueId());

            sender.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + "Your Stats");
            sender.sendMessage("");
            sender.sendMessage(ChatColor.GOLD + "Kills: " + ChatColor.AQUA + Stats.kills(player));
            sender.sendMessage(ChatColor.GOLD + "Kill Streak: " + ChatColor.AQUA + Stats.killStreak(player));
            sender.sendMessage(ChatColor.GOLD + "Deaths: " + Stats.deaths(player));
            sender.sendMessage(ChatColor.GOLD + "KDR: " + Stats.kdr(player));
            return true;
        }
        return false;
    }
}
