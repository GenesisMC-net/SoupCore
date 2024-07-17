package me.smeo.soupcore.listeners.abilities;

import me.smeo.soupcore.SoupCore;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import static me.smeo.soupcore.listeners.cancelFallDmgListener.cancelFallDamage;

public class AbilityMage implements Listener {

    public static final HashMap<UUID, Long> waterAbilityCooldown = new HashMap<>();
    public static final HashMap<UUID, Long> fireLaunchCooldown = new HashMap<>();

    // Stop water from flowing
    @EventHandler
    public void onWaterFlow(BlockFromToEvent e)
    {
        int id = e.getBlock().getTypeId();
        if(id == 8 || id == 9) {
            e.setCancelled(true);
        }
    }

    private void makeWaterGrid(Location targetLocation) {
        for (int j = -1; j < 2; j++) {
            for (int k = -1; k < 2; k++) {
                Location waterGridCell = targetLocation.clone().getBlock().getLocation().add(j, 0, k);

                if (waterGridCell.getBlock().getType().equals(Material.AIR)) {
                    waterGridCell.getBlock().setType(Material.WATER);
                }
            }
        }

        deleteWaterGrid(targetLocation);
    }

    private void deleteWaterGrid(Location targetLocation)
    {
        new BukkitRunnable() {
            @Override
            public void run() {

                for (int j = -1; j < 2; j++) {
                    for (int k = -1; k < 2; k++) {
                        Location waterGridCell = targetLocation.getBlock().getLocation().clone().add(j, 0, k);

                        if (waterGridCell.getBlock().getType().equals(Material.WATER) || waterGridCell.getBlock().getType().equals(Material.STATIONARY_WATER)) {
                            waterGridCell.getBlock().setType(Material.AIR);
                        }
                    }
                }
            }
        }.runTaskLater(SoupCore.plugin, 20L * 3L);
    }


    @EventHandler
    public void onRightClick(PlayerInteractEvent e)
    {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player p = e.getPlayer();

            // Water ability
            if (!p.isSneaking())
            {
                ItemStack itemInHand = p.getItemInHand();

                if (Objects.equals(itemInHand.getType(), Material.INK_SACK) && Objects.equals(itemInHand.getItemMeta().getDisplayName(), ChatColor.BLUE + "Mage Abilities")) {
                    boolean cooldownActive = false;
                    if (waterAbilityCooldown.containsKey(p.getUniqueId())) {
                        if (System.currentTimeMillis() - waterAbilityCooldown.get(p.getUniqueId()) < 10 * 1000) {
                            cooldownActive = true;
                            p.sendMessage(ChatColor.RED + "You cannot use this ability for another " + ChatColor.GREEN + Math.round((float) (10 - (System.currentTimeMillis() - waterAbilityCooldown.get(p.getUniqueId())) / 1000)) + ChatColor.RED + " seconds!");
                        } else {
                            waterAbilityCooldown.remove(p.getUniqueId());
                        }
                    }

                    if (!cooldownActive) {
                        waterAbilityCooldown.put(p.getUniqueId(), System.currentTimeMillis());

                        Cooldowns.addAbilityCooldown(p, waterAbilityCooldown, 10, ChatColor.BLUE + "Water Attack");

                        Location playerLocation = p.getLocation();

                        ArmorStand projectile = p.getWorld().spawn(playerLocation, ArmorStand.class);

                        projectile.setVisible(false);
                        projectile.setSmall(true);
                        projectile.setHelmet(new ItemStack(Material.PACKED_ICE));

                        Vector projVelocity = p.getEyeLocation().clone().getDirection().multiply(new Vector(2, 2, 2));
                        projectile.setVelocity(projVelocity);

                        final int[] i = {0};
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                i[0] ++;
                                if (projectile.isOnGround()) {
                                    if (!projectile.getNearbyEntities(3,3,3).contains(p)) {
                                        Location targetLocation = projectile.getLocation().clone();

                                        makeWaterGrid(targetLocation);
                                        projectile.remove();
                                        this.cancel();
                                        return;
                                    }
                                }

                                for (Entity entity : projectile.getNearbyEntities(0.6, 0.6, 0.6)) {
                                    if (entity instanceof Player) {
                                        Player target = (Player) entity;
                                        if (!target.getUniqueId().equals(p.getUniqueId())) {
                                            target.damage(0.1, p);
                                            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5, 2, false));
                                            if (projectile.getNearbyEntities(0.6, 0.6, 0.6).contains(target) && Math.round(projectile.getLocation().getY()) - 1.5 <= Math.round(projectile.getLocation().getY())) {
                                                Location targetLocation = target.getLocation().clone();

                                                makeWaterGrid(targetLocation);

                                                projectile.remove();
                                                this.cancel();
                                                return;
                                            }
                                        }
                                    }
                                }

                                projectile.setVelocity(projVelocity);

                                if (i[0] >= 20L * 5L) {
                                    projectile.remove();
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(SoupCore.plugin, 0L, 1L);
                    }
                }
            } else
            // Fire Launch Ability
            {
                ItemStack itemInHand = p.getItemInHand();

                if (Objects.equals(itemInHand.getType(), Material.INK_SACK) && Objects.equals(itemInHand.getItemMeta().getDisplayName(), ChatColor.BLUE + "Mage Abilities")) {

                    boolean cooldownActive = false;
                    if (fireLaunchCooldown.containsKey(p.getUniqueId())) {
                        if (System.currentTimeMillis() - fireLaunchCooldown.get(p.getUniqueId()) < 15 * 1000) {
                            cooldownActive = true;
                            p.sendMessage(ChatColor.RED + "You cannot use this ability for another " + ChatColor.GREEN + Math.round((float) (15 - (System.currentTimeMillis() - fireLaunchCooldown.get(p.getUniqueId())) / 1000)) + ChatColor.RED + " seconds!");
                        } else {
                            fireLaunchCooldown.remove(p.getUniqueId());
                        }
                    }

                    if (!cooldownActive) {

                        if (!p.isOnGround()) {
                            p.sendMessage(ChatColor.RED + "You must be on the ground to use this ability!");
                            return;
                        }

                        p.setVelocity(new Vector(p.getEyeLocation().getDirection().getX() * 15, 1, p.getEyeLocation().getDirection().getZ() * 15));
                        p.playSound(p.getLocation(), Sound.WITHER_SHOOT, 1.2F, 0.0F);

                        p.getWorld().playEffect(p.getLocation(), Effect.FLAME, 0);
                        final int[] i = {0};
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (i[0] > 40) {
                                    this.cancel();
                                }

                                p.getWorld().playEffect(p.getLocation(), Effect.FLAME, 0);

                                i[0] += 1;
                            }
                        }.runTaskTimerAsynchronously(SoupCore.plugin, 5L, 2L);

                        fireLaunchCooldown.put(p.getUniqueId(), System.currentTimeMillis());

                        Cooldowns.addAbilityCooldown(p, fireLaunchCooldown, 15, ChatColor.RED + "Fire Jump");

                        cancelFallDamage.add(p.getUniqueId());

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                while (cancelFallDamage.contains(p.getUniqueId()))
                                {
                                    cancelFallDamage.remove(p.getUniqueId());
                                }
                            }
                        }.runTaskLater(SoupCore.plugin, 20L * 10L);
                    }
                }
            }
        }
    }
}

