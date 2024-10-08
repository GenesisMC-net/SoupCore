package org.genesismc.SoupCore.listeners.abilities;

import org.genesismc.SoupCore.SoupCore;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class AbilitySoldier implements Listener {
    public static final HashMap<UUID, Long> iceDomeCooldown = new HashMap<>();

    private static List<Location> circle (Location loc, Integer r, Integer h, Boolean hollow, Boolean sphere, int plus_y) {
        List<Location> circleblocks = new ArrayList<>();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        for (int x = cx - r; x <= cx +r; x++)
            for (int z = cz - r; z <= cz +r; z++)
                for (int y = (sphere ? cy - r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r*r && !(hollow && dist < (r-1)*(r-1))) {
                        Location l = new Location(loc.getWorld(), x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }

        return circleblocks;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e)
    {

        if (!(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            return;
        }
            Player p = e.getPlayer();

            ItemStack itemInHand = p.getItemInHand().clone();

            if (!Objects.equals(itemInHand.getItemMeta().getDisplayName(), ChatColor.DARK_AQUA + "Ice Dome")) {
                return;
            }
        if (iceDomeCooldown.containsKey(p.getUniqueId())) {
            if (System.currentTimeMillis() - iceDomeCooldown.get(p.getUniqueId()) < 35 * 1000) {
                p.sendMessage(ChatColor.RED + "You cannot use this ability for another " + ChatColor.GREEN + Math.round((float) (35 - (System.currentTimeMillis() - iceDomeCooldown.get(p.getUniqueId())) / 1000)) + ChatColor.RED + " seconds!");
                return;
            }
            iceDomeCooldown.remove(p.getUniqueId());
        }
        Cooldowns.addAbilityCooldown(p, iceDomeCooldown, 35, ChatColor.DARK_AQUA + "Ice Dome");

        PotionEffect strengthTwo = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 10, 0);
        p.addPotionEffect(strengthTwo);

        final List<Location> circs = circle(p.getLocation(), 5, 5, true, true, 1);

        for (Location loc : circs) {
            if (loc.getBlock().getType() == null || loc.getBlock().getType() == Material.AIR)
            {
                loc.getBlock().setType(Material.ICE);
            }
        }
        new BukkitRunnable() {
            public void run() {
                for (Location loc : circs) {
                    if (loc.getBlock().getType() == Material.ICE)
                    {
                        loc.getBlock().setType(Material.AIR);
                    }
                }
            }
        }.runTaskLater(SoupCore.plugin, 20L * 10L);
    }
}