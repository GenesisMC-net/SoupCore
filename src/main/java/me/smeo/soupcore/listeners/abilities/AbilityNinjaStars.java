package me.smeo.soupcore.listeners.abilities;

import me.smeo.soupcore.Database.Database;
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
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class AbilityNinjaStars implements Listener {
    HashMap<UUID, Long> ninjaStarCooldown = new HashMap<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        ninjaStarCooldown.remove(e.getEntity().getPlayer().getUniqueId());
        if(e.getEntity().getPlayer().getKiller() == null){return;}
        if (Objects.equals(Integer.valueOf((String) Objects.requireNonNull(Database.getPlayerData(e.getEntity().getPlayer().getKiller(), "soupData", "kit"))), 4)) // Kit ID for stealth is 4
        {
            ItemStack ninjaStar = new ItemStack(Material.NETHER_STAR, 1);

            ItemMeta ninjaStarMeta = ninjaStar.getItemMeta();

            ArrayList<String> ninjaStarLore = new ArrayList<>();
            ninjaStarLore.add("");
            ninjaStarLore.add(ChatColor.WHITE + "Right Click: " + ChatColor.RED + "Star Throw");
            ninjaStarLore.add(ChatColor.GRAY + "Throw a ninja star that deals blindness");
            ninjaStarLore.add(ChatColor.GRAY + "for 5 seconds to any player it hits!");
            ninjaStarLore.add("");
            ninjaStarLore.add(ChatColor.WHITE + "Every kill with the Ninja Kit:" + ChatColor.GREEN + " +1 Ninja Star");
            ninjaStarMeta.setLore(ninjaStarLore);

            ninjaStarMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Ninja Star");

            ninjaStar.setItemMeta(ninjaStarMeta);
            PlayerInventory inv = e.getEntity().getPlayer().getKiller().getInventory();
            if (inv.contains(ninjaStar) || inv.contains((ItemStack) null)) {
                inv.addItem(ninjaStar);
            } else {
                inv.setItem(1, ninjaStar);
            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e)
    {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player p = e.getPlayer();

            ItemStack itemInHand = p.getItemInHand().clone();

            if (Objects.equals(itemInHand.getType(), Material.NETHER_STAR) && Objects.equals(itemInHand.getItemMeta().getDisplayName(), ChatColor.LIGHT_PURPLE + "Ninja Star")) {
                {
                    boolean cooldownActive = false;
                    if (ninjaStarCooldown.containsKey(p.getUniqueId())) {
                        if (System.currentTimeMillis() - ninjaStarCooldown.get(p.getUniqueId()) < 10 * 1000) {
                            cooldownActive = true;
                            p.sendMessage(ChatColor.RED + "You cannot use this ability for another " + ChatColor.GREEN + Math.round((float) (10 - (System.currentTimeMillis() - ninjaStarCooldown.get(p.getUniqueId())) / 1000)) + ChatColor.RED + " seconds!");
                        } else {
                            ninjaStarCooldown.remove(p.getUniqueId());
                        }
                    }

                    if (!cooldownActive) {
                        ninjaStarCooldown.put(p.getUniqueId(), System.currentTimeMillis());

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                ninjaStarCooldown.remove(p.getUniqueId());
                                p.sendMessage(ChatColor.GRAY + "You can now use " + ChatColor.LIGHT_PURPLE + "Ninja Star");
                            }
                        }.runTaskLaterAsynchronously(SoupCore.plugin, 20L * 10L);


                        ItemStack oneNinjaStar = itemInHand.clone();

                        p.getInventory().removeItem(itemInHand);
                        if (itemInHand.getAmount() > 1) {
                            oneNinjaStar.setAmount(itemInHand.getAmount() - 1);
                            p.getInventory().addItem(oneNinjaStar);
                        }

                        oneNinjaStar.setAmount(1);

                        final Item[] ninjaStar = {p.getWorld().dropItem(p.getEyeLocation(), oneNinjaStar)};
                        Vector starVelocity = p.getEyeLocation().clone().getDirection();
                        ninjaStar[0].setVelocity(starVelocity);

                        final int[] i = {0};
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Location oldLocation = ninjaStar[0].getLocation().clone();
                                ninjaStar[0].remove();
                                ninjaStar[0] = p.getWorld().dropItem(oldLocation, oneNinjaStar);
                                ninjaStar[0].setVelocity(starVelocity);
                                i[0] = i[0] + 1;
                                for (Player target : p.getWorld().getPlayers()) {
                                    if (target.getLocation().getBlock().getLocation().equals(ninjaStar[0].getLocation().getBlock().getLocation().clone().subtract(0, 1, 0)) || target.getLocation().getBlock().getLocation().equals(ninjaStar[0].getLocation().getBlock().getLocation().clone().subtract(0, 0, 0))) {
                                        if (target.getUniqueId() != p.getUniqueId()) {
                                            target.damage(6, p);
                                            PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, 20 * 5, 0);
                                            target.addPotionEffect(blindness);
                                            ninjaStar[0].remove();
                                            this.cancel();
                                            return;
                                        }
                                    }
                                }
                                if (ninjaStar[0].getLocation().getBlock().getType() != Material.AIR) {
                                    ninjaStar[0].remove();
                                    this.cancel();
                                    return;
                                }
                                if (ninjaStar[0].isOnGround()) {
                                    ninjaStar[0].remove();
                                    this.cancel();
                                    return;
                                }
                                if (i[0] >= 20 * 2) {
                                    ninjaStar[0].remove();
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
