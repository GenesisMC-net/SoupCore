package me.smeo.soupcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class enterPVPRegion implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            Player player = Bukkit.getPlayer(args[0]);
            Integer kit = Database.getPlayerData(player, "kit");
            if(kit != null)
            {
                // Assign kits here
            }
        }
        return false;
    }
}
