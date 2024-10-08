package org.genesismc.SoupCore.listeners.abilities;

import org.genesismc.SoupCore.Database.Database;
import org.bukkit.ChatColor;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class AbilityGlider implements Listener {

    public static final HashMap<UUID, Long> gliderCooldown = new HashMap<>();

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

            if (!(e.getEntity() instanceof EnderPearl)) { return; }
            if (!Objects.equals(ChatColor.stripColor(Database.getPlayerData(p, "soupData", "kit")), "Glider")) { return; }

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
            }
            Cooldowns.addAbilityCooldown(p, gliderCooldown, 15, ChatColor.YELLOW + "Pearl Ride");

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

    @EventHandler
    public void onPearlLand(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof EnderPearl)) {
            return;
        }
        if(e.getEntity().getPassenger() instanceof ArmorStand) {
            e.getEntity().getPassenger().remove();
        }
    }

    @EventHandler
    public void onPearlDamage(PlayerTeleportEvent e){
        Player p = e.getPlayer();
        if(!Objects.equals(e.getCause(), PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
            return;
        }
        if (!Objects.equals(ChatColor.stripColor(Database.getPlayerData(p, "soupData", "kit")), "Glider")) {
            return;
        }
        e.setCancelled(true);
        p.setNoDamageTicks(1);
        p.teleport(e.getTo());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerKill(PlayerDeathEvent e)
    {
        Player p = e.getEntity().getPlayer();
        if (p.getKiller() == null) {
            return;
        }

        Player killer = p.getKiller();
        String activeKit = ChatColor.stripColor(Database.getPlayerData(killer, "soupData", "kit"));
        if (!Objects.equals(activeKit, "Glider")) {
            return;
        }
        PlayerInventory inv = killer.getInventory();
        if (inv.contains(Material.ENDER_PEARL) || inv.contains((ItemStack) null)) {
            inv.addItem(getGliderPearl());
        } else {
            inv.setItem(1, getGliderPearl());
        }
    }
}
