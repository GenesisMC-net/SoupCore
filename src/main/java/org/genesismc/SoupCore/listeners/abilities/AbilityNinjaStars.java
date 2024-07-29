package org.genesismc.SoupCore.listeners.abilities;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.genesismc.SoupCore.Database.Database;
import org.genesismc.SoupCore.Kits.KitStealth;
import org.genesismc.SoupCore.SoupCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class AbilityNinjaStars implements Listener {
    public static final HashMap<UUID, Long> ninjaStarCooldown = new HashMap<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        ninjaStarCooldown.remove(e.getEntity().getPlayer().getUniqueId());
        if(e.getEntity().getPlayer().getKiller() == null){return;}
        if (Objects.requireNonNull(Database.getPlayerData(e.getEntity().getPlayer().getKiller(), "soupData", "kit")).contains("Stealth"))
        {
            ItemStack ninjaStar = KitStealth.getAbilityItem(1);
            PlayerInventory inv = e.getEntity().getPlayer().getKiller().getInventory();
            if (inv.contains(ninjaStar) || inv.contains((ItemStack) null)) {
                inv.addItem(ninjaStar);
            } else {
                inv.setItem(1, ninjaStar);
            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e)
    {
        if (!(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            return;
        }

        Player p = e.getPlayer();

        ItemStack itemInHand = p.getItemInHand().clone();

        if (!Objects.equals(itemInHand.getItemMeta().getDisplayName(), ChatColor.LIGHT_PURPLE + "Ninja Star")) {
            return;
        }

        if (ninjaStarCooldown.containsKey(p.getUniqueId())) {
            if (System.currentTimeMillis() - ninjaStarCooldown.get(p.getUniqueId()) < 10 * 1000) {
                p.sendMessage(ChatColor.RED + "You cannot use this ability for another " + ChatColor.GREEN + Math.round((float) (10 - (System.currentTimeMillis() - ninjaStarCooldown.get(p.getUniqueId())) / 1000)) + ChatColor.RED + " seconds!");
                return;
            }
            ninjaStarCooldown.remove(p.getUniqueId());
        }
        Cooldowns.addAbilityCooldown(p, ninjaStarCooldown, 10, ChatColor.LIGHT_PURPLE + "Ninja Star");

        PlayerInventory inv = p.getInventory();
        ItemStack ninjaStars = inv.getItemInHand();
        if (ninjaStars.getAmount() <= 1 ) {
            inv.setItemInHand(null);
        } else {
            ninjaStars.setAmount(ninjaStars.getAmount() - 1);
        }


        ArmorStand projectile = p.getWorld().spawn(p.getLocation(), ArmorStand.class);
        projectile.setVisible(false);
        projectile.setSmall(true);
        projectile.setHelmet(new ItemStack(Material.NETHER_STAR));

        Vector projVelocity = p.getEyeLocation().clone().getDirection().multiply(new Vector(2, 2.3, 2));
        projectile.setVelocity(projVelocity);

        final int[] i = {0};
        new BukkitRunnable() {
            @Override
            public void run() {
                i[0] ++;
                if (!projectile.getLocation().getBlock().getType().equals(Material.AIR)) {
                    projectile.remove();
                    this.cancel();
                    return;
                }

                for (Entity entity : projectile.getNearbyEntities(0.6, 0.6, 0.6)) {
                    if (entity instanceof Player) {
                        Player target = (Player) entity;
                        if (!target.getUniqueId().equals(p.getUniqueId())) {
                            if (Math.round(projectile.getLocation().getY()) - 1.5 <= Math.round(projectile.getLocation().getY())) {
                                PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, 20 * 5, 2, true, false);
                                target.addPotionEffect(blindness);
                                target.damage(6, p);

                                p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 1F, 1F);

                                projectile.remove();
                                this.cancel();
                                return;
                            }
                        }
                    }
                }

                projectile.setVelocity(projVelocity);

                if (i[0] >= 20L * 10L) {
                    projectile.remove();
                    this.cancel();
                }
            }
        }.runTaskTimer(SoupCore.plugin, 0L, 1L);
    }
}
