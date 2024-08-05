package org.genesismc.SoupCore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.genesismc.SoupCore.Database.Database;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.Vector;
import org.genesismc.SoupCore.listeners.abilities.Cooldowns;

import java.util.Objects;

import static org.genesismc.SoupCore.commands.spawnCommand.spawnInventory;

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

        player.setFlying(false);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setWalkSpeed(0.2F);
        player.setExp(0);
        player.setLevel(0);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        for (Player online : player.getServer().getOnlinePlayers()) {
            player.showPlayer(online);
            online.showPlayer(player);
        }

        Vector v = player.getVelocity();
        v.setX(0);
        v.setY(0);
        v.setZ(0);
        player.setVelocity(v);

        Location spawnLoc = Bukkit.getWorld("world").getSpawnLocation();

        player.teleport(spawnLoc);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 10, 1);
        spawnInventory(player);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Cooldowns.removeCooldowns(e.getPlayer());
    }
}
