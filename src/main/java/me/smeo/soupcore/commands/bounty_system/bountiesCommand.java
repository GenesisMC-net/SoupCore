package me.smeo.soupcore.commands.bounty_system;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static me.smeo.soupcore.Database.Database.*;

public class bountiesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) { //Needs fixing
        Integer page = 1;

        if(args.length >= 1)
        {
            page = Integer.getInteger(args[0]);
        }
        Player p = Bukkit.getPlayer(sender.getName());
        Connection connection = getConnection();
        PreparedStatement queryStatement;
        try{
            queryStatement = connection.prepareStatement("SELECT * FROM soupData WHERE bounty > 0 ORDER BY bounty DESC");
            ResultSet rows = queryStatement.executeQuery();
            int counter = 0;
            String message;
            while(rows.next() && counter <= 9)
            {
                OfflinePlayer player = Bukkit.getOfflinePlayer(rows.getString("uuid"));
                Integer number = ((counter+1) + 10*(page-1));
                System.out.println(rows.getString("uuid"));
                System.out.println(rows.getInt("bounty"));
                System.out.println(rows.getString("name"));
                System.out.println(((counter+1) + 10*(page-1)) );
                message = ChatColor.AQUA + String.valueOf(number) + ". " + ChatColor.RESET + rows.getString("name") + ChatColor.GRAY + " | " + ChatColor.GOLD + rows.getInt("bounty");
                p.sendMessage(message);
                counter++;
            }

            connection.close();
        }catch(SQLException e){
            System.out.println("Error accessing data");
            System.out.println(e);
        }
        return false;
    }
}
