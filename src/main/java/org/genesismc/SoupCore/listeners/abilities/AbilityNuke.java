package org.genesismc.SoupCore.listeners.abilities;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.genesismc.SoupCore.SoupCore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AbilityNuke {

    public static final List<Block> fire = new ArrayList<>();

    public static void spawnNuke(Player p) {
        World world = p.getWorld();
        broadcastWorld(world, " ", false);
        broadcastWorld(world, ChatColor.RED + "A player has unlocked a nuke, will go live in" + ChatColor.YELLOW + " 10 " + ChatColor.RED + "seconds!", true);
        broadcastWorld(world, " ", false);

        final int[] i = {0};
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!p.isOnline() || !Objects.equals(p.getWorld().getName(), "world") || SoupCore.playerInSpawn(p)) {
                    broadcastWorld(world, ChatColor.RED + "The nuke has been cancelled!", false);
                }
                if (i[0] >= 5) {
                    int total = 0;
                    p.setNoDamageTicks(20 * 5);

                    lightning(p);
                    surroundFlames(p.getLocation(), 12);
                    for (Entity entity : p.getNearbyEntities(12, 100 ,12)) {
                        if (!(entity instanceof Player)) {
                            entity.remove();
                            continue;
                        }

                        Player target = (Player) entity;
                        if (SoupCore.playerInSpawn(p)) continue;

                        target.getLocation().getBlock().setType(Material.FIRE);
                        fire.add(target.getLocation().getBlock());

                        target.damage(100, p);
                        total ++;
                    }
                    broadcastWorld(world, " ", false);
                    broadcastWorld(world, ChatColor.RED + "The nuke has killed " + ChatColor.YELLOW + total + ChatColor.RED + " players!", false);
                    broadcastWorld(world, " ", false);

                    this.cancel();
                    return;
                }

                broadcastWorld(world, ChatColor.RED + "Nuke goes live in " + ChatColor.YELLOW + (5 - i[0]), false);

                i[0]++;
            }
        }.runTaskTimer(SoupCore.plugin, 20L * 5, 20L);
    }

    private static void broadcastWorld(World world, String msg, boolean sound) {
        for (Player online : world.getPlayers()) {
            online.sendMessage(msg);
            if (sound) {
                online.playSound(online.getLocation(), Sound.NOTE_PLING, 1, 1);
            }
        }
    }

    private static List<Block> getBlocks(Location center, int radius, boolean hollow, boolean sphere) {
        List<Location> locs = circle(center, radius, radius, hollow, sphere, 0);
        List<Block> blocks = new ArrayList<>();

        for (Location loc : locs) {
            blocks.add(loc.getBlock());
        }

        return blocks;
    }

    private static List<Location> circle(Location loc, int radius, int height, boolean hollow, boolean sphere, int plusY) {
        List<Location> circleBlocks = new ArrayList<>();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();

        for (int x = cx - radius; x <= cx + radius; x++) {
            for (int z = cz - radius; z <= cz + radius; z++) {
                for (int y = (sphere ? cy - radius : cy); y < (sphere ? cy + radius : cy + height); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);

                    if (dist < radius * radius && !(hollow && dist < (radius - 1) * (radius - 1))) {
                        Location l = new Location(loc.getWorld(), x, y + plusY, z);
                        circleBlocks.add(l);
                    }
                }
            }
        }

        return circleBlocks;
    }

    private static void surroundFlames(Location l, int r){
        List<Block> ring = getBlocks(l, r, true, false);
        for (Block b : ring){
            b.setType(Material.FIRE);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Block b : ring) {
                    if (b.getType() == Material.FIRE) {
                        b.setType(Material.AIR);
                    }
                }
                for (Block b: fire) {
                    b.setType(Material.AIR);
                }
            }
        }.runTaskLater(SoupCore.plugin, 20L * 5L);
    }

    private static void lightning(Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                p.getWorld().strikeLightningEffect(p.getLocation());
            }
        }.runTaskLater(SoupCore.plugin, 1L);
    }
}
