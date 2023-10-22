package me.smeo.soupcore.commands;

import me.smeo.soupcore.Credits;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class adminGiveCredits implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = Bukkit.getPlayer(args[0]);
        Credits.giveCredits(player, Integer.valueOf(args[1]));
        return false;
    }
}
