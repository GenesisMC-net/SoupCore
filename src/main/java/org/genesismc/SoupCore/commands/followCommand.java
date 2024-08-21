package org.genesismc.SoupCore.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class followCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        if (args.length != 1) return false;

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            p.sendMessage(ChatColor.RED + "There is no player with the name " + ChatColor.WHITE + args[0]);
            return true;
        }

        p.setAllowFlight(true);
        p.setFlying(true);

        Vector direction = target.getLocation().getDirection().subtract(p.getLocation().getDirection());

        Location location = target.getLocation().add(2, 2, 2);
        location.setDirection(direction);

        p.teleport(location);


        return true;
    }
}
