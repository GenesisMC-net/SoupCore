package org.genesismc.SoupCore.listeners.abilities;

import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class AbilitySpiderwebs implements Listener {
    public static final HashMap<UUID, Long> spiderWebCooldown = new HashMap<>();

    private void makeWebGrid(Location targetLocation) {
        for (int j = -1; j < 2; j++) {
            for (int k = -1; k < 2; k++) {
                Location waterGridCell = targetLocation.clone().getBlock().getLocation().add(j, 0, k);

                if (waterGridCell.getBlock().getType().equals(Material.AIR)) {
                    waterGridCell.getBlock().setType(Material.WEB);
                }
            }
        }

        deleteWebGrid(targetLocation);
    }

    private void deleteWebGrid(Location targetLocation)
    {
        new BukkitRunnable() {
            @Override
            public void run() {

                for (int j = -1; j < 2; j++) {
                    for (int k = -1; k < 2; k++) {
                        Location waterGridCell = targetLocation.getBlock().getLocation().clone().add(j, 0, k);

                        if (waterGridCell.getBlock().getType().equals(Material.WEB)) {
                            waterGridCell.getBlock().setType(Material.AIR);
                        }
                    }
                }
            }
        }.runTaskLater(SoupCore.plugin, 20L * 7L);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e)
    {
        if (!(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            return;
        }
        Player p = e.getPlayer();

        ItemStack itemInHand = p.getItemInHand().clone();

        if (!Objects.equals(itemInHand.getItemMeta().getDisplayName(), (ChatColor.WHITE + "Spider Webs"))) {
            return;
        }
        if (spiderWebCooldown.containsKey(p.getUniqueId())) {
            if (System.currentTimeMillis() - spiderWebCooldown.get(p.getUniqueId()) < 30 * 1000) {
                p.sendMessage(ChatColor.RED + "You cannot use this ability for another " + ChatColor.GREEN + Math.round((float) (30 - (System.currentTimeMillis() - spiderWebCooldown.get(p.getUniqueId())) / 1000)) + ChatColor.RED + " seconds!");
                return;
            }
            spiderWebCooldown.remove(p.getUniqueId());
        }

        Cooldowns.addAbilityCooldown(p, spiderWebCooldown, 30, ChatColor.RED + "Web Attack");
        p.playSound(p.getLocation(), Sound.SPIDER_IDLE, 1F, 1F);

        Location playerLocation = p.getLocation();

        ArmorStand projectile = p.getWorld().spawn(playerLocation, ArmorStand.class);
        projectile.setVisible(false);
        projectile.setSmall(true);
        projectile.setHelmet(new ItemStack(Material.WEB));

        Vector projVelocity = p.getEyeLocation().clone().getDirection().multiply(new Vector(2, 2.3, 2));
        projectile.setVelocity(projVelocity);

        final Location[] lastPos = {projectile.getLocation()};
        final int[] i = {0};
        new BukkitRunnable() {
            @Override
            public void run() {
                i[0]++;
                if (!projectile.getLocation().getBlock().getType().equals(Material.AIR)) {
                    if (!projectile.getNearbyEntities(3, 3, 3).contains(p)) {
                        Location targetLocation = projectile.getLocation().clone();

                        makeWebGrid(targetLocation);
                        projectile.remove();
                        this.cancel();
                        return;
                    }
                }

                for (Entity entity : projectile.getNearbyEntities(0.6, 0.6, 0.6)) {
                    if (entity instanceof Player) {
                        Player target = (Player) entity;
                        if (!target.getUniqueId().equals(p.getUniqueId())) {
                            if (Math.round(projectile.getLocation().getY()) - 1.5 <= Math.round(projectile.getLocation().getY())) {
                                Location targetLocation = target.getLocation().clone();

                                makeWebGrid(targetLocation);
                                target.damage(0.1, p);
                                target.setVelocity(new Vector(0, 0, 0));
                                p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 1F, 1F);

                                projectile.remove();
                                this.cancel();
                                return;
                            }
                        }
                    }
                }

                projectile.setVelocity(projVelocity);

                if (projectile.getLocation() == lastPos[0]) {
                    projectile.remove();
                    this.cancel();
                }
                lastPos[0] = projectile.getLocation();

                if (i[0] >= 20L * 7L) {
                    projectile.remove();
                    this.cancel();
                }
            }
        }.runTaskTimer(SoupCore.plugin, 0L, 1L);
    }
}

