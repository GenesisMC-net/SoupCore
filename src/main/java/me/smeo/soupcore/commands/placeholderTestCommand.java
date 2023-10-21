package me.smeo.soupcore.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class placeholderTestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = Bukkit.getPlayer(args[0].toString());
        System.out.println(PlaceholderAPI.setPlaceholders(player, "%soupCore_kills%"));
        System.out.println(PlaceholderAPI.setPlaceholders(player, "%soupCore_killStreak%"));
        return false;
    }
}
