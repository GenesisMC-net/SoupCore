package me.smeo.soupcore.listeners.abilities;

import me.smeo.soupcore.SoupCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class AbilityTank implements Listener {

    HashMap<UUID, Long> silverFishCooldown = new HashMap<>();
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
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            silverFishCooldown.remove(p.getUniqueId());
                            p.sendMessage(ChatColor.GRAY + "You can now use " + ChatColor.DARK_RED + "Silverfish Army");
                        }
                    }.runTaskLaterAsynchronously(SoupCore.plugin, 20L * 45L);

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

                Player silverFishOwner = Bukkit.getPlayer(e.getDamager().getCustomName().replace((CharSequence) ChatColor.RED, ""));
                p.damage(3, silverFishOwner);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSilverfishTarget(EntityTargetLivingEntityEvent e)
    {
        if (e.getEntity().getType() == EntityType.SILVERFISH && Objects.equals(e.getEntity().getCustomName(), ChatColor.RED + e.getTarget().getName()))
        {
            e.setCancelled(true);
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