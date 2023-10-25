package me.smeo.soupcore.listeners;

import me.smeo.soupcore.Credits;
import me.smeo.soupcore.Database.Database;
import me.smeo.soupcore.SoupCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Random;

public class PlayerKillListener implements Listener
{


    @EventHandler
    public void onPlayerKill(PlayerDeathEvent e)
    {
        Player p = e.getEntity();
        if(p.getKiller() != null)
        {
            Player killer = p.getKiller();
            Integer kills = Integer.valueOf((String) Database.getPlayerData(killer, "soupData", "kills")) + 1;
            Database.SetPlayerData(killer, "soupData", "kills", kills);
            Integer killStreak = Integer.valueOf((String) Database.getPlayerData(killer, "soupData", "killStreak")) + 1;
            Database.SetPlayerData(killer, "soupData", "killStreak", killStreak);

            Random rand = new Random();
            int credits = rand.nextInt(6) + 5; // Replace with credit rank system when created.
            Credits.giveCredits(killer, credits);
            killer.sendMessage(ChatColor.GRAY + "You have killed " + ChatColor.GREEN + p.getName() + ChatColor.GRAY + " and earned " + ChatColor.GREEN + credits + " credits");
            p.sendMessage(ChatColor.GRAY + "You have been killed by " + ChatColor.GREEN + killer.getName());
            if(SoupCore.killStreakMilestones.contains(killStreak))
            {
                Bukkit.broadcastMessage(ChatColor.GREEN + killer.getName() + ChatColor.GRAY + " has reached a killstreak of " + ChatColor.AQUA + killStreak);
            }
            if( Integer.valueOf((String) Database.getPlayerData(p, "soupData", "bounty")) > 0)
            {
                Integer bounty = Integer.valueOf((String) Database.getPlayerData(p, "soupData", "bounty"));
                Credits.giveCredits(killer, bounty);
                Database.SetPlayerData(p, "soupData", "bounty", 0);
                Bukkit.broadcastMessage(ChatColor.GREEN + killer.getName() + ChatColor.GRAY + " has killed " + ChatColor.GREEN + p.getName() + ChatColor.GRAY + " and has claimed the " + ChatColor.GREEN + bounty + " credit " + ChatColor.GRAY + "bounty");
            }
        }

    }

}
