package org.genesismc.SoupCore.listeners.abilities;

import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.genesismc.SoupCore.SoupCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        if (!(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            return;
        }
        Player p = e.getPlayer();

        ItemStack itemInHand = p.getItemInHand();

        if (!Objects.equals(itemInHand.getItemMeta().getDisplayName(), ChatColor.DARK_RED + "Silverfish Army")) {
            return;
        }
        if (silverFishCooldown.containsKey(p.getUniqueId())) {
            if (System.currentTimeMillis() - silverFishCooldown.get(p.getUniqueId()) < 45 * 1000) {
                p.sendMessage(ChatColor.RED + "You cannot use this ability for another " + ChatColor.GREEN + Math.round((float) (45 - (System.currentTimeMillis() - silverFishCooldown.get(p.getUniqueId())) / 1000)) + ChatColor.RED + " seconds!");
                return;
            }
            silverFishCooldown.remove(p.getUniqueId());
        }
        Cooldowns.addAbilityCooldown(p, silverFishCooldown, 30, ChatColor.RED + "Silverfish Army");
        PotionEffect speedTwo = new PotionEffect(PotionEffectType.SPEED, 20 * 15, 1, false, false);

        List<Entity> silverfishArmy = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            LivingEntity silverfish = (LivingEntity) p.getWorld().spawnEntity(p.getLocation(), EntityType.SILVERFISH);
            silverfish.setCustomName(ChatColor.RED + p.getName());
            silverfish.setCustomNameVisible(true);
            silverfish.addPotionEffect(speedTwo);
            silverfishArmy.add(silverfish);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Entity fish : silverfishArmy) {
                    fish.remove();
                }
            }
        }.runTaskLater(SoupCore.plugin, 20L * 15L);
    }

    @EventHandler
    public void onSilverfishDamage(EntityDamageByEntityEvent e)
    {
        if (!(e.getEntity() instanceof Player)) { return; }
        if (!e.getDamager().getType().equals(EntityType.SILVERFISH)) { return; }
        if (!e.getDamager().isCustomNameVisible()) { return; } // Normal Silverfish

        Player p = (Player) e.getEntity();

        if (Objects.equals(e.getEntity().getCustomName(), ChatColor.RED + p.getName())) {
            e.setCancelled(true);
            return;
        }

        Player silverFishOwner = Bukkit.getPlayer(ChatColor.stripColor(e.getDamager().getCustomName()));
        e.setCancelled(true);
        e.setDamage(0);
        if ((p.getHealth() - 3) <= 0) {
            p.damage(3, silverFishOwner);
        } else {
            p.damage(0, silverFishOwner);
            p.setHealth(p.getHealth() - 3);
        }
    }

    @EventHandler
    public void onSilverfishTarget(EntityTargetLivingEntityEvent e)
    {
        if (!(e.getEntity().getType() == EntityType.SILVERFISH)) {
            return;
        }

        if (e.getTarget() == null || Objects.equals(e.getEntity().getCustomName(), ChatColor.RED + e.getTarget().getName())) // Targeting owner
        {
            for (Entity target : e.getEntity().getNearbyEntities(10, 10, 10)) { // Target another player
                if (!target.isDead() && !Objects.equals(e.getEntity().getCustomName(), ChatColor.RED + target.getName())) {
                    e.setTarget(target);
                    return;
                }
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        Player p = e.getEntity();
        for (Entity entity : Bukkit.getWorld("world").getEntitiesByClass(Silverfish.class)) {
            if (entity.getCustomName().contains(p.getName())) {
                entity.remove();
            }
        }
    }
}