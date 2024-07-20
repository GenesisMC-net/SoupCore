package org.genesismc.SoupCore.listeners.abilities;

import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.entity.EnderPearl;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.genesismc.SoupCore.Database.Database;
import org.genesismc.SoupCore.SoupCore;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class AbilityBlitz implements Listener {

    public static final HashMap<UUID, Long> pearlCooldown = new HashMap<>();

    private ItemStack getBlitzPearl()
    {
        ItemStack blitzPearl = new ItemStack(Material.ENDER_PEARL, 1);
        ItemMeta blitzPearlMeta = blitzPearl.getItemMeta();

        ArrayList<String> blitzPearlLore = new ArrayList<>();
        blitzPearlLore.add("");
        blitzPearlLore.add(ChatColor.GRAY + "Acts like a normal ender pearl");
        blitzPearlLore.add("");
        blitzPearlLore.add(ChatColor.WHITE + "Every Kill as Blitz: " + ChatColor.RED + "+1 Pearl");
        blitzPearlLore.add(ChatColor.GRAY + "With every kill you also get a");
        blitzPearlLore.add(ChatColor.GRAY + "boost of Speed III for 15s");
        blitzPearlMeta.setLore(blitzPearlLore);

        blitzPearlMeta.setDisplayName(ChatColor.YELLOW + "Blitz Pearl");

        blitzPearl.setItemMeta(blitzPearlMeta);
        return blitzPearl;
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent e)
    {
        Player p = e.getEntity();
        Player killer = p.getKiller();
        if (killer == null)
        {
            return;
        }

        if (Objects.equals(ChatColor.stripColor(Database.getPlayerData(killer, "soupData", "kit")), "Blitz"))
        {
            PlayerInventory inv = killer.getInventory();
            if (inv.contains(Material.ENDER_PEARL) || inv.contains((ItemStack) null)) {
                inv.addItem(getBlitzPearl());
            } else {
                inv.setItem(1, getBlitzPearl());
            }
            killer.removePotionEffect(PotionEffectType.SPEED);
            PotionEffect speedThree = new PotionEffect(PotionEffectType.SPEED, 20 * 15, 2);
            killer.addPotionEffect(speedThree);
            new BukkitRunnable() {
                @Override
                public void run() {
                    RegionManager rgManager = SoupCore.getWorldGuard.getRegionManager(p.getWorld());

                    if (!Objects.requireNonNull(rgManager.getRegion("pvp")).contains(killer.getLocation().getBlockX(), killer.getLocation().getBlockY(), killer.getLocation().getBlockZ())) {
                        this.cancel();
                        return;
                    }

                    if (!Objects.equals(ChatColor.stripColor(Database.getPlayerData(p, "soupData", "kit")), "Blitz"))
                    {
                        this.cancel();
                        return;
                    }

                    killer.removePotionEffect(PotionEffectType.SPEED);
                    PotionEffect speedTwo = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1);
                    killer.addPotionEffect(speedTwo);

                }
            }.runTaskLater(SoupCore.plugin, 20L * 15L);
        }
    }

    @EventHandler
    public void onThrow(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            Player p = (Player) e.getEntity().getShooter();

            if (e.getEntity() instanceof EnderPearl && Objects.equals(p.getItemInHand().getItemMeta().getDisplayName(), ChatColor.YELLOW + "Glider")) {
                boolean cooldownActive = false;
                if (pearlCooldown.containsKey(p.getUniqueId())) {
                    if (System.currentTimeMillis() - pearlCooldown.get(p.getUniqueId()) < 5 * 1000) {
                        cooldownActive = true;
                        p.sendMessage(ChatColor.RED + "You cannot use this ability for another " + ChatColor.GREEN + Math.round((float) (5 - (System.currentTimeMillis() - pearlCooldown.get(p.getUniqueId())) / 1000)) + ChatColor.RED + " seconds!");
                    } else {
                        pearlCooldown.remove(p.getUniqueId());
                    }
                }

                if (cooldownActive) {
                    e.setCancelled(true);
                    PlayerInventory inv = p.getInventory();
                    if (inv.contains(Material.ENDER_PEARL) || inv.contains((ItemStack) null)) {
                        inv.addItem(getBlitzPearl());
                    } else {
                        inv.setItem(1, getBlitzPearl());
                    }
                } else {
                    pearlCooldown.put(p.getUniqueId(), System.currentTimeMillis());

                    Cooldowns.addAbilityCooldown(p, pearlCooldown, 5, ChatColor.YELLOW + "E-Pearl");
                }
            }
        }
    }
    @EventHandler
    public void onPearlDamage(PlayerTeleportEvent e){
        Player p = e.getPlayer();
        if(Objects.equals(e.getCause(), PlayerTeleportEvent.TeleportCause.ENDER_PEARL))
        {
            if (Objects.equals(ChatColor.stripColor(Database.getPlayerData(p, "soupData", "kit")), "Blitz")) {
                e.setCancelled(true);
                p.setNoDamageTicks(2);
                p.teleport(e.getTo());
            }
        }
    }
}
