package org.genesismc.SoupCore.listeners;

import org.genesismc.SoupCore.Database.Database;
import org.genesismc.SoupCore.SoupCore;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.genesismc.SoupCore.listeners.abilities.Cooldowns;

import java.util.Objects;

import static org.genesismc.SoupCore.Duels.showAllPlayers;
import static org.genesismc.SoupCore.commands.spawnCommand.teleportToSpawn;

public class PlayerDeathListener implements Listener
{
    @EventHandler()
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        Player p = e.getEntity();

        e.setDeathMessage("");
        if (!Objects.equals(p.getWorld().getName(), "world")) { return; }

        Cooldowns.removeCooldowns(p);

        int killStreak = Integer.parseInt(Objects.requireNonNull(Database.getPlayerData(p, "soupData", "killStreak")));
        if(killStreak >= 5)
        {
            for (Player online : p.getWorld().getPlayers()) {
                online.sendMessage(ChatColor.RED + p.getName() + ChatColor.AQUA + " has died with a killstreak of " + ChatColor.RED + killStreak);
            }
        }

        Database.setPlayerData(p, "soupData", "deaths", String.valueOf(( Integer.parseInt(Objects.requireNonNull(Database.getPlayerData(p, "soupData", "deaths"))))+1));
        Database.setPlayerData(p, "soupData", "killStreak", String.valueOf(0));
        Location lastLoc = p.getLocation();

        int soupDrop = 0;
        for (ItemStack item: p.getInventory().getContents()) {
            if (!Objects.equals(item, null) && !Objects.equals(item.getType(), Material.AIR)) {
                if (Objects.equals(item.getType(), Material.MUSHROOM_SOUP)) {
                    soupDrop += 1;
                }
            }
        }
        for (int i = 0; i < soupDrop; i++) {
            Item droppedSoup = p.getWorld().dropItemNaturally(lastLoc, new ItemStack(Material.MUSHROOM_SOUP));
            new BukkitRunnable()
            {
                @Override
                public void run() {
                    if (Objects.equals(droppedSoup.getType(), EntityType.DROPPED_ITEM))
                    {
                        droppedSoup.remove();
                    }
                }
            }.runTaskLaterAsynchronously(SoupCore.plugin, 20L * 7L);
        }
        new BukkitRunnable()
        {
            @Override
            public void run() {
                showAllPlayers(p);
                p.spigot().respawn();
                teleportToSpawn(p);
            }
        }.runTaskLater(SoupCore.plugin, 1L);
    }
}
