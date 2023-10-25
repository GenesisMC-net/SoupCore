package me.smeo.soupcore.commands;

import me.smeo.soupcore.Database.Database;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class enterPVPRegion implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            Player player = Bukkit.getPlayer(args[0]);
            String kit = (String) Database.getPlayerData(player, "soupData", "kit");
            if(kit != null)
            {
                // Assign kits here
            }
        }
        return false;
    }
}
