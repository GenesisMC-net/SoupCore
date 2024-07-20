package org.genesismc.SoupCore.listeners.abilities;

import jdk.vm.ci.meta.Local;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.genesismc.SoupCore.Database.Database;
import org.genesismc.SoupCore.Kits.KitSwitcher;
import org.genesismc.SoupCore.SoupCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class AbilitySwitcher implements Listener {
    public static final HashMap<UUID, Long> switcherCooldown = new HashMap<>();
    private final HashMap<UUID, UUID> thrownSnowballs = new HashMap<>(); // <SnowballUUID, PlayerUUID>

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        switcherCooldown.remove(e.getEntity().getPlayer().getUniqueId());
        if(e.getEntity().getPlayer().getKiller() == null){return;}
        if (Objects.requireNonNull(Database.getPlayerData(e.getEntity().getPlayer().getKiller(), "soupData", "kit")).contains("Switcher"))
        {
            ItemStack snowball = KitSwitcher.getAbilityItem(1);
            PlayerInventory inv = e.getEntity().getPlayer().getKiller().getInventory();
            if (inv.contains(Material.SNOW_BALL) || inv.contains((ItemStack) null)) {
                inv.addItem(snowball);
            } else {
                inv.setItem(1, snowball);
            }
        }
    }

    @EventHandler
    public void onThrow(ProjectileLaunchEvent e)
    {
        if (!(e.getEntity() instanceof Snowball)) { return; }
        if (!(e.getEntity().getShooter() instanceof Player)) { return; }

        Player p = (Player) e.getEntity().getShooter();

        if (!(Objects.requireNonNull(Database.getPlayerData(p, "soupData", "kit")).contains("Switcher"))) { return; }

        boolean cooldownActive = false;
        if (switcherCooldown.containsKey(p.getUniqueId())) {
            if (System.currentTimeMillis() - switcherCooldown.get(p.getUniqueId()) < 10 * 1000) {
                cooldownActive = true;
                p.sendMessage(ChatColor.RED + "You cannot use this ability for another " + ChatColor.GREEN + Math.round((float) (10 - (System.currentTimeMillis() - switcherCooldown.get(p.getUniqueId())) / 1000)) + ChatColor.RED + " seconds!");

                e.setCancelled(true);
                e.getEntity().remove();

                ItemStack snowball = KitSwitcher.getAbilityItem(1);
                PlayerInventory inv = p.getInventory();
                if (inv.contains(Material.SNOW_BALL) || inv.contains((ItemStack) null)) {
                    inv.addItem(snowball);
                } else {
                    inv.setItem(1, snowball);
                }
            } else {
                switcherCooldown.remove(p.getUniqueId());
            }
        }

        if (!cooldownActive) {
            switcherCooldown.put(p.getUniqueId(), System.currentTimeMillis());
            thrownSnowballs.put(e.getEntity().getUniqueId(), p.getUniqueId());

            Cooldowns.addAbilityCooldown(p, switcherCooldown, 10, ChatColor.AQUA + "Switcher Ball");
        }
    }

    @EventHandler
    public void onSnowballHit(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) { return; }
        if (!(e.getDamager() instanceof Snowball)) { return; }

        Snowball snowball = (Snowball) e.getDamager();
        if (!(snowball.getShooter() instanceof Player)) { return; }

        Player shooter = (Player) snowball.getShooter();
        if (!(thrownSnowballs.containsValue(shooter.getUniqueId()))) { return; }

        Player target = (Player) e.getEntity();

        Location shooterLocation = shooter.getLocation().clone();
        Location targetLocation = target.getLocation().clone();

        shooter.teleport(targetLocation);
        target.teleport(shooterLocation);

        shooter.playSound(shooter.getLocation(), Sound.ENDERMAN_TELEPORT, 1F, 1F);
        target.playSound(target.getLocation(), Sound.ENDERMAN_TELEPORT, 1F, 1F);

        shooter.sendMessage(ChatColor.YELLOW + "Switch!" + ChatColor.GRAY + " You have switched positions with " + ChatColor.RED + target.getDisplayName());
        target.sendMessage(ChatColor.YELLOW + "Switch!" + ChatColor.GRAY + " You have switched positions with " + ChatColor.RED + shooter.getDisplayName());

        thrownSnowballs.remove(e.getDamager().getUniqueId());
    }

    @EventHandler
    public void onSnowballLand(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Snowball)) { return; }
        thrownSnowballs.remove(e.getEntity().getUniqueId());
    }
}
