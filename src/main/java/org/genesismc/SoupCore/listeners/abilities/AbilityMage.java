package org.genesismc.SoupCore.listeners.abilities;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.genesismc.SoupCore.Kits.Methods_Kits;
import org.genesismc.SoupCore.SoupCore;
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
import org.genesismc.SoupCore.listeners.cancelFallDmgListener;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

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
        }.runTaskLater(SoupCore.plugin, 20L * 5L);
    }


    @EventHandler
    public void onRightClick(PlayerInteractEvent e)
    {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player p = e.getPlayer();
            for (ProtectedRegion rg : WGBukkit.getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation())){
                if (Objects.equals(rg.getId(), "spawn")) { return; }
            }

            if (!Objects.equals(ChatColor.stripColor(Methods_Kits.getActiveKit(p)), "Mage")) {
                return;
            }

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

                        Vector projVelocity = p.getEyeLocation().clone().getDirection().multiply(new Vector(2, 2.3, 2));
                        projectile.setVelocity(projVelocity);

                        final int[] i = {0};
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                i[0] ++;
                                if (!projectile.getLocation().subtract(new Vector(0, 0.1, 0)).getBlock().getType().equals(Material.AIR)) {
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
                                            if (Math.round(projectile.getLocation().getY()) - 1.5 <= Math.round(projectile.getLocation().getY())) {
                                                Location targetLocation = target.getLocation().clone();

                                                PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, 20 * 5, 2, true, false);
                                                target.addPotionEffect(slowness);
                                                makeWaterGrid(targetLocation);
                                                target.damage(0.1, p);
                                                target.setVelocity(new Vector(0, 0, 0));

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

                    p.setVelocity(p.getEyeLocation().getDirection().multiply(new Vector(12, 6, 12)));
                    p.playSound(p.getLocation(), Sound.WITHER_SHOOT, 1.2F, 0.0F);

                    p.getWorld().playEffect(p.getLocation(), Effect.FLAME, 1, 1);
                    final int[] i = {0};
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (i[0] > 20 * 5 || p.isOnGround()) {
                                this.cancel();
                                return;
                            }

                            p.getWorld().playEffect(p.getLocation(), Effect.FLAME, 1, 1);

                            for (Entity entity : p.getNearbyEntities(2, 10, 2)) {
                                if (entity instanceof Player) {
                                    Player target = (Player) entity;
                                    if (target.getLocation().getY() < p.getLocation().getY()) {
                                        target.damage(0.1, p);
                                        target.setFireTicks(20 * 4);
                                    }
                                }
                            }

                            i[0] += 1;
                        }
                    }.runTaskTimerAsynchronously(SoupCore.plugin, 5L, 1L);

                    fireLaunchCooldown.put(p.getUniqueId(), System.currentTimeMillis());

                    Cooldowns.addAbilityCooldown(p, fireLaunchCooldown, 15, ChatColor.RED + "Fire Jump");

                    cancelFallDmgListener.cancelFallDamage.add(p.getUniqueId());

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
        }
    }
}

