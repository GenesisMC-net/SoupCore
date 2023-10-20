package me.smeo.soupcore.listeners;

import me.smeo.soupcore.Database.Database;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlayerKillListener implements Listener
{
    @EventHandler
    public void onPlayerKill(PlayerDeathEvent e)
    {
        Player p = e.getEntity();
        if(p.getKiller() != null)
        {
            Player killer = p.getKiller();
            Integer kills = Database.getPlayerData(killer, "kit");
            Database.SetPlayerData(killer, "kills", kills+1);
        }

    }

}
