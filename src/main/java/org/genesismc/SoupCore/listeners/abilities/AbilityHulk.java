package org.genesismc.SoupCore.listeners.abilities;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.genesismc.SoupCore.Kits.Methods_Kits;
import org.genesismc.SoupCore.SoupCore;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.genesismc.SoupCore.listeners.cancelFallDmgListener;

import java.util.*;

public class AbilityHulk implements Listener {

    public static final HashMap<UUID, Long> hulkSmashCooldown = new HashMap<>();
    private final ArrayList<UUID> activeAbility = new ArrayList<>();

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
            Player p = ((Player) e.getEntity()).getPlayer();
            // Fall Damage from Hulk (activate ability)
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL && activeAbility.contains(p.getUniqueId()))
            {
                if (!Objects.equals(ChatColor.stripColor(Methods_Kits.getActiveKit(p)), "Hulk")) {
                    return;
                }

                e.setCancelled(true);
                cancelFallDmgListener.cancelFallDamage.remove(p.getUniqueId());
                activeAbility.remove(p.getUniqueId());

                List<Entity> nearbyPlayers = p.getNearbyEntities(4, 4, 4);
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
    public void onShift(PlayerToggleSneakEvent e) {
        if (!e.isSneaking()) { return; }
        Player p = e.getPlayer();
        if (!Objects.equals(ChatColor.stripColor(Methods_Kits.getActiveKit(p)), "Hulk")) {
            return;
        }
        if (p.isOnGround()) { return; }
        for (ProtectedRegion rg : WGBukkit.getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation())){
            if (Objects.equals(rg.getId(), "spawn")) { return; }
        }
        if (p.getLocation().subtract(new Vector(0, 2, 0)).getBlock().getType() != Material.AIR) { return; }

        Vector velocity = p.getVelocity().setY(-2);
        p.setVelocity(velocity);

        p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2F, 1F);}

    @EventHandler
    public void onRightClick(PlayerInteractEvent e)
    {
        if (!(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            return;
        }
        Player p = e.getPlayer();

        ItemStack itemInHand = p.getItemInHand();

        if (!Objects.equals(itemInHand.getItemMeta().getDisplayName(), ChatColor.DARK_GREEN + "Hulk Smash")) {
            return;
        }
        if (hulkSmashCooldown.containsKey(p.getUniqueId())) {
            if (System.currentTimeMillis() - hulkSmashCooldown.get(p.getUniqueId()) < 30 * 1000) {
                p.sendMessage(ChatColor.RED + "You cannot use this ability for another " + ChatColor.GREEN + Math.round((float) (30 - (System.currentTimeMillis() - hulkSmashCooldown.get(p.getUniqueId())) / 1000)) + ChatColor.RED + " seconds!");
                return;
            }
            hulkSmashCooldown.remove(p.getUniqueId());
        }

        Cooldowns.addAbilityCooldown(p, hulkSmashCooldown, 30, ChatColor.DARK_GREEN + "Hulk Smash");

        p.setVelocity(new Vector(0, 1.5, 0));
        cancelFallDmgListener.addPlayer(p);
        activeAbility.add(p.getUniqueId());
    }
}
