package me.smeo.soupcore.listeners;

import me.smeo.soupcore.Database.Database;
import me.smeo.soupcore.SoupCore;
import me.smeo.soupcore.commands.spawnCommand;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;

import static me.smeo.soupcore.listeners.abilities.AbilityFisherman.playerReelCooldown;
import static me.smeo.soupcore.listeners.abilities.AbilityGlider.gliderCooldown;
import static me.smeo.soupcore.listeners.abilities.AbilityGrappler.grapplingHookCooldown;
import static me.smeo.soupcore.listeners.abilities.AbilityHulk.hulkSmashCooldown;
import static me.smeo.soupcore.listeners.abilities.AbilityMage.fireLaunchCooldown;
import static me.smeo.soupcore.listeners.abilities.AbilityMage.waterAbilityCooldown;
import static me.smeo.soupcore.listeners.abilities.AbilityNinjaStars.ninjaStarCooldown;
import static me.smeo.soupcore.listeners.abilities.AbilitySoldier.iceDomeCooldown;
import static me.smeo.soupcore.listeners.abilities.AbilitySpiderwebs.spiderWebCooldown;
import static me.smeo.soupcore.listeners.abilities.AbilityTank.silverFishCooldown;

public class PlayerDeathListener implements Listener
{
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        Player p = e.getEntity();
        Database.SetPlayerData(p, "soupData", "deaths", String.valueOf(( Integer.parseInt((String) Objects.requireNonNull(Database.getPlayerData(p, "soupData", "deaths"))))+1));
        Database.SetPlayerData(p, "soupData", "killStreak", String.valueOf(0));
        Location lastLoc = p.getLocation();

        removeCooldowns(p);

        int killStreak = Integer.parseInt((String) Objects.requireNonNull(Database.getPlayerData(p, "soupData", "killStreak")));
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
                    soupDrop += 1;
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
        Vector v = p.getVelocity();
        v.setX(0);
        v.setY(0);
        v.setZ(0);
        p.setVelocity(v);
        e.getEntity().spigot().respawn();

        spawnCommand.spawnInventory(p);
    }

    private void removeCooldowns(Player player) {
        playerReelCooldown.remove(player.getUniqueId());
        gliderCooldown.remove(player.getUniqueId());
        grapplingHookCooldown.remove(player.getUniqueId());
        hulkSmashCooldown.remove(player.getUniqueId());
        waterAbilityCooldown.remove(player.getUniqueId());
        fireLaunchCooldown.remove(player.getUniqueId());
        ninjaStarCooldown.remove(player.getUniqueId());
        iceDomeCooldown.remove(player.getUniqueId());
        spiderWebCooldown.remove(player.getUniqueId());
        silverFishCooldown.remove(player.getUniqueId());
    }
}
