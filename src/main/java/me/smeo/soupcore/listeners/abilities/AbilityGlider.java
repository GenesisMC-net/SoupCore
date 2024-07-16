package me.smeo.soupcore.listeners.abilities;

import com.sk89q.worldguard.protection.managers.RegionManager;
import me.smeo.soupcore.Database.Database;
import me.smeo.soupcore.SoupCore;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class AbilityGlider implements Listener {

    public static HashMap<UUID, Long> gliderCooldown = new HashMap<>();

    private ItemStack getGliderPearl() {
        ItemStack gliderPearl = new ItemStack(Material.ENDER_PEARL, 1);
        ItemMeta gliderPearlMeta = gliderPearl.getItemMeta();

        ArrayList<String> gliderPearlLore = new ArrayList<>();
        gliderPearlLore.add("");
        gliderPearlLore.add(ChatColor.WHITE + "Right Click: " + ChatColor.RED + "Pearl Ride");
        gliderPearlLore.add(ChatColor.GRAY + "Throw the pearl like normal but instead");
        gliderPearlLore.add(ChatColor.GRAY + "you ride the pearl! +1 Pearl per Kill");
        gliderPearlMeta.setLore(gliderPearlLore);

        gliderPearlMeta.setDisplayName(ChatColor.YELLOW + "Glider");

        gliderPearl.setItemMeta(gliderPearlMeta);
        return gliderPearl;
    }

    @EventHandler
    public void onThrow(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            Player p = (Player) e.getEntity().getShooter();

            if (e.getEntity() instanceof EnderPearl && Objects.equals(p.getItemInHand().getItemMeta().getDisplayName(), ChatColor.YELLOW + "Glider"))
            {
                boolean cooldownActive = false;
                if (gliderCooldown.containsKey(p.getUniqueId())) {
                    if (System.currentTimeMillis() - gliderCooldown.get(p.getUniqueId()) < 15 * 1000) {
                        cooldownActive = true;
                        p.sendMessage(ChatColor.RED + "You cannot use this ability for another " + ChatColor.GREEN + Math.round((float) (15 - (System.currentTimeMillis() - gliderCooldown.get(p.getUniqueId())) / 1000)) + ChatColor.RED + " seconds!");
                    } else {
                        gliderCooldown.remove(p.getUniqueId());
                    }
                }

                if (cooldownActive) {
                    e.setCancelled(true);
                    PlayerInventory inv = p.getInventory();
                    if (inv.contains(Material.ENDER_PEARL) || inv.contains((ItemStack) null)) {
                        inv.addItem(getGliderPearl());
                    } else {
                        inv.setItem(1, getGliderPearl());
                    }
                    return;
                } else {
                    gliderCooldown.put(p.getUniqueId(), System.currentTimeMillis());
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (gliderCooldown.containsKey(p.getUniqueId())){
                                gliderCooldown.remove(p.getUniqueId());
                                p.sendMessage(ChatColor.GRAY + "You can now use " + ChatColor.YELLOW + "Pearl Ride");
                            }
                        }
                    }.runTaskLaterAsynchronously(SoupCore.plugin, 20L * 15L);

                    EnderPearl ender = (EnderPearl) e.getEntity();
                    ArmorStand as = (ArmorStand) ender.getWorld().spawnEntity(ender.getLocation(), EntityType.ARMOR_STAND);
                    as.setVisible(false);
                    as.setGravity(false);
                    as.setSmall(true);
                    as.setCanPickupItems(false);
                    as.setArms(false);
                    as.setBasePlate(false);
                    as.setMarker(true);
                    as.setMaxHealth(1);
                    as.setHealth(1);
                    ender.setPassenger(as);
                    as.setPassenger(p);
                }
            }
        }
    }

    @EventHandler
    public void onPearlLand(ProjectileHitEvent e) {
        if(e.getEntity() instanceof EnderPearl) {
            if(e.getEntity().getPassenger() instanceof ArmorStand) {
                e.getEntity().getPassenger().remove();
            }
        }
    }

    @EventHandler
    public void onPearlDamage(PlayerTeleportEvent e){
        Player p = e.getPlayer();
        if(Objects.equals(e.getCause(), PlayerTeleportEvent.TeleportCause.ENDER_PEARL))
        {
            if (Objects.equals(Integer.valueOf((String) Objects.requireNonNull(Database.getPlayerData(p, "soupData", "kit"))), 8)) {
                e.setCancelled(true);
                p.setNoDamageTicks(1);
                p.teleport(e.getTo());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerKill(PlayerDeathEvent e)
    {

        Player p = e.getEntity();
        Player killer = p.getKiller();

        if (killer == null)
        {
            return;
        }

        if (Objects.equals(Objects.requireNonNull(Database.getPlayerData(killer, "soupData", "kit")), "Glider"))
        {

            PlayerInventory inv = killer.getInventory();
            if (inv.contains(Material.ENDER_PEARL) || inv.contains((ItemStack) null)) {
                inv.addItem(getGliderPearl());
            } else {
                inv.setItem(1, getGliderPearl());
            }
        }
    }
}
