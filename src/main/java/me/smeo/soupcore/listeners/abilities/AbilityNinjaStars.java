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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.Random;

public class AbilityNinjaStars implements Listener {
    @EventHandler
    public void onDamagePlayer(PlayerInteractEvent e)
    {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player p = e.getPlayer();

            ItemStack itemInHand = p.getItemInHand().clone();

            if (Objects.equals(itemInHand.getType(), Material.NETHER_STAR) && Objects.equals(itemInHand.getItemMeta().getDisplayName(), ChatColor.LIGHT_PURPLE + "Ninja Star")) {
                {
                    ItemStack oneNinjaStar = itemInHand.clone();

                    p.getInventory().removeItem(itemInHand);
                    if (itemInHand.getAmount() > 1) {
                        oneNinjaStar.setAmount(itemInHand.getAmount() - 1);
                        p.getInventory().addItem(oneNinjaStar);
                    }

                    oneNinjaStar.setAmount(1);
                    final Item[] ninjaStar = {p.getWorld().dropItem(p.getEyeLocation(), oneNinjaStar)};
                    Vector starVelocity = p.getEyeLocation().clone().getDirection().setY(0.01);
                    ninjaStar[0].setVelocity(starVelocity);

                    final int[] i = {0};
                    new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            Location oldLocation = ninjaStar[0].getLocation().clone();
                            ninjaStar[0].remove();
                            ninjaStar[0] = p.getWorld().dropItem(oldLocation, oneNinjaStar);
                            ninjaStar[0].setVelocity(starVelocity);
                            i[0] = i[0] + 1;
                            for (Player target: p.getWorld().getPlayers()) {
                                System.out.println(target.getName());
                                System.out.println(target.getLocation().getBlock().getLocation().toString());
                                System.out.println(ninjaStar[0].getLocation().getBlock().getLocation().clone().subtract(0,1,0).toString());
                                System.out.println(target.getLocation().getBlock().getLocation().equals(ninjaStar[0].getLocation().getBlock().getLocation().clone().subtract(0,1,0)));
                                if (target.getLocation().getBlock().getLocation().equals(ninjaStar[0].getLocation().getBlock().getLocation().clone().subtract(0,1,0)))
                                {
                                    if (target.getUniqueId() != p.getUniqueId())
                                    {
                                        System.out.println("I hit a player");
                                        target.damage(4, p);
                                        PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, 20 * 5, 1);
                                        target.addPotionEffect(blindness);
                                        ninjaStar[0].remove();
                                        this.cancel();
                                        return;
                                    }
                                }
                            }
                            if (ninjaStar[0].getLocation().getBlock().getType() != Material.AIR) {
                                System.out.println("I hit a wall");
                                ninjaStar[0].remove();
                                this.cancel();
                                return;
                            }
                            System.out.println(i[0]);
                            if (i[0] >= 20 * 2) {
                                System.out.println("Ran out of time");
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
