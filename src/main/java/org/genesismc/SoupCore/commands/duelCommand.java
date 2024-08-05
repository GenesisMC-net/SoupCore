package org.genesismc.SoupCore.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.genesismc.SoupCore.Database.Database;

import java.util.Objects;

import static org.genesismc.SoupCore.Duels.*;
import static org.genesismc.SoupCore.SoupCore.playerInSpawn;

public class duelCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        if (args.length < 1) return false;

        Player p = (Player) sender;
        Player target;
        switch (args[0].toLowerCase()) {
            case "help":
                duelHelpMsg(p);
                break;
            case "toggle":
                toggleDuels(p);
                break;
            case "stats":
                target = p;
                if (args.length > 1) {
                    target = Bukkit.getOfflinePlayer(args[1]).getPlayer();
                    if (target == null) {
                        p.sendMessage(ChatColor.RED + "There is no player called " + ChatColor.WHITE + args[1]);
                        return true;
                    }
                }

                duelStats(p, target);
                break;
            case "accept":
                if (Objects.equals(p.getWorld().getName(), "world") && !playerInSpawn(p)) {
                    p.sendMessage(ChatColor.RED + "You must be in spawn to use this command");
                    return true;
                }
                if (!activeDuelRequests.containsValue(p.getUniqueId())){
                    p.sendMessage(ChatColor.RED + "You do not have any active duel requests");
                    return true;
                }
                if (!activeDuelRequests.containsValue(p.getUniqueId())){
                    p.sendMessage(ChatColor.RED + "You do not have any active duel requests");
                    return true;
                }
                if (args.length < 2) {
                    p.sendMessage(ChatColor.RED + "Please enter a valid player name");
                    return false;
                }
                OfflinePlayer acceptedPlayer = Bukkit.getOfflinePlayer(args[1]);
                if (acceptedPlayer == null) {
                    p.sendMessage(ChatColor.RED + "Please enter a valid player name");
                    return false;
                }
                if (activeDuelRequests.get(acceptedPlayer.getPlayer().getUniqueId()) != p.getUniqueId()) {
                    p.sendMessage(ChatColor.RED + "You do not have any duel requests from " + args[1]);
                    return true;
                }
                if (!acceptedPlayer.isOnline()) {
                    p.sendMessage(ChatColor.RED + "That player has left the game, duel cancelled.");
                    activeDuelRequests.values().remove(p.getUniqueId());
                }

                activeDuelRequests.values().remove(p.getUniqueId());
                p.sendMessage(ChatColor.GRAY + "Your duel request from " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', acceptedPlayer.getPlayer().getDisplayName()) + ChatColor.GRAY + " has been" + ChatColor.GREEN + " accepted");

                acceptedPlayer.getPlayer().sendMessage(ChatColor.GRAY + "Your duel request to " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', p.getDisplayName()) + ChatColor.GRAY + " has been" + ChatColor.GREEN + " accepted");

                initialiseDuel(acceptedPlayer.getPlayer(), p);
                break;
            case "deny":
                if (!activeDuelRequests.containsValue(p.getUniqueId())){
                    p.sendMessage(ChatColor.RED + "You do not have any active duel requests");
                    return true;
                }
                if (args.length < 2) {
                    p.sendMessage(ChatColor.RED + "Please enter a valid player name");
                    return false;
                }
                OfflinePlayer deniedPlayer = Bukkit.getOfflinePlayer(args[1]);
                if (deniedPlayer == null) {
                    p.sendMessage(ChatColor.RED + "Please enter a valid player name");
                    return true;
                }
                if (activeDuelRequests.get(deniedPlayer.getPlayer().getUniqueId()) != p.getUniqueId()) {
                    p.sendMessage(ChatColor.RED + "You do not have any duel requests from " + args[1]);
                    return true;
                }

                activeDuelRequests.values().remove(p.getUniqueId());
                p.sendMessage(ChatColor.GRAY + "Your duel request from " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', deniedPlayer.getPlayer().getDisplayName()) + ChatColor.GRAY + " has been" + ChatColor.RED + " denied");

                if (Bukkit.getOnlinePlayers().contains(deniedPlayer.getPlayer())) {
                    deniedPlayer.getPlayer().sendMessage(ChatColor.GRAY + "Your duel request to " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', p.getDisplayName()) + ChatColor.GRAY + " has been" + ChatColor.RED + " denied");
                }
                break;
            case "cancel":
                if (!activeDuelRequests.containsKey(p.getUniqueId())){
                    p.sendMessage(ChatColor.RED + "You do not have any active duel requests");
                    return true;
                }
                Player requestee = Bukkit.getPlayer(activeDuelRequests.get(p.getUniqueId()));
                activeDuelRequests.remove(p.getUniqueId());
                p.sendMessage(ChatColor.GRAY + "Your duel request has been cancelled");
                if (requestee != null) {
                    requestee.sendMessage(p.getName() + ChatColor.GRAY + " has cancelled their duel request");
                }
                break;
            default:
                if (Objects.equals(p.getWorld().getName(), "world") && !playerInSpawn(p)) {
                    p.sendMessage(ChatColor.RED + "You must be in spawn to use this command");
                    return true;
                }
                if (activeDuelRequests.containsKey(p.getUniqueId())) {
                    p.sendMessage(ChatColor.RED + "You already have a duel request! (/duel cancel to cancel)");
                    return true;
                }

                target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    p.sendMessage(ChatColor.RED + "Unable to find the player " + ChatColor.WHITE + args[0]);
                    return true;
                }
                if (p == target) {
                    p.sendMessage(ChatColor.RED + "You cannot duel yourself!");
                    return true;
                }
                if (Objects.equals(Database.getPlayerData(target, "duelData", "duelsEnabled"), "false")) {
                    p.sendMessage(ChatColor.WHITE + args[0] + ChatColor.RED + " is not accepting duel requests");
                    return true;
                }

                duelRequest(p, target);
                break;
        }
        return true;
    }
}
