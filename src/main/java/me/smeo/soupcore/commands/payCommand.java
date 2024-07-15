package me.smeo.soupcore.commands;

import me.smeo.soupcore.Credits;
import me.smeo.soupcore.Database.Database;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class payCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;

            if (args.length < 2) {
                return false;
            }
            Player target;
            try {
                target = p.getServer().getOfflinePlayer(args[0]).getPlayer();
            } catch (NullPointerException exc) {
                p.sendMessage(ChatColor.RED + "There is no player with the name: " + ChatColor.RESET + args[0]);
                return true;
            }

            if (target.getUniqueId().equals(p.getUniqueId())) {
                p.sendMessage(ChatColor.RED + "You cannot pay yourself!");
                return true;
            }

            int payment;
            try {
                payment = Integer.parseInt(args[1]);
            } catch (NumberFormatException exc) {
                p.sendMessage(ChatColor.RED + "Invalid pay amount: " + ChatColor.RESET + args[1]);
                return true;
            }
            if (payment <= 0) {
                p.sendMessage(ChatColor.RED + "Invalid pay amount: " + ChatColor.RESET + args[1]);
                return true;
            }

            if (!Credits.checkCreditBalance(p, payment)) {
                p.sendMessage(ChatColor.RED + "Insufficient Funds! You do not have " + ChatColor.GREEN + payment + ChatColor.RED + "credits");
                return true;
            }

            Credits.chargeCredits(p, payment);
            Credits.giveCredits(target, payment);

            p.sendMessage(ChatColor.GRAY + "You paid " + ChatColor.GREEN + payment + ChatColor.GRAY + " credits to " + ChatColor.GREEN + target.getName());
            if (target.isOnline()) {
                target.sendMessage(ChatColor.GRAY + "You received " + ChatColor.GREEN + payment + ChatColor.GRAY + " credits from " + ChatColor.GREEN + p.getName());;
            }
            return true;
        }
        return false;
    }
}
