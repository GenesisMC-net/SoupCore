package me.smeo.soupcore.commands;

import me.smeo.soupcore.Database.Database;
import me.smeo.soupcore.SoupCore;
import me.smeo.soupcore.listeners.combatLogListeners;
import org.bukkit.ChatColor;
import me.smeo.soupcore.listeners.combatLogListeners.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
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

import java.util.*;

public class spawnCommand implements CommandExecutor {

    private final Map<UUID, Long> spawnCooldown = new HashMap<>();

    public static void spawnInventory(Player p)
    {
        for (PotionEffect effect : p.getActivePotionEffects())
        {
            p.removePotionEffect(effect.getType());
        }
        PlayerInventory inv = p.getInventory();
        inv.clear();
        inv.setArmorContents(new ItemStack[]{null, null, null, null});

        ItemStack kitSelection = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta kitSelectionMeta = kitSelection.getItemMeta();

        kitSelectionMeta.setDisplayName(ChatColor.YELLOW + "Kit Selection");
        kitSelection.setItemMeta(kitSelectionMeta);
        inv.setItem(0, kitSelection);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player)
        {
            Player p = (Player) sender;

            if (combatLogListeners.antiLog.contains(p.getUniqueId()))
            {
                p.sendMessage(ChatColor.RED + "You cannot use this command while in combat!");
                return true;
            }
            boolean cooldownActive = false;
            if (spawnCooldown.containsKey(p.getUniqueId())) {
                if (System.currentTimeMillis() - spawnCooldown.get(p.getUniqueId()) < 15 * 1000) {
                    cooldownActive = true;
                    p.sendMessage(ChatColor.RED + "You cannot use this command for another " + ChatColor.GREEN + Math.round((float) (15 - (System.currentTimeMillis() - spawnCooldown.get(p.getUniqueId())) / 1000)) + ChatColor.RED + " seconds!");
                } else {
                    spawnCooldown.remove(p.getUniqueId());
                }
            }

            if (!cooldownActive) {
                p.sendMessage(ChatColor.GREEN + "Attempting to teleport to spawn...");
                Location startLocation = p.getLocation().getBlock().getLocation().clone();
                final int[] i = {0};
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        System.out.println(startLocation.toString() + " | " + p.getLocation().getBlock().getLocation().toString());
                        if (!startLocation.equals(p.getLocation().getBlock().getLocation())) {
                            p.sendMessage(ChatColor.RED + "Teleport Failed");
                            this.cancel();
                            return;
                        }

                        if (i[0] >= 5) {
                            Vector v = p.getVelocity();
                            v.setX(0);
                            v.setY(0);
                            v.setZ(0);
                            p.setVelocity(v);

                            Location spawnLoc = p.getWorld().getSpawnLocation();

                            p.teleport(spawnLoc);
                            p.sendMessage(ChatColor.GREEN + "You are now at spawn");
                            p.playSound(p.getLocation(), Sound.LEVEL_UP, 10, 1);
                            spawnInventory(p);

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
            }
        }
        return true;
    }
}
