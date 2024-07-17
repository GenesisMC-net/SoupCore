package org.genesismc.SoupCore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.scheduler.BukkitScheduler;

import static org.genesismc.SoupCore.SoupCore.plugin;

public class ItemDropListener implements Listener
{

    @EventHandler
    public void onSoupDrop(PlayerDropItemEvent e)
    {
        if(e.getItemDrop().getItemStack().getType().equals(Material.BOWL))
        {
            e.getItemDrop().remove();
        }
        else if(e.getItemDrop().getItemStack().getType().equals(Material.MUSHROOM_SOUP)) // Ignore red, it works
        {
            BukkitScheduler scheduler = Bukkit.getScheduler();
            scheduler.runTaskLater(plugin, () -> e.getItemDrop().remove(), 20L * 7L);
        } else {
            e.setCancelled(true);
        }
    }
}
