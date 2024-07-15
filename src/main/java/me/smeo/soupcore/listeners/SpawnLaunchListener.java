package me.smeo.soupcore.listeners;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.smeo.soupcore.SoupCore;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
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
    public void onFallDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL && cancelFallDamage.contains(e.getEntity().getUniqueId())) {
                e.setCancelled(true);
                cancelFallDamage.remove(e.getEntity().getUniqueId());
            }
        }
    }

    @EventHandler
    public void onLaunchBlock(PlayerMoveEvent e)
    {
        Player p = e.getPlayer();

        if (cancelFallDamage.contains(p.getUniqueId())) {
            return;
        }

        RegionContainer container = SoupCore.getWorldGuard.getRegionContainer();
        RegionManager regions = container.get(p.getWorld());
        assert regions != null;
        ProtectedRegion region = regions.getRegion("spawn");

        Location loc = p.getLocation();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(loc);

        if (!set.getRegions().contains(region)) {
            return;
        }
        if(p.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.WOOL) {
            return;
        }

        p.playSound(p.getLocation(), Sound.EXPLODE, 1.5F, 1F);
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