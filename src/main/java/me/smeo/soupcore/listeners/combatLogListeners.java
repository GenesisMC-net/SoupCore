package me.smeo.soupcore.listeners;

import me.smeo.soupcore.Credits;
import me.smeo.soupcore.SoupCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class combatLogListeners implements Listener {

    public ArrayList<UUID> antiLog = new ArrayList<>();
    public HashMap<BukkitTask, UUID[]> combatTimers = new HashMap<>();
    // Lists below prevent ConcurrentModification errors
    public List<BukkitTask> combatTimersToRemove = new ArrayList<>();
    public HashMap<BukkitTask, UUID[]> combatTimersToAdd = new HashMap<>();

    @EventHandler
    public void onQuit(PlayerQuitEvent e)
    {
        Player p = e.getPlayer();
        Location lastLoc = p.getLocation();
        int soupDrop = 0;
        for (ItemStack item: p.getInventory().getContents()) {
            if (Objects.equals(item.getType(), Material.MUSHROOM_SOUP))
            {
                soupDrop = soupDrop + 1;
            }
        }
        if (antiLog.contains(p.getUniqueId()))
        {
            while (antiLog.contains(p.getUniqueId())) {
                antiLog.remove(p.getUniqueId());
            }
            int killStreak = Integer.valueOf((String) Database.getPlayerData(p, "soupData", "killStreak"));
            Database.SetPlayerData(p, "soupData", "deaths", Integer.valueOf((String) Database.getPlayerData(p, "soupData", "deaths") + 1));
            Database.SetPlayerData(p, "soupData", "killStreak", 0);

            for (Map.Entry<BukkitTask, UUID[]> timer : combatTimers.entrySet()) {
                if (Objects.equals(timer.getValue()[0], p.getUniqueId()) || Objects.equals(timer.getValue()[1], p.getUniqueId())) {
                    Bukkit.getScheduler().cancelTask(timer.getKey().getTaskId());

                    // Player dies
                    if(killStreak >= 20)
                    {
                        Bukkit.broadcastMessage(ChatColor.RED + p.getName() + ChatColor.GRAY + " has died with a killstreak of " + ChatColor.AQUA + killStreak);
                    }

                    // If the player was being attacked (Attacker gets the kill)
                    if (timer.getValue()[0] != p.getUniqueId())
                    {
                        System.out.println("The logger was being attacked");
                        Player killer = Bukkit.getPlayer(timer.getValue()[0]);
                        antiLog.remove(killer.getUniqueId());
                        Integer kills = Integer.valueOf((String) Database.getPlayerData(killer, "soupData", "deaths")) + 1;
                        Database.SetPlayerData(killer, "soupData", "kills", kills);
                        Integer attackerKillStreak = Integer.valueOf((String) Database.getPlayerData(p, "soupData", "killStreak")) + 1;
                        Database.SetPlayerData(killer, "soupData", "killStreak", attackerKillStreak);

                        Random rand = new Random();
                        int credits = rand.nextInt(6) + 5; // Replace with credit rank system when created.
                        Credits.giveCredits(killer, credits);

                        killer.sendMessage(ChatColor.GRAY + "You have killed " + ChatColor.GREEN + p.getName() + ChatColor.GRAY + " and earned " + ChatColor.GREEN + credits + " credits");
                        if(SoupCore.killStreakMilestones.contains(attackerKillStreak))
                        {
                            Bukkit.broadcastMessage(ChatColor.GREEN + killer.getName() + ChatColor.GRAY + " has reached a killstreak of " + ChatColor.AQUA + attackerKillStreak);
                        }
                    } else { // The player was the attacker (no one gets the kill)
                        System.out.println("The logger was the attacker");
                        antiLog.remove(timer.getValue()[1]);
                    }

                    combatTimersToRemove.add(timer.getKey());
                }
            }
            for (BukkitTask key: combatTimersToRemove) {
                combatTimers.remove(key);
            }
            combatTimersToRemove = new ArrayList<>();

            for (int i = 0; i < soupDrop; i++) {
                Item droppedSoup = p.getWorld().dropItemNaturally(lastLoc, new ItemStack(Material.MUSHROOM_SOUP));
                new BukkitRunnable()
                {
                    @Override
                    public void run() {
                        if (Objects.equals(droppedSoup.getType(), EntityType.DROPPED_ITEM))
                        {
                            droppedSoup.remove();
                        }
                    }
                }.runTaskLaterAsynchronously(SoupCore.plugin, 20L * 7L);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        UUID playerUUID = e.getEntity().getPlayer().getUniqueId();
        for (Map.Entry<BukkitTask, UUID[]> timer : combatTimers.entrySet()) {
            if (Objects.equals(timer.getValue()[0], playerUUID) || Objects.equals(timer.getValue()[1], playerUUID)) {
                Bukkit.getScheduler().cancelTask(timer.getKey().getTaskId());
                combatTimersToRemove.add(timer.getKey());
            }
        }
        for (BukkitTask key: combatTimersToRemove) {
            combatTimers.remove(key);
        }
        combatTimersToRemove = new ArrayList<>();

        if (antiLog.contains(playerUUID)) {
            while (antiLog.contains(playerUUID)) {
                antiLog.remove(playerUUID);
            }
            e.getEntity().getPlayer().sendMessage(ChatColor.GREEN + "You are no longer in combat!");
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e)
    {
        if ((e.getDamager() instanceof Player) && (e.getEntity() instanceof Player))
        {
            Player attacker = (Player) e.getEntity();
            Player target = (Player) e.getDamager();
            UUID attackerUUID = attacker.getUniqueId();
            UUID targetUUID = target.getUniqueId();

            if (antiLog.contains(attackerUUID) && antiLog.contains(targetUUID))
            {
                for (Map.Entry<BukkitTask, UUID[]> timer : combatTimers.entrySet()) {
                    if (Objects.equals(timer.getValue()[0], attackerUUID) && Objects.equals(timer.getValue()[1], targetUUID))
                    {
                        System.out.println("Timer should reset");
                        // Cancel old timer
                        Bukkit.getServer().getScheduler().cancelTask(timer.getKey().getTaskId());

                        // Create a new timer
                        BukkitTask newCombatTagTimer = Bukkit.getServer().getScheduler().runTaskLater(SoupCore.plugin, new Runnable() {
                            @Override
                            public void run() {
                                {
                                    if ((antiLog.contains(attackerUUID) && antiLog.contains(targetUUID))) {
                                        antiLog.remove(attackerUUID);
                                        antiLog.remove(targetUUID);
                                        attacker.sendMessage(ChatColor.GREEN + "You are no longer in combat!");
                                        target.sendMessage(ChatColor.GREEN + "You are no longer in combat!");
                                        Bukkit.getServer().broadcastMessage("New timer");
                                    }
                                }
                            }
                        }, 20L * 15L);
                        combatTimersToAdd.put(newCombatTagTimer, new UUID[]{attackerUUID, targetUUID});
                        System.out.println("Timer has been recreated");
                    }
                }
                combatTimers.putAll(combatTimersToAdd);
                combatTimersToAdd = new HashMap<>();
            }

            if (!antiLog.contains(attackerUUID) && !antiLog.contains(targetUUID))
            {
                antiLog.add(attackerUUID);
                antiLog.add(targetUUID);
                attacker.sendMessage(ChatColor.GRAY + "You are now in combat! " + ChatColor.RED + "(15s)");
                target.sendMessage(ChatColor.GRAY + "You are now in combat! " + ChatColor.RED + "(15s)");

                BukkitTask combatTagTimer = Bukkit.getServer().getScheduler().runTaskLater(SoupCore.plugin, new Runnable() {
                    @Override
                    public void run() {
                        {
                            if ((antiLog.contains(attackerUUID) && antiLog.contains(targetUUID))) {
                                antiLog.remove(attackerUUID);
                                antiLog.remove(targetUUID);
                                attacker.sendMessage(ChatColor.GREEN + "You are no longer in combat!");
                                target.sendMessage(ChatColor.GREEN + "You are no longer in combat!");
                                Bukkit.getServer().broadcastMessage("Initial timer");
                            }
                        }
                    }
                }, 20L * 15L);
                combatTimers.put(combatTagTimer, new UUID[]{attackerUUID, targetUUID});
                System.out.println("Initial timer has been created");
            }
        }
    }

}
