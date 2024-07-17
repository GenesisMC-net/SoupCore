package org.genesismc.SoupCore.commands;

import org.genesismc.SoupCore.Kits.Methods_Kits;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class kitsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player)
        {
            Methods_Kits.createKitInventory((Player) sender);
        }
        return false;
    }
}
