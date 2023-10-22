package me.smeo.soupcore.listeners;

import me.smeo.soupcore.Database.Database;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener
{

    @EventHandler
    public void OnPlayerDeath(PlayerDeathEvent e)
    {
        Player p = e.getEntity();
        Database.SetPlayerData(p, "deaths", (Database.getPlayerData(p, "deaths") + 1));
        Database.SetPlayerData(p, "killStreak", 0);

        int killStreak = Database.getPlayerData(p, "killStreak");
        if(killStreak >= 20)
        {
            e.setDeathMessage(ChatColor.RED + p.getName() + ChatColor.GRAY + " has died with a killstreak of " + ChatColor.AQUA + killStreak);
        }
        e.getEntity().spigot().respawn();
    }

}
