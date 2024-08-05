package org.genesismc.SoupCore.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.genesismc.SoupCore.SoupCore;

public class reloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        SoupCore.loadConfigs();
        commandSender.sendMessage(ChatColor.GREEN + "Successfully Reloaded Config");
        return true;
    }
}
