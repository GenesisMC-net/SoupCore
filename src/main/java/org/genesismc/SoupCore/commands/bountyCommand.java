package org.genesismc.SoupCore.commands;

import org.genesismc.SoupCore.Credits;
import org.genesismc.SoupCore.Database.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class bountyCommand implements CommandExecutor {


    public void bountyUsageMessage(CommandSender sender)
    {
        sender.sendMessage(ChatColor.BOLD.toString() + ChatColor.UNDERLINE + ChatColor.GOLD + "Bounty Command");
        sender.sendMessage(ChatColor.ITALIC.toString() + ChatColor.GOLD  + "/bounty me");
        sender.sendMessage(ChatColor.ITALIC.toString() + ChatColor.GOLD  + "/bounty create <player> <credits>");
        sender.sendMessage(ChatColor.ITALIC.toString() + ChatColor.GOLD  + "/bounty list [page]");
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = Bukkit.getPlayer(sender.getName());

        if (args.length >= 1) // Main Argument Length Check
        {
            if (args[0].equals("me") && sender instanceof Player) {
                int bounty = Integer.parseInt(Objects.requireNonNull(Database.getPlayerData(player, "soupData", "bounty")));
                String string = ChatColor.GRAY + "You have a " + ChatColor.GOLD + bounty + " credit" + ChatColor.GRAY + " bounty on your head";
                player.sendMessage(string);
                return true;
            } else if (args[0].equals("create") && sender instanceof Player) {
                Player target = null;
                try {
                    target = Bukkit.getServer().getOfflinePlayer(Database.getUUIDFromNameInDatabase(args[1])).getPlayer();
                } catch (NullPointerException exc) {
                    player.sendMessage(ChatColor.RED + "There is no player with the name: " + ChatColor.RESET + args[1]);
                }
                if (target == null) {
                    player.sendMessage(ChatColor.RED + "There is no player with the name: " + ChatColor.RESET + args[1]);
                }

                Integer previousBounty = Integer.valueOf(Objects.requireNonNull(Database.getPlayerData(target, "soupData", "bounty")));
                Integer newBounty = Integer.valueOf(args[2]);

                if (!(newBounty >= 50)) {
                    player.sendMessage(ChatColor.RED + "Minimum bounty is 50 credits");
                    return true;
                }
                if (!(Credits.checkCreditBalance(player, newBounty))) {
                    player.sendMessage(ChatColor.RED + "You do not have enough credits to complete this action!");
                    return true;
                }

                Credits.chargeCredits(player, newBounty);
                Database.setPlayerData(target, "soupData", "bounty", String.valueOf((previousBounty + newBounty)));
                assert target != null;
                Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + ChatColor.GRAY + " has set a bounty on " + ChatColor.GREEN + target.getName() + ChatColor.GRAY + " for " + ChatColor.GREEN + (newBounty) + " credits" + ChatColor.GRAY + ". Total: " + (previousBounty + newBounty));
                return true;


            } else if (args[0].equals("list")) {
                Player p = Bukkit.getPlayer(sender.getName());
                Connection connection = Database.getConnection();
                PreparedStatement queryStatement;
                try {
                    queryStatement = connection.prepareStatement("SELECT * FROM soupData WHERE bounty > 0 ORDER BY bounty DESC");
                    ResultSet rows = queryStatement.executeQuery();
                    int counter = 0;
                    String message;
                    p.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + "Top 10 Bounties");
                    while (rows.next() && counter <= 9) {
                        Integer number = counter + 1;
                        message = ChatColor.AQUA + String.valueOf(number) + ". " + ChatColor.RESET + Database.getNameFromUUIDInDatabase(rows.getString("uuid")) + ChatColor.GRAY + " | " + ChatColor.GOLD + rows.getInt("bounty");
                        p.sendMessage(message);
                        counter++;
                    }

                    connection.close();
                    return false;
                } catch (SQLException e) {
                    System.out.println("Error accessing data");
                    throw new RuntimeException(e);
                }
            }else {
                player.sendMessage("DEBUG: Invalid arg");
            }
        }
        bountyUsageMessage(sender);
        return false;
    }
}
