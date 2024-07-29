package org.genesismc.SoupCore.listeners.abilities;

import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.genesismc.SoupCore.Database.Database;
import org.genesismc.SoupCore.SoupCore;

import java.util.Objects;

public class AbilityTurbo implements Listener {
    @EventHandler
    public void onPlayerKill(PlayerDeathEvent e)
    {
        Player p = e.getEntity();
        Player killer = p.getKiller();
        if (killer == null)
        {
            return;
        }

        if (!Objects.equals(ChatColor.stripColor(Database.getPlayerData(killer, "soupData", "kit")), "Turbo")) {
            return;
        }
        killer.removePotionEffect(PotionEffectType.SPEED);
        PotionEffect speedFive = new PotionEffect(PotionEffectType.SPEED, 20 * 8, 4);
        PotionEffect regenThree = new PotionEffect(PotionEffectType.REGENERATION, 20 * 8, 2);
        killer.addPotionEffect(speedFive);
        killer.addPotionEffect(regenThree);
        new BukkitRunnable() {
            @Override
            public void run() {
                RegionManager rgManager = SoupCore.getWorldGuard.getRegionManager(p.getWorld());

                if (!Objects.requireNonNull(rgManager.getRegion("pvp")).contains(killer.getLocation().getBlockX(), killer.getLocation().getBlockY(), killer.getLocation().getBlockZ())) {
                    this.cancel();
                    return;
                }

                if (!Objects.equals(ChatColor.stripColor(Database.getPlayerData(killer, "soupData", "kit")), "Turbo"))
                {
                    this.cancel();
                    return;
                }

                killer.removePotionEffect(PotionEffectType.SPEED);
                killer.removePotionEffect(PotionEffectType.REGENERATION);
                PotionEffect speedOne = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0);
                killer.addPotionEffect(speedOne);

            }
        }.runTaskLater(SoupCore.plugin, 20L * 8L);
    }
}
