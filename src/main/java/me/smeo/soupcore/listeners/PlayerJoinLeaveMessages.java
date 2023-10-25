package me.smeo.soupcore.listeners;

import me.smeo.soupcore.Database.Database;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinLeaveMessages implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();
        if(Database.isPlayerInDatabase(player) == false)
        {
            Database.addPlayerToDataBase(player);
        }
        e.setJoinMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.WHITE + player.getName());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e)
    {
        Player player = e.getPlayer();
        e.setQuitMessage(ChatColor.GRAY + "[" + ChatColor.RED + "-" + ChatColor.GRAY + "] " + ChatColor.WHITE + player.getName());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e)
    {
        Player player = e.getPlayer();
        e.setLeaveMessage(ChatColor.GRAY + "[" + ChatColor.RED + "-" + ChatColor.GRAY + "] " + ChatColor.WHITE + player.getName());
    }
}
