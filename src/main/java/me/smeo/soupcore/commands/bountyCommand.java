package me.smeo.soupcore.commands;

import me.smeo.soupcore.Credits;
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

import static me.smeo.soupcore.Database.Database.getConnection;
import static me.smeo.soupcore.Database.Database.isPlayerInDatabaseByName;

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
            if (args[0].equals("me")) {
                Integer bounty = Database.getPlayerData(player, "bounty");
                String string = ChatColor.GRAY + "You have a " + ChatColor.GOLD + String.valueOf(bounty) + " credit" + ChatColor.GRAY + " bounty on your head";
                player.sendMessage(string);
                return false;


            } else if (args[0].equals("create")) {
                if (args.length >= 3) {
                    Integer newBounty;
                    Integer previousBounty = null;
                    String targetName = "";
                    try{newBounty = Integer.valueOf(args[2]);}catch (NumberFormatException ex){bountyUsageMessage(sender); return false;};
                    if(isPlayerInDatabaseByName(args[1]))
                    {
                        if (newBounty >= 50) {
                            if (Credits.checkCreditBalance(player, newBounty)) {

                                Connection connection1 = getConnection();
                                PreparedStatement queryStatement;
                                try{
                                    queryStatement = connection1.prepareStatement("SELECT * FROM soupData WHERE LOWER(name) = '" + args[1].toLowerCase() + "'");
                                    ResultSet rows = queryStatement.executeQuery();
                                    while(rows.next())
                                    {
                                        previousBounty = rows.getInt("bounty");
                                        targetName = rows.getString("name");
                                        break;
                                    }
                                    connection1.close();
                                }catch(SQLException e){
                                    System.out.println("Error accessing data");
                                    System.out.println(e);
                                    sender.sendMessage("Error, please contact an admin.");
                                    return false;
                                }

                                Credits.chargeCredits(player, newBounty);
                                Integer totalBounty = previousBounty + newBounty;
                                Connection connection2 = getConnection();
                                PreparedStatement statement;
                                try{
                                    statement = connection2.prepareStatement("UPDATE soupData SET bounty = " + totalBounty + " WHERE LOWER(name) = '" + args[1].toLowerCase() + "'");
                                    statement.execute();
                                    connection2.close();
                                }catch(SQLException e){
                                    System.out.println("Error modifying data");
                                    System.out.println(e);
                                    sender.sendMessage("Error, please contact an admin.");
                                    return false;
                                }


                                Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + ChatColor.GRAY + " has set a bounty on " + ChatColor.GREEN + targetName + ChatColor.GRAY + " for " + ChatColor.GREEN + (newBounty) + " credits" + ChatColor.GRAY + ". Total: " + (totalBounty));
                            } else {
                                player.sendMessage(ChatColor.RED + "You do not have enough credits to complete this action!");
                            }
                            return false;
                        } else {
                            sender.sendMessage(ChatColor.RED + "Minimum bounty is 50 credits");
                            return false;
                        }
                    } else {
                        bountyUsageMessage(sender);
                    }
                    return false;
                }
                bountyUsageMessage(sender);
                return false;

            } else if (args[0].equals("list")) {
                Integer page = 1;
                if (args.length >= 2) {
                    page = Integer.getInteger(args[1]);
                }
                Player p = Bukkit.getPlayer(sender.getName());
                Connection connection = getConnection();
                PreparedStatement queryStatement;
                try {
                    queryStatement = connection.prepareStatement("SELECT * FROM soupData WHERE bounty > 0 ORDER BY bounty DESC");
                    ResultSet rows = queryStatement.executeQuery();
                    int counter = 0;
                    String message;
                    while (rows.next() && counter <= 9) {
                        Integer number = ((counter + 1) + 10 * (page - 1));
                        System.out.println(rows.getString("uuid"));
                        System.out.println(rows.getInt("bounty"));
                        System.out.println(rows.getString("name"));
                        System.out.println(((counter + 1) + 10 * (page - 1)));
                        message = ChatColor.AQUA + String.valueOf(number) + ". " + ChatColor.RESET + rows.getString("name") + ChatColor.GRAY + " | " + ChatColor.GOLD + rows.getInt("bounty");
                        p.sendMessage(message);
                        counter++;
                    }

                    connection.close();
                    return false;
                } catch (SQLException e) {
                    System.out.println("Error accessing data");
                    System.out.println(e);
                }
            }else {
                player.sendMessage("DEBUG: Invalid arg");
            }


        }

        bountyUsageMessage(sender);

        return false;
    }
}
