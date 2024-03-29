package me.smeo.soupcore.listeners.abilities;

import me.smeo.soupcore.SoupCore;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class AbilityHulk implements Listener {

    HashMap<UUID, Long> hulkSmashCooldown = new HashMap<>();
    ArrayList<UUID> cancelFallDamage = new ArrayList<>();
    ArrayList<UUID> cancelHulkSmashDamage = new ArrayList<>();

    @EventHandler
    public void onTntDamage(EntityDamageEvent e)
    {
        if (e.getEntity() instanceof Player) {
            // Cancel TNT damage
            if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
            {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent e)
    {
        if (e.getEntity() instanceof Player) {
            // Fall Damage from Hulk (activate ability)
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL && cancelFallDamage.contains(e.getEntity().getUniqueId()))
            {

                e.setCancelled(true);
                cancelFallDamage.remove(e.getEntity().getUniqueId());

                Player p = ((Player) e.getEntity()).getPlayer();

                List<Entity> nearbyPlayers = p.getNearbyEntities(3, 3, 3);
                for (Entity entity : nearbyPlayers) {
                    if (entity instanceof Player)
                    {
                        if (((Player) entity).getPlayer().getUniqueId() != p.getUniqueId())
                        {
                            ((Player) entity).damage(10, p);
                        }
                    }
                }

                TNTPrimed tnt = (TNTPrimed) p.getWorld().spawnEntity(p.getLocation(), EntityType.PRIMED_TNT);
                tnt.setFuseTicks(1);
            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e)
    {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player p = e.getPlayer();

            ItemStack itemInHand = p.getItemInHand();

            if (Objects.equals(itemInHand.getType(), Material.ANVIL) && Objects.equals(itemInHand.getItemMeta().getDisplayName(), ChatColor.DARK_GREEN + "Hulk Smash")) {
                boolean cooldownActive = false;
                if (hulkSmashCooldown.containsKey(p.getUniqueId())) {
                    if (System.currentTimeMillis() - hulkSmashCooldown.get(p.getUniqueId()) < 30 * 1000) {
                        cooldownActive = true;
                        p.sendMessage(ChatColor.RED + "You cannot use this ability for another " + ChatColor.GREEN + Math.round((float) (30 - (System.currentTimeMillis() - hulkSmashCooldown.get(p.getUniqueId())) / 1000)) + ChatColor.RED + " seconds!");
                    } else {
                        hulkSmashCooldown.remove(p.getUniqueId());
                    }
                }

                if (!cooldownActive) {
                    hulkSmashCooldown.put(p.getUniqueId(), System.currentTimeMillis());
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            hulkSmashCooldown.remove(p.getUniqueId());
                            p.sendMessage(ChatColor.GRAY + "You can now use " + ChatColor.DARK_GREEN + "Hulk Smash");
                        }
                    }.runTaskLaterAsynchronously(SoupCore.plugin, 20L * 30L);

                    p.setVelocity(new Vector(0, 1.5, 0));
                    cancelFallDamage.add(p.getUniqueId());
                    cancelHulkSmashDamage.add(p.getUniqueId());

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            cancelFallDamage.remove(p.getUniqueId());
                            cancelHulkSmashDamage.remove(p.getUniqueId());
                        }
                    }.runTaskLaterAsynchronously(SoupCore.plugin, 20L * 10L);
                }
            }
        }
    }
}
