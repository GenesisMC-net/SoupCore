package me.smeo.soupcore.listeners;

import me.smeo.soupcore.Database.Database;
import me.smeo.soupcore.SoupCore;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;

public class PlayerDeathListener implements Listener
{

    @EventHandler(priority = EventPriority.LOWEST)
    public void OnPlayerDeath(PlayerDeathEvent e)
    {
        Player p = e.getEntity();
        Database.SetPlayerData(p, "soupData", "deaths", ( Integer.valueOf((String) Database.getPlayerData(p, "soupData", "deaths")))+1);
        Database.SetPlayerData(p, "soupData", "killStreak", 0);
        Location lastLoc = p.getLocation();


        int killStreak = Integer.valueOf((String) Database.getPlayerData(p, "soupData", "killStreak"));
        if(killStreak >= 20)
        {
            e.setDeathMessage(ChatColor.RED + p.getName() + ChatColor.GRAY + " has died with a killstreak of " + ChatColor.AQUA + killStreak);
        } else {
            e.setDeathMessage("");
        }

        int soupDrop = 0;
        for (ItemStack item: p.getInventory().getContents()) {
            if (!Objects.equals(item, null) && !Objects.equals(item.getType(), Material.AIR)) {
                if (Objects.equals(item.getType(), Material.MUSHROOM_SOUP)) {
                    soupDrop = soupDrop + 1;
                }
            }
        }
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

        for (PotionEffect effect : p.getActivePotionEffects())
        {
            p.removePotionEffect(effect.getType());
        }
        p.getInventory().clear();
        p.getInventory().setArmorContents(new ItemStack[]{null, null, null, null});

        new BukkitRunnable(){
            @Override
            public void run() {
                Vector v = p.getVelocity();
                v.setX(0);
                v.setY(0);
                v.setZ(0);
                p.setVelocity(v);
                e.getEntity().spigot().respawn();
            }
        }.runTaskLater(SoupCore.plugin, 1L);
    }
}
