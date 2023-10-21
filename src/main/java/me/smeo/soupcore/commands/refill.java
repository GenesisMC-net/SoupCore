package me.smeo.soupcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class refill implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player)
        {
            // TODO: Costs 200 credits

            Player p = ((Player) sender).getPlayer();
            for (ItemStack item : p.getInventory().getContents())
            {
                if ((item == null) || item.getType() == Material.AIR)
                {
                    p.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP, 1));
                }
            }
            p.sendMessage(ChatColor.GREEN + "Refilled soup");
        }
        return false;
    }
}
