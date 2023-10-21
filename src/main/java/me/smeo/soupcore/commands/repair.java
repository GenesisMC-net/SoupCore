package me.smeo.soupcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class repair implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player)
        {
            // TODO: Costs 50 credits

            Player p = ((Player) sender).getPlayer();
            for (ItemStack item : p.getInventory().getContents())
            {
                item.setDurability((item.getType().getMaxDurability()));
            }
            p.sendMessage(ChatColor.GREEN + "Repaired armour");
        }
        return false;
    }
}
