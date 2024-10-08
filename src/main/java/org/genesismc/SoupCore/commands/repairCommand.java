package org.genesismc.SoupCore.commands;

import com.alonsoaliaga.alonsolevels.api.AlonsoLevelsAPI;
import org.genesismc.SoupCore.Credits;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.genesismc.SoupCore.XP;

import static org.genesismc.SoupCore.SoupCore.playerInSpawn;

public class repairCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) { return false; }
        Player p = ((Player) sender).getPlayer();

        if (playerInSpawn(p)) {
            p.sendMessage(ChatColor.RED + "You cannot use this command in spawn");
            return true;
        }

        if(Credits.checkCreditBalance(p, 50))
        {
            for (ItemStack item : p.getInventory().getContents())
            {
                if ((item != null) && (item.getType() != Material.AIR) && item.getType() != Material.INK_SACK)
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
            AlonsoLevelsAPI.addExperience(p.getUniqueId(), XP.repair);
            p.sendMessage(ChatColor.GREEN + "Repaired Kit " + ChatColor.GRAY + "[" + ChatColor.RED + "-50" + ChatColor.GRAY + "]");
        } else {
            p.sendMessage(ChatColor.RED + "You require 50 credits to complete this action!");
        }
        return false;
    }
}
