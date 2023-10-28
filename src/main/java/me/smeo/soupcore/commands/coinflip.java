package me.smeo.soupcore.commands;

import me.smeo.soupcore.CoinFlip;
import me.smeo.soupcore.listeners.CoinFlipListeners;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class coinflip implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player)
        {
            Player p = (Player) sender;
            if (CoinFlipListeners.awaitingNewGameResponse.contains(p.getUniqueId())) {
                p.sendMessage(ChatColor.RED + "You are already creating a coin flip game!");
                return false;
            }
            CoinFlip.openGui(p, "main");
        }
        return false;
    }
}
