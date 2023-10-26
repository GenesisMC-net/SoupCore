package me.smeo.soupcore.listeners.abilities;

import me.smeo.soupcore.SoupCore;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class AbilityFisherman implements Listener {
    HashMap<UUID, Long> playerReelCooldown = new HashMap<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        playerReelCooldown.remove(e.getEntity().getPlayer().getUniqueId());
    }

    @EventHandler
    public void onFish(PlayerFishEvent e)
    {
        Player p = e.getPlayer();

        ItemStack itemInHand = p.getItemInHand().clone();

        if (e.getCaught() instanceof Player && Objects.equals(itemInHand.getItemMeta().getDisplayName(), ChatColor.DARK_GREEN + "Fishing Rod")) {
            {
                boolean cooldownActive = false;
                if (playerReelCooldown.containsKey(p.getUniqueId())) {
                    if (System.currentTimeMillis() - playerReelCooldown.get(p.getUniqueId()) < 30 * 1000) {
                        cooldownActive = true;
                    } else {
                        playerReelCooldown.remove(p.getUniqueId());
                    }
                }

                if (!cooldownActive) {

                    e.getCaught().teleport(p);

                    playerReelCooldown.put(p.getUniqueId(), System.currentTimeMillis());
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            playerReelCooldown.remove(p.getUniqueId());
                            p.sendMessage(ChatColor.GRAY + "You can now use " + ChatColor.DARK_GREEN + "Player Reel");
                        }
                    }.runTaskLaterAsynchronously(SoupCore.plugin, 20L * 30L);
                }
            }
        }
    }
}
