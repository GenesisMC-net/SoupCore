package org.genesismc.SoupCore.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.genesismc.SoupCore.SoupCore;

import java.util.ArrayList;
import java.util.UUID;

public class cancelFallDmgListener implements Listener {

    public static final ArrayList<UUID> cancelFallDamage = new ArrayList<>();

    @EventHandler
    public void onPlayerDamaged(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL && cancelFallDamage.contains(e.getEntity().getUniqueId()))
        {
            e.setCancelled(true);
            cancelFallDamage.remove(e.getEntity().getUniqueId());
        }
    }

    public static void addPlayer(Player p) {
        if (cancelFallDamage.contains(p.getUniqueId())) {
            return;
        }
        cancelFallDamage.add(p.getUniqueId());
        removeFallDmgTimer(p);
    }

    private static void removeFallDmgTimer(Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                while (cancelFallDmgListener.cancelFallDamage.contains(p.getUniqueId()))
                {
                    cancelFallDmgListener.cancelFallDamage.remove(p.getUniqueId());
                }
            }
        }.runTaskLater(SoupCore.plugin, 20L * 10L);
    }
}
