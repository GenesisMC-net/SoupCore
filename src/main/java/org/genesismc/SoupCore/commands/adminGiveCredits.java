package org.genesismc.SoupCore.commands;

import org.genesismc.SoupCore.Credits;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class adminGiveCredits implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = Bukkit.getPlayer(args[0]);
        Credits.giveCredits(player, Integer.parseInt(args[1]));
        return false;
    }
}
