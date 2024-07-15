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
import java.util.UUID;

public class statsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player)
        {
            Player player = Bukkit.getPlayer(((Player) sender).getUniqueId());

            if (args.length == 1) {
                try {
                    player = sender.getServer().getOfflinePlayer(args[0]).getPlayer();
                    sender.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + args[0] + "'s Stats");

                    if (Database.isPlayerInDatabase(player, "Users")) {
                        sender.sendMessage(ChatColor.RED + "There is no player with the name: " + ChatColor.RESET + args[0]);
                        return true;
                    }
                } catch (NullPointerException exc) {
                    sender.sendMessage(ChatColor.RED + "There is no player with the name: " + ChatColor.RESET + args[0]);
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + "Your Stats");
            }

            sender.sendMessage("");
            sender.sendMessage(ChatColor.GOLD + "Kills: " + ChatColor.AQUA + Stats.kills(player));
            sender.sendMessage(ChatColor.GOLD + "Kill Streak: " + ChatColor.AQUA + Stats.killStreak(player));
            sender.sendMessage(ChatColor.GOLD + "Deaths: " + ChatColor.AQUA + Stats.deaths(player));
            sender.sendMessage(ChatColor.GOLD + "KDR: " + ChatColor.AQUA + Stats.kdr(player));
            return true;
        }
        return false;
    }
}
