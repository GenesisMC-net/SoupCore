package org.genesismc.SoupCore.commands;

import org.bukkit.*;
import org.genesismc.SoupCore.SoupCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.genesismc.SoupCore.listeners.abilities.Cooldowns;

import java.util.*;

import static org.genesismc.SoupCore.Duels.*;
import static org.genesismc.SoupCore.SoupCore.playerInSpawn;
import static org.genesismc.SoupCore.listeners.combatLogListeners.antiLog;

public class spawnCommand implements CommandExecutor {

    private final Map<UUID, Long> spawnCooldown = new HashMap<>();

    public static void spawnInventory(Player p)
    {
        p.teleport(new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), 180, 0));
        p.setHealth(p.getMaxHealth());
        for (PotionEffect effect : p.getActivePotionEffects())
        {
            p.removePotionEffect(effect.getType());
        }
        PlayerInventory inv = p.getInventory();
        inv.clear();
        inv.setArmorContents(new ItemStack[]{null, null, null, null});

        // Kit Selection
        ItemStack kitSelection = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta kitSelectionMeta = kitSelection.getItemMeta();

        kitSelectionMeta.setDisplayName(ChatColor.YELLOW + "Kit Selection");
        kitSelection.setItemMeta(kitSelectionMeta);
        inv.setItem(0, kitSelection);

        // Duels Warp
        ItemStack duelWarp = new ItemStack(Material.BLAZE_ROD);
        ItemMeta duelWarpMeta = duelWarp.getItemMeta();

        duelWarpMeta.setDisplayName(ChatColor.GOLD + "Duels");
        duelWarp.setItemMeta(duelWarpMeta);
        inv.setItem(8, duelWarp);
    }

    public static void teleportToSpawn(Player p) {
        Vector v = p.getVelocity();
        v.setX(0);
        v.setY(0);
        v.setZ(0);
        p.setVelocity(v);

        Location spawnLoc = Bukkit.getWorld("world").getSpawnLocation();

        p.teleport(spawnLoc);
        p.sendMessage(ChatColor.GREEN + "You are now at spawn");
        Cooldowns.removeCooldowns(p);
        spawnInventory(p);

        new BukkitRunnable() {
            @Override
            public void run() {
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 10, 1);
            }
        }.runTaskLater(SoupCore.plugin, 1L);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) { return false; }

        Player p = (Player) sender;

        if (antiLog.containsKey(p.getUniqueId()))
        {
            p.sendMessage(ChatColor.RED + "You cannot use this command while in combat!");
            return true;
        }
        if (activeDuels.containsKey(p.getUniqueId()) || activeDuels.containsValue(p.getUniqueId()))
        {
            p.sendMessage(ChatColor.RED + "You cannot use this command while during a duel!");
            return true;
        }
        if (spawnCooldown.containsKey(p.getUniqueId())) {
            if (System.currentTimeMillis() - spawnCooldown.get(p.getUniqueId()) < 15 * 1000) {
                p.sendMessage(ChatColor.RED + "You cannot use this command for another " + ChatColor.GREEN + Math.round((float) (15 - (System.currentTimeMillis() - spawnCooldown.get(p.getUniqueId())) / 1000)) + ChatColor.RED + " seconds!");
                return true;
            }
            spawnCooldown.remove(p.getUniqueId());
        }

        if (playerInSpawn(p)) { teleportToSpawn(p); return true; }
        if (awaitingRematch.containsKey(p.getUniqueId()) || awaitingRematch.containsValue(p.getUniqueId())) {
            showAllPlayers(p);
            teleportToSpawn(p);
            awaitingRematch.remove(p.getUniqueId());
            return true;
        }

        p.sendMessage(ChatColor.GREEN + "Attempting to teleport to spawn...");
        Location startLocation = p.getLocation().getBlock().getLocation().clone();
        final int[] i = {0};
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!startLocation.equals(p.getLocation().getBlock().getLocation())) {
                    p.sendMessage(ChatColor.RED + "Teleport Failed");
                    this.cancel();
                    return;
                }
                if (antiLog.containsKey(p.getUniqueId())) {
                    p.sendMessage(ChatColor.RED + "Teleport Failed");
                    this.cancel();
                    return;
                }

                if (i[0] >= 5) {
                    teleportToSpawn(p);
                    this.cancel();
                    return;
                }

                p.sendMessage(ChatColor.GRAY + "Time Remaining: " + ChatColor.RED + (5 - i[0]) + "s");
                p.playSound(p.getLocation(), Sound.WOOD_CLICK, 8, 1);

                spawnCooldown.remove(p.getUniqueId());
                i[0]++;
            }
        }.runTaskTimer(SoupCore.plugin, 0L, 20L);

        spawnCooldown.put(p.getUniqueId(), System.currentTimeMillis());
        new BukkitRunnable() {
            @Override
            public void run() {
                spawnCooldown.remove(p.getUniqueId());
            }
        }.runTaskLaterAsynchronously(SoupCore.plugin, 20L * 15L);
        return true;
    }
}
