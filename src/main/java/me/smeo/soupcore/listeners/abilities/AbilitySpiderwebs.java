package me.smeo.soupcore.listeners.abilities;

import me.smeo.soupcore.SoupCore;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class AbilitySpiderwebs implements Listener {
    public static HashMap<UUID, Long> spiderWebCooldown = new HashMap<>();

    private void deleteWebGrid(Location targetLocation)
    {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int j = -1; j < 2; j++) {
                    for (int k = -1; k < 2; k++) {
                        Location gridCell = targetLocation.getBlock().getLocation().clone().add(j, 0, k);

                        if (gridCell.getBlock().getType().equals(Material.WEB)) {
                            gridCell.getBlock().setType(Material.AIR);
                        }
                    }
                }
            }
        }.runTaskLater(SoupCore.plugin, 20L * 10L);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        spiderWebCooldown.remove(e.getEntity().getPlayer().getUniqueId());
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e)
    {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player p = e.getPlayer();

            ItemStack itemInHand = p.getItemInHand().clone();

            if (Objects.equals(itemInHand.getType(), Material.WEB) && Objects.equals(itemInHand.getItemMeta().getDisplayName(), (ChatColor.WHITE + "Spider Webs"))) {
                {
                    boolean cooldownActive = false;
                    if (spiderWebCooldown.containsKey(p.getUniqueId())) {
                        if (System.currentTimeMillis() - spiderWebCooldown.get(p.getUniqueId()) < 30 * 1000) {
                            cooldownActive = true;
                            p.sendMessage(ChatColor.RED + "You cannot use this ability for another " + ChatColor.GREEN + Math.round((float) (30 - (System.currentTimeMillis() - spiderWebCooldown.get(p.getUniqueId())) / 1000)) + ChatColor.RED + " seconds!");
                        } else {
                            spiderWebCooldown.remove(p.getUniqueId());
                        }
                    }

                    if (!cooldownActive) {
                        spiderWebCooldown.put(p.getUniqueId(), System.currentTimeMillis());

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (spiderWebCooldown.containsKey(p.getUniqueId())){
                                    spiderWebCooldown.remove(p.getUniqueId());
                                    p.sendMessage(ChatColor.GRAY + "You can now use " + ChatColor.RED + "Web Attack");
                                }
                            }
                        }.runTaskLaterAsynchronously(SoupCore.plugin, 20L * 30L);

                        final Item[] web = {p.getWorld().dropItem(p.getEyeLocation(), new ItemStack(Material.WEB))};
                        Vector webVelocity = p.getEyeLocation().clone().getDirection().multiply(new Vector(2.5, 1, 2.5));
                        web[0].setVelocity(webVelocity);

                        final int[] i = {0};
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                i[0] = i[0] + 1;
                                for (Player target : p.getWorld().getPlayers()) {

                                    if (target.getLocation().getBlock().getLocation().equals(web[0].getLocation().getBlock().getLocation().clone()) || target.getLocation().getBlock().getLocation().equals(web[0].getLocation().getBlock().getLocation().clone().subtract(0, 1.5, 0)) || target.getEyeLocation().getBlock().getLocation().equals(web[0].getLocation().getBlock().getLocation().clone())) {
                                        if (target.getUniqueId() != p.getUniqueId()) {
                                            Location targetPlayerLocation = target.getLocation().getBlock().getLocation();
                                            for (int j = -1; j < 2; j++) {
                                                for (int k = -1; k < 2; k++) {
                                                    Location gridCell = targetPlayerLocation.clone().add(j, 0, k);
                                                    if (gridCell.getBlock().getType().equals(Material.AIR)) {
                                                        gridCell.getBlock().setType(Material.WEB);
                                                    }
                                                }
                                            }

                                            target.damage(0.1, p);
                                            deleteWebGrid(targetPlayerLocation);

                                            web[0].remove();
                                            this.cancel();
                                            return;
                                        }
                                    }
                                }
                                if (web[0].getLocation().getBlock().getType() != Material.AIR) {
                                    web[0].remove();
                                    this.cancel();
                                    return;
                                }
                                if (web[0].isOnGround()) {
                                    web[0].remove();
                                    this.cancel();
                                    return;
                                }
                                if (i[0] >= 20 * 4) {
                                    web[0].remove();
                                    this.cancel();
                                    return;
                                }
                            }

                        }.runTaskTimer(SoupCore.plugin, 0L, 1L);
                    }
                }
            }
        }
    }
}

