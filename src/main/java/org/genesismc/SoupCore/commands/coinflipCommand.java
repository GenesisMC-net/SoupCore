package org.genesismc.SoupCore.commands;

import org.genesismc.SoupCore.CoinFlip;
import org.genesismc.SoupCore.listeners.CoinFlipListeners;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.genesismc.SoupCore.Duels.activeDuels;

public class coinflipCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) { return false; }

        Player p = (Player) sender;
        if (activeDuels.containsValue(p.getUniqueId()) || activeDuels.containsKey(p.getUniqueId())) {
            p.sendMessage(ChatColor.RED + "You cannot use this command while during a duel!");
            return true;
        }
        if (CoinFlipListeners.awaitingNewGameResponse.contains(p.getUniqueId())) {
            p.sendMessage(ChatColor.RED + "You are already creating a coin flip game!");
            return false;
        }
        CoinFlip.openGui(p, "main");
        return false;
    }
}
