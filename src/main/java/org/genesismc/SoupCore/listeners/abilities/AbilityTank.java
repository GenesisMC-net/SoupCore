package org.genesismc.SoupCore.listeners.abilities;

import org.genesismc.SoupCore.SoupCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class AbilityTank implements Listener {
    public static final HashMap<UUID, Long> silverFishCooldown = new HashMap<>();
    @EventHandler
    public void onRightClick(PlayerInteractEvent e)
    {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player p = e.getPlayer();

            ItemStack itemInHand = p.getItemInHand();

            if (Objects.equals(itemInHand.getType(), Material.INK_SACK) && Objects.equals(itemInHand.getItemMeta().getDisplayName(), ChatColor.DARK_RED + "Silverfish Army")) {
                boolean cooldownActive = false;
                if (silverFishCooldown.containsKey(p.getUniqueId())) {
                    if (System.currentTimeMillis() - silverFishCooldown.get(p.getUniqueId()) < 45 * 1000) {
                        cooldownActive = true;
                        p.sendMessage(ChatColor.RED + "You cannot use this ability for another " + ChatColor.GREEN + Math.round((float) (45 - (System.currentTimeMillis() - silverFishCooldown.get(p.getUniqueId())) / 1000)) + ChatColor.RED + " seconds!");
                    } else {
                        silverFishCooldown.remove(p.getUniqueId());
                    }
                }

                if (!cooldownActive) {
                    silverFishCooldown.put(p.getUniqueId(), System.currentTimeMillis());

                    Cooldowns.addAbilityCooldown(p, silverFishCooldown, 30, ChatColor.RED + "Silverfish Army");

                    List<Entity> silverfishArmy = new ArrayList<>();
                    for (int i = 0; i < 6; i++) {
                        Entity silverfish = p.getWorld().spawnEntity(p.getLocation(), EntityType.SILVERFISH);
                        silverfish.setCustomName(ChatColor.RED + p.getName());
                        silverfish.setCustomNameVisible(true);
                        silverfishArmy.add(silverfish);
                    }

                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            for (Entity fish: silverfishArmy) {
                                fish.remove();
                            }
                        }
                    }.runTaskLater(SoupCore.plugin, 20L * 15L);
                }
            }
        }
    }

    @EventHandler
    public void onSilverfishDamage(EntityDamageByEntityEvent e)
    {
        if (e.getEntity() instanceof Player && e.getDamager().getType() == EntityType.SILVERFISH)
        {
            Player p = (Player) e.getEntity();

            if (e.getDamager().isCustomNameVisible()) {
                if (Objects.equals(e.getEntity().getCustomName(), ChatColor.RED + p.getName())) {
                    e.setCancelled(true);
                    return;
                }

                Player silverFishOwner = Bukkit.getPlayer(ChatColor.stripColor(e.getDamager().getCustomName()));
                p.damage(3, silverFishOwner);
                e.setDamage(0);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSilverfishTarget(EntityTargetLivingEntityEvent e)
    {
        if (!(e.getEntity().getType() == EntityType.SILVERFISH)) {
            return;
        }

        if (Objects.equals(e.getEntity().getCustomName(), ChatColor.RED + e.getTarget().getName()))
        {
            for (Entity target : e.getEntity().getNearbyEntities(10, 10, 10)) {
                if (target instanceof Player) {
                    if (!target.isDead() && !Objects.equals(e.getEntity().getCustomName(), ChatColor.RED + target.getName())) {
                        e.setTarget(target);
                        return;
                    }
                }
            }
            e.setTarget(null);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        Player p = e.getEntity();
        for (Entity entity : p.getNearbyEntities(10, 10, 10)) {
            if (entity.getType() == EntityType.SILVERFISH && entity.getCustomName().contains(p.getName())) {
                entity.remove();
            }
        }
    }
}