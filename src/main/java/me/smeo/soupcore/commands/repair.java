package me.smeo.soupcore.commands;

import me.smeo.soupcore.Credits;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
            Player p = ((Player) sender).getPlayer();
            if(Credits.checkCreditBalance(p, 50))
            {
                for (ItemStack item : p.getInventory().getContents())
                {
                    if ((item != null) && (item.getType() != Material.AIR))
                    {
                        item.setDurability((short) 0);
                    }
                }
                for (ItemStack item : p.getInventory().getArmorContents())
                {
                    if ((item != null) && (item.getType() != Material.AIR))
                    {
                        item.setDurability((short) 0);
                    }
                }
                Credits.chargeCredits(p, 50);
                p.sendMessage(ChatColor.GREEN + "Repaired Kit " + ChatColor.GRAY + "[" + ChatColor.RED + "-50" + ChatColor.GRAY + "]");
            }


        }
        return false;
    }
}
