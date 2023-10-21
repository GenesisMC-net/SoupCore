package me.smeo.soupcore.listeners;

import me.smeo.soupcore.Database.Database;
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
    }

}
