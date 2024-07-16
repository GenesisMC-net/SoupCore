package me.smeo.soupcore.listeners;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.smeo.soupcore.Credits;
import me.smeo.soupcore.Database.Database;
import me.smeo.soupcore.SoupCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class combatLogListeners implements Listener {

    public static HashMap<UUID, Long> antiLog = new HashMap<>();
    public static HashMap<BukkitTask, UUID[]> combatTimers = new HashMap<>();
    // Lists below prevent ConcurrentModification errors
    private List<BukkitTask> combatTimersToRemove = new ArrayList<>();
    private HashMap<BukkitTask, UUID[]> combatTimersToAdd = new HashMap<>();

    @EventHandler
    public void onFish(PlayerFishEvent e)
    {
        Player p = e.getPlayer();
        // Put the player in combat if they use a rod
        if (e.getCaught() instanceof Player) {
            ((Player) e.getCaught()).getPlayer().damage(0.1, p);
        }
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent e) {
        ThrownPotion potion = e.getPotion();
        ProjectileSource source = potion.getShooter();

        if (!(source instanceof Player)) {
            return;
        }

        Player attacker = (Player) source;

        for (Entity entity : e.getAffectedEntities()) {
            if (entity instanceof Player) {
                Player target = (Player) entity;
                if (!target.getUniqueId().equals(attacker.getUniqueId())) {
                    target.damage(0.1, attacker);
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e)
    {
        Player p = e.getPlayer();
        Location lastLoc = p.getLocation();
        int soupDrop = 0;
        for (ItemStack item: p.getInventory().getContents()) {
            if (item != null)
            {
                if (Objects.equals(item.getType(), Material.MUSHROOM_SOUP))
                {
                    soupDrop = soupDrop + 1;
                }
            }
        }
        if (antiLog.containsKey(p.getUniqueId()))
        {
            while (antiLog.containsKey(p.getUniqueId())) {
                antiLog.remove(p.getUniqueId());
            }
            Database.SetPlayerData(p, "soupData", "deaths", (String) Database.getPlayerData(p, "soupData", "deaths")+1);
            Database.SetPlayerData(p, "soupData", "killStreak", String.valueOf(0));

            for (Map.Entry<BukkitTask, UUID[]> timer : combatTimers.entrySet()) {
                if (Objects.equals(timer.getValue()[0], p.getUniqueId()) || Objects.equals(timer.getValue()[1], p.getUniqueId())) {
                    Bukkit.getScheduler().cancelTask(timer.getKey().getTaskId());

                    // Player dies
                    int killStreak = Integer.parseInt((String) Objects.requireNonNull(Database.getPlayerData(p, "soupData", "killStreak")));
                    if(killStreak >= 20)
                    {
                        Bukkit.broadcastMessage(ChatColor.RED + p.getName() + ChatColor.GRAY + " has died with a killstreak of " + ChatColor.AQUA + killStreak);
                    }

                    // If the player was being attacked (Attacker gets the kill)
                    if (timer.getValue()[0] != p.getUniqueId())
                    {
                        Player killer = Bukkit.getPlayer(timer.getValue()[0]);
                        antiLog.remove(killer.getUniqueId());
                        Integer kills = Integer.parseInt((String) Objects.requireNonNull(Database.getPlayerData(killer, "soupData", "kills"))) + 1;
                        Database.SetPlayerData(killer, "soupData", "kills", String.valueOf(kills));
                        Integer attackerKillStreak = Integer.parseInt((String) Objects.requireNonNull(Database.getPlayerData(killer, "soupData", "killStreak"))) + 1;
                        Database.SetPlayerData(killer, "soupData", "killStreak", String.valueOf(attackerKillStreak));

                        Random rand = new Random();
                        int credits = rand.nextInt(6) + 5; // Replace with credit rank system when created.
                        Credits.giveCredits(killer, credits);

                        killer.sendMessage(ChatColor.GRAY + "You have killed " + ChatColor.GREEN + p.getName() + ChatColor.GRAY + " and earned " + ChatColor.GREEN + credits + " credits");
                        if(SoupCore.killStreakMilestones.contains(attackerKillStreak))
                        {
                            Bukkit.broadcastMessage(ChatColor.GREEN + killer.getName() + ChatColor.GRAY + " has reached a killstreak of " + ChatColor.AQUA + attackerKillStreak);
                        }
                    } else { // The player was the attacker (no one gets the kill)
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

        Player killer = e.getEntity().getPlayer().getKiller();
        if (killer == null) {return;}

        if (antiLog.containsKey(playerUUID)) {
            while (antiLog.containsKey(playerUUID)) {
                antiLog.remove(playerUUID);
            }

            if (antiLog.containsKey(killer.getUniqueId())) {
                while (antiLog.containsKey(killer.getUniqueId())) {

                    antiLog.remove(killer.getUniqueId());
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e)
    {
        if (!(e.getDamager() instanceof Player) && !(e.getEntity() instanceof Player)) {
            return;
        }
        for (ProtectedRegion rg : WGBukkit.getRegionManager(e.getEntity().getWorld()).getApplicableRegions(e.getEntity().getLocation())){
            if (Objects.equals(rg.getId(), "spawn")) {return;} // Return if they are in spawn
        }

        Player attacker = (Player) e.getDamager();
        Player target = (Player) e.getEntity();
        UUID attackerUUID = attacker.getUniqueId();
        UUID targetUUID = target.getUniqueId();

        if (antiLog.containsKey(attackerUUID) && antiLog.containsKey(targetUUID))
        {
            for (Map.Entry<BukkitTask, UUID[]> timer : combatTimers.entrySet()) {
                if (Objects.equals(timer.getValue()[0], attackerUUID) && Objects.equals(timer.getValue()[1], targetUUID))
                {
                    // Cancel old timer
                    Bukkit.getServer().getScheduler().cancelTask(timer.getKey().getTaskId());
                    combatTimersToRemove.add(timer.getKey());

                    // Create a new timer
                    createCombatTimer(attacker, target);
                }
                if (Objects.equals(timer.getValue()[0], targetUUID) && Objects.equals(timer.getValue()[1], attackerUUID))
                {
                    // Cancel old timer
                    Bukkit.getServer().getScheduler().cancelTask(timer.getKey().getTaskId());
                    combatTimersToRemove.add(timer.getKey());

                    // Create a new timer
                    createCombatTimer(attacker, target);
                }
            }
            combatTimers.putAll(combatTimersToAdd);
            for (BukkitTask key: combatTimersToRemove) {
                combatTimers.remove(key);
            }
            combatTimersToRemove = new ArrayList<>();
            combatTimersToAdd = new HashMap<>();
        }

        if (!antiLog.containsKey(attackerUUID) && !antiLog.containsKey(targetUUID))
        {
            attacker.sendMessage(ChatColor.GRAY + "You are now in combat! " + ChatColor.RED + "(15s)");
            target.sendMessage(ChatColor.GRAY + "You are now in combat! " + ChatColor.RED + "(15s)");

            createCombatTimer(attacker, target);
        }
    }

    private void createCombatTimer(Player attacker, Player target) {
        UUID attackerUUID = attacker.getUniqueId();
        UUID targetUUID = target.getUniqueId();

        antiLog.remove(attackerUUID);
        antiLog.remove(targetUUID);

        antiLog.put(attackerUUID, 0L);
        antiLog.put(targetUUID, 0L);

        final int[] i = {0};
        BukkitTask newCombatTagTimer = new BukkitRunnable() {
            @Override
            public void run() {
                if (!antiLog.containsKey(attackerUUID) && !antiLog.containsKey(targetUUID)) {
                    this.cancel();
                    return;
                }

                if (i[0] >= 20L * 15L) {
                    antiLog.remove(attackerUUID);
                    antiLog.remove(targetUUID);
                    attacker.sendMessage(ChatColor.GREEN + "You are no longer in combat!");
                    target.sendMessage(ChatColor.GREEN + "You are no longer in combat!");
                    this.cancel();
                    return;
                }
                antiLog.replace(attackerUUID, (long) i[0]);
                antiLog.replace(targetUUID, (long) i[0]);

                i[0] += 2;
            }
        }.runTaskTimerAsynchronously(SoupCore.plugin,0L, 2L);
        combatTimersToAdd.put(newCombatTagTimer, new UUID[]{attackerUUID, targetUUID});
    }
}
