package me.smeo.soupcore.listeners;

import me.smeo.soupcore.SoupCore;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.UUID;

public class SpawnLaunchListener implements Listener {

    ArrayList<UUID> cancelFallDamage = new ArrayList<>();

    @EventHandler
    public void onTntDamage(EntityDamageEvent e)
    {
        if (e.getEntity() instanceof Player) {
            // Cancel TNT damage
            if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
            {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL && cancelFallDamage.contains(e.getEntity().getUniqueId())) {
                e.setCancelled(true);
                cancelFallDamage.remove(e.getEntity().getUniqueId());
            }
        }
    }

    @EventHandler
    public void onPressurePlate(PlayerMoveEvent e)
    {
        Player p = e.getPlayer();
        if(p.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.WOOL)
        {
            p.playSound(p.getLocation(), Sound.EXPLODE, 8, 1);
            p.setVelocity(new Vector(1 + p.getLocation().getDirection().getX() * 6, 2, 1 + p.getLocation().getDirection().getZ() * 6));
            cancelFallDamage.add(p.getUniqueId());
            new BukkitRunnable() {
                @Override
                public void run() {
                    cancelFallDamage.remove(p.getUniqueId());
                }
            }.runTaskLaterAsynchronously(SoupCore.plugin, 20L * 10L);
        }
    }
}

