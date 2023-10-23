package me.smeo.soupcore.listeners;

import me.smeo.soupcore.Database.Database;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();
        if(Database.isPlayerInDatabase(player) == false)
        {
            Database.addPlayerToDataBase(player);
        }
    }
}
