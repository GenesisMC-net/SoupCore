package org.genesismc.SoupCore.listeners;

import org.bukkit.event.player.PlayerQuitEvent;
import org.genesismc.SoupCore.Database.Database;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.genesismc.SoupCore.listeners.abilities.Cooldowns;

import static org.genesismc.SoupCore.commands.freezeCommand.frozenPlayers;
import static org.genesismc.SoupCore.commands.spawnCommand.teleportToSpawn;

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

        for (Player online : player.getServer().getOnlinePlayers()) {
            if (frozenPlayers.contains(online)) continue;
            player.showPlayer(online);
            online.showPlayer(player);
        }

        teleportToSpawn(player);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 10, 1);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Cooldowns.removeCooldowns(e.getPlayer());
    }
}
