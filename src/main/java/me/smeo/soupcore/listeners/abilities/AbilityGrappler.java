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

public class AbilityGrappler implements Listener {
    HashMap<UUID, Long> grapplingHookCooldown = new HashMap<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        grapplingHookCooldown.remove(e.getEntity().getPlayer().getUniqueId());
    }

        @EventHandler
    public void onFish(PlayerFishEvent e)
    {
        Player p = e.getPlayer();

        ItemStack itemInHand = p.getItemInHand().clone();

        if (e.getState() == PlayerFishEvent.State.FAILED_ATTEMPT && Objects.equals(itemInHand.getItemMeta().getDisplayName(), (ChatColor.DARK_GRAY + "Grappling Hook"))) {
            {
                boolean cooldownActive = false;
                if (grapplingHookCooldown.containsKey(p.getUniqueId())) {
                    if (System.currentTimeMillis() - grapplingHookCooldown.get(p.getUniqueId()) < 20 * 1000) {
                        cooldownActive = true;
                    } else {
                        grapplingHookCooldown.remove(p.getUniqueId());
                    }
                }

                if (!cooldownActive) {

                    Location playerLocation = p.getLocation();
                    Location hookLocation = e.getHook().getLocation();

                    double x = playerLocation.getX() - hookLocation.getX();
                    double y = (playerLocation.getY() - hookLocation.getY()) + 1.5;
                    double z = playerLocation.getZ() - hookLocation.getZ();
                    Vector velocity = new Vector(x, y, z).normalize().multiply(new Vector(-8, -3.5, -8));
                    p.setVelocity(velocity);

                    grapplingHookCooldown.put(p.getUniqueId(), System.currentTimeMillis());
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            grapplingHookCooldown.remove(p.getUniqueId());
                            p.sendMessage(ChatColor.GRAY + "You can now use " + ChatColor.DARK_GRAY + "Grappling Hook");
                        }
                    }.runTaskLaterAsynchronously(SoupCore.plugin, 20L * 20L);
                }
            }
        }
    }
}
