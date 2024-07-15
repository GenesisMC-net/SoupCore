package me.smeo.soupcore.listeners;

import me.smeo.soupcore.Database.Database;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import static me.smeo.soupcore.commands.spawnCommand.spawnInventory;

public class PlayerJoinLeaveListeners implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();
        if(!Database.isPlayerInDatabase(player, "Users"))
        {
            Database.addPlayerToDataBase(player, "Users");
        }

        Vector v = player.getVelocity();
        v.setX(0);
        v.setY(0);
        v.setZ(0);
        player.setVelocity(v);

        Location spawnLoc = player.getWorld().getSpawnLocation();

        player.teleport(spawnLoc);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 10, 1);
        spawnInventory(player);
    }

//    @EventHandler
//    public void onPlayerLeave(PlayerQuitEvent e)
//    {
//        Player player = e.getPlayer();
//    }
}
