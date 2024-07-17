package org.genesismc.SoupCore.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.UUID;

public class cancelFallDmgListener implements Listener {

    public static final ArrayList<UUID> cancelFallDamage = new ArrayList<>();

    @EventHandler
    public void onPlayerDeath(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL && cancelFallDamage.contains(e.getEntity().getUniqueId()))
            {
                e.setCancelled(true);
                cancelFallDamage.remove(e.getEntity().getUniqueId());
            }
        }
    }
}
