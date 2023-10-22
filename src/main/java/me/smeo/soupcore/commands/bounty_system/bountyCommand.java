package me.smeo.soupcore.commands.bounty_system;

import me.smeo.soupcore.Credits;
import me.smeo.soupcore.Database.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class bountyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length >= 2) // /bounty <player> <amount>
        {
            Player player = Bukkit.getPlayer(sender.getName());
            Player bountyPlayer = Bukkit.getPlayer(args[0]);
            Integer newBounty = Integer.valueOf(args[1]);
            if(bountyPlayer != null)
            {
                if(newBounty >= 50)
                {
                    if(Credits.checkCreditBalance(player, newBounty))
                    {
                        Integer previousBounty = Database.getPlayerData(player, "bounty");
                        Credits.chargeCredits(player, newBounty);
                        Database.SetPlayerData(bountyPlayer, "bounty", newBounty+previousBounty);
                        Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + ChatColor.GRAY + " has set a bounty for a total of " + ChatColor.GREEN + (newBounty+previousBounty) + " credits");
                    }else
                    {
                        player.sendMessage(ChatColor.RED + "You do not have enough credits to complete this action!");
                    }
                    return false;
                }else
                {
                    sender.sendMessage(ChatColor.RED + "Minimum bounty is 50 credits");
                }
            }
        }
        sender.sendMessage(ChatColor.RED + "/bounty <player> <amount>");
        return false;
    }
}
