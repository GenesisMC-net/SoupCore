package org.genesismc.SoupCore.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class gamemodeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player target = (Player) sender;
        if (args.length > 1) {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "There is no player with the name " + ChatColor.WHITE + args[0]);
                return true;
            }
        }

        switch (label.toLowerCase()) {
            case "gms":
            case "survival":
                target.setGameMode(GameMode.SURVIVAL);
                break;
            case "gmc":
            case "creative":
                target.setGameMode(GameMode.CREATIVE);
                break;
            case "gmsp":
            case "spectator":
                target.setGameMode(GameMode.SPECTATOR);
                break;
        }


        return true;
    }
}
