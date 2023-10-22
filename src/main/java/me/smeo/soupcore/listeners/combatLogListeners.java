package me.smeo.soupcore.listeners;

import me.smeo.soupcore.SoupCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class combatLogListeners implements Listener {

    public ArrayList<UUID> antiLog = new ArrayList<>();
    public ArrayList<String[]> combatTimers = new ArrayList<>();

//    @EventHandler
//    public void onQuit(PlayerQuitEvent e)
//    {
//        Player p = e.getPlayer();
//        if (antiLog.contains(p.getName()))
//        {
//
//        }
//    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        UUID playerUUID = e.getEntity().getPlayer().getUniqueId();
        for (String[] timer : combatTimers) {
            if (Objects.equals(timer[1], String.valueOf(playerUUID)) || Objects.equals(timer[2], String.valueOf(playerUUID))) {
                Bukkit.getScheduler().cancelTask(Integer.parseInt(timer[0]));
            }
        }
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
                for (String[] timer : combatTimers) {
                    if (Objects.equals(timer[1], String.valueOf(attackerUUID)) && Objects.equals(timer[2], String.valueOf(targetUUID)))
                    {
                        // Cancel old timer
                        Bukkit.getScheduler().cancelTask(Integer.parseInt(timer[0]));

                        // Create a new timer
                        int newCombatTagTimer = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SoupCore.plugin, new Runnable() {
                            @Override
                            public void run() {
                                {
                                    if ((antiLog.contains(attackerUUID) && antiLog.contains(targetUUID))) {
                                        antiLog.remove(attackerUUID);
                                        antiLog.remove(targetUUID);
                                        attacker.sendMessage(ChatColor.GREEN + "You are no longer in combat!");
                                        target.sendMessage(ChatColor.GREEN + "You are no longer in combat!");
                                    }
                                }
                            }
                        }, 20L * 15L);
                        combatTimers.add(new String[]{String.valueOf(newCombatTagTimer), String.valueOf(attackerUUID), String.valueOf(targetUUID)});
                    }
                }
            }

            if (!antiLog.contains(attackerUUID) && !antiLog.contains(targetUUID))
            {
                antiLog.add(attackerUUID);
                antiLog.add(targetUUID);
                attacker.sendMessage(ChatColor.GRAY + "You are now in combat! " + ChatColor.RED + "(15s)");
                target.sendMessage(ChatColor.GRAY + "You are now in combat! " + ChatColor.RED + "(15s)");

                int combatTagTimer = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SoupCore.plugin, new Runnable() {
                    @Override
                    public void run() {
                        {
                            if ((antiLog.contains(attackerUUID) && antiLog.contains(targetUUID))) {
                                antiLog.remove(attackerUUID);
                                antiLog.remove(targetUUID);
                                attacker.sendMessage(ChatColor.GREEN + "You are no longer in combat!");
                                target.sendMessage(ChatColor.GREEN + "You are no longer in combat!");
                            }
                        }
                    }
                }, 20L * 15L);
                combatTimers.add(new String[]{String.valueOf(combatTagTimer), String.valueOf(attackerUUID), String.valueOf(targetUUID)});
            }
        }
    }

}
