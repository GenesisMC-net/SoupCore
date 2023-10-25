package me.smeo.soupcore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.Material.*;

import static me.smeo.soupcore.SoupCore.plugin;

public class soupDropListener implements Listener
{

    @EventHandler
    public void onSoupDrop(PlayerDropItemEvent e)
    {
        System.out.println(e.getItemDrop().getItemStack().getType());
        if(e.getItemDrop().getItemStack().getType().equals(Material.BOWL))
        {
            e.getItemDrop().remove();
        }
        else if(e.getItemDrop().getItemStack().getType().equals(Material.MUSHROOM_SOUP)) // Ignore red, it works
        {
            BukkitScheduler scheduler = Bukkit.getScheduler();
            scheduler.runTaskLater(plugin, () -> {
                e.getItemDrop().remove();
            }, 20L * 7L);
        }
    }
}
