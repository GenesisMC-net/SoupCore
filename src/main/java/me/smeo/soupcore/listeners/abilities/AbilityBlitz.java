package me.smeo.soupcore.listeners.abilities;

import com.sk89q.worldguard.protection.managers.RegionManager;
import me.smeo.soupcore.SoupCore;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Objects;

public class AbilityBlitz implements Listener {

    private ItemStack getBlitzPearl()
    {
        ItemStack blitzPearl = new ItemStack(Material.ENDER_PEARL, 1);
        ItemMeta blitzPearlMeta = blitzPearl.getItemMeta();

        ArrayList<String> blitzPearlLore = new ArrayList<>();
        blitzPearlLore.add("");
        blitzPearlLore.add(ChatColor.GRAY + "Acts like a normal ender pearl");
        blitzPearlLore.add("");
        blitzPearlLore.add(ChatColor.WHITE + "Every Kill as Blitz: " + ChatColor.RED + "+1 Pearl");
        blitzPearlLore.add(ChatColor.GRAY + "With every kill you also get a");
        blitzPearlLore.add(ChatColor.GRAY + "boost of Speed III for 15s");
        blitzPearlMeta.setLore(blitzPearlLore);

        blitzPearlMeta.setDisplayName(ChatColor.YELLOW + "Blitz Pearl");

        blitzPearl.setItemMeta(blitzPearlMeta);
        return blitzPearl;
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent e)
    {
        Player p = e.getEntity();
        Player killer = p.getKiller();
        System.out.println((killer == null));
        if (killer == null)
        {
            return;
        }
        System.out.println("Killer: " + killer.getName());
        System.out.println(Integer.valueOf((String) Database.getPlayerData(player, "soupData", "kit")));
        if (!Objects.equals(Integer.valueOf((String) Database.getPlayerData(player, "soupData", "kit")), 3)) // Index for blitz kit
        {
            return;
        }

        System.out.println(killer.getName());
        PlayerInventory inv = killer.getInventory();
        if (inv.contains(Material.ENDER_PEARL) || inv.contains((ItemStack) null)) {
            inv.addItem(getBlitzPearl());
        } else {
            inv.setItem(1, getBlitzPearl());
        }
        killer.removePotionEffect(PotionEffectType.SPEED);
        PotionEffect speedThree = new PotionEffect(PotionEffectType.SPEED, 15, 2);
        killer.addPotionEffect(speedThree);
        new BukkitRunnable()
        {
            @Override
            public void run() {
                RegionManager rgManager = SoupCore.getWorldGuard.getRegionManager(p.getWorld());

                System.out.println("15 seconds");

                if (!Objects.requireNonNull(rgManager.getRegion("pvp")).contains(killer.getLocation().getBlockX(), killer.getLocation().getBlockY(), killer.getLocation().getBlockZ()))
                {
                    this.cancel();
                    return;
                }
                System.out.println("Killer is still in pvp");
                if (!Objects.equals(Integer.valueOf((String) Database.getPlayerData(p, "soupData", "kit")), 3)) // Index for blitz kit
                {
                    this.cancel();
                    return;
                }
                System.out.println("Killer has kit blitz");

                killer.removePotionEffect(PotionEffectType.SPEED);
                PotionEffect speedTwo = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1);
                killer.addPotionEffect(speedTwo);

            }
        }.runTaskLaterAsynchronously(SoupCore.plugin, 20L * 15L);
    }
}
