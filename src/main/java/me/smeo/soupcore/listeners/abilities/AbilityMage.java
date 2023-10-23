package me.smeo.soupcore.listeners.abilities;

import me.smeo.soupcore.SoupCore;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class AbilityMage implements Listener {

    HashMap<UUID, Long> waterAbilityCooldown = new HashMap<>();
    HashMap<UUID, Long> fireLaunchCooldown = new HashMap<>();
    ArrayList<UUID> cancelFallDamage = new ArrayList<>();

    // Stop water from flowing
    @EventHandler
    public void onWaterFlow(BlockFromToEvent e)
    {
        int id = e.getBlock().getTypeId();
        if(id == 8 || id == 9) {
            e.setCancelled(true);
        }
    }

    // Cancel fall damage
    @EventHandler
    public void onFallDamage(EntityDamageEvent e)
    {
        if (e.getEntity() instanceof Player) {
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL && cancelFallDamage.contains(e.getEntity().getUniqueId()))
            {
                e.setCancelled(true);
                cancelFallDamage.remove(e.getEntity().getUniqueId());
            }
        }
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

                        Location playerLocation = p.getLocation();
                        playerLocation.setY(playerLocation.getY() + 1);
                        Vector playerDirection = playerLocation.clone().getDirection();
                        playerDirection.setY(0);
                        Vector directionIncrement = playerDirection.clone();

                        final int[] i = {0};
                        final Vector[] waterDirection = {playerDirection.clone()};
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                i[0] = i[0] + 1;
                                Location newLocation = playerLocation.clone().add(waterDirection[0]);
                                if (p.getWorld().getBlockAt(newLocation).getType() != Material.AIR) {
                                    for (int j = -1; j < 2; j++) {
                                        for (int k = -1; k < 2; k++) {
                                            Location waterGridCell = newLocation.clone().getBlock().getLocation().add(j, 1, k);
                                            if (waterGridCell.getBlock().getType().equals(Material.AIR)) {
                                                waterGridCell.getBlock().setType(Material.WATER);
                                                waterGridCell.getBlock().setData((byte) 10);
                                                waterGridCell.getBlock().getState().update();
                                            }
                                        }
                                    }

                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            for (int j = -1; j < 2; j++) {
                                                for (int k = -1; k < 2; k++) {
                                                    Location waterGridCell = newLocation.clone().getBlock().getLocation().add(j, 1, k);
                                                    if (waterGridCell.getBlock().getType().equals(Material.WATER)) {
                                                        waterGridCell.getBlock().setType(Material.AIR);
                                                    }
                                                }
                                            }
                                        }
                                    }.runTaskLater(SoupCore.plugin, 20L * 3L);
                                    i[0] = 10;
                                    this.cancel();
                                    return;
                                } else {
                                    for (Player player : p.getWorld().getPlayers()) {
                                        if (player.getUniqueId() != p.getUniqueId()) {
                                            Location targetPlayerLoc = player.getLocation().clone();
                                            if (targetPlayerLoc.getBlock().getLocation().equals(newLocation.getBlock().getLocation()) || targetPlayerLoc.getBlock().getLocation().equals(newLocation.clone().subtract(new Vector(0, 1, 0)).getBlock().getLocation())) {
                                                for (int j = -1; j < 2; j++) {
                                                    for (int k = -1; k < 2; k++) {
                                                        Location waterGridCell = targetPlayerLoc.clone().add(j, 1, k);
                                                        if (waterGridCell.getBlock().getType().equals(Material.AIR)) {
                                                            waterGridCell.getBlock().setType(Material.WATER);
                                                            waterGridCell.getBlock().setData((byte) 10);
                                                            waterGridCell.getBlock().getState().update();
                                                        }
                                                    }
                                                }

                                                new BukkitRunnable() {
                                                    @Override
                                                    public void run() {
                                                        for (int j = -1; j < 2; j++) {
                                                            for (int k = -1; k < 2; k++) {
                                                                Location waterGridCell = targetPlayerLoc.clone().add(j, 1, k);
                                                                if (waterGridCell.getBlock().getType().equals(Material.WATER)) {
                                                                    waterGridCell.getBlock().setType(Material.AIR);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }.runTaskLater(SoupCore.plugin, 20L * 3L);
                                                i[0] = 10;
                                                this.cancel();
                                                return;
                                            }
                                        }
                                    }
                                    newLocation.getBlock().setType(Material.WATER);
                                    newLocation.getBlock().setData((byte) 10);
                                    newLocation.getBlock().getState().update();
                                }

                                waterDirection[0] = waterDirection[0].add(directionIncrement);

                                Location oldLocation = newLocation.subtract(directionIncrement);
                                if (p.getWorld().getBlockAt(oldLocation).getType() == Material.WATER) {
                                    p.getWorld().getBlockAt(oldLocation).setType(Material.AIR);
                                }

                                if (i[0] >= 20) {
                                    this.cancel();
                                    return;
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
                        p.setVelocity(new Vector(p.getEyeLocation().getDirection().getX() * 10, (double) 1.5, p.getEyeLocation().getDirection().getZ() * 10));
                        fireLaunchCooldown.put(p.getUniqueId(), System.currentTimeMillis());

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
