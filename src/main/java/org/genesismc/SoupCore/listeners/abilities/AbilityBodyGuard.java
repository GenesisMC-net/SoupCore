package org.genesismc.SoupCore.listeners.abilities;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

public class AbilityBodyGuard implements Listener {

    private static String getOwner(Zombie bodyguard) {
        String[] name = ChatColor.stripColor(bodyguard.getCustomName()).split(" "); // { current, max }
        System.out.println(name[0].replace("'s", ""));
        return name[0].replace("'s", "");
    }

    @EventHandler
    public static void onAttack(EntityDamageByEntityEvent e) {
        if (!Objects.equals(e.getDamager().getWorld().getName(), "world")) return;
        if (e.getDamager() instanceof Zombie && e.getEntity() instanceof Player) { // Do damage from owning player
            Zombie bodyguard = (Zombie) e.getDamager();
            System.out.println(bodyguard.getCustomName());
            System.out.println(bodyguard.getCustomName().contains("Bodyguard"));
            if (!bodyguard.getCustomName().contains("Bodyguard")) return;
            Player target = (Player) e.getEntity();

            String owner = getOwner(bodyguard);
            Player owningPlayer = Bukkit.getPlayer(owner);
            if (owningPlayer == null) return;
            if (owner.equals(target.getName())) {
                e.setCancelled(true);
                owningPlayer.setNoDamageTicks(1);
                return;
            }

            e.setCancelled(true);
            e.setDamage(0);
            if ((target.getHealth() - 4) <= 0) {
                target.damage(20, owningPlayer);
            } else {
                target.damage(0, owningPlayer);
                target.setHealth(target.getHealth() - 4); target.setHealth(target.getHealth() - 4); // dmg 2 hearts ignoring armour
            }
        } else if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player target = (Player) e.getDamager();
            Player owningPlayer = (Player) e.getEntity();

            for (Zombie zombie : target.getWorld().getEntitiesByClass(Zombie.class)) {
                if (!zombie.getCustomName().contains("Bodyguard")) return;

                String owner = getOwner(zombie);

                if (!owner.equals(owningPlayer.getName())) return;
                if (zombie.getTarget() == null || Objects.equals(zombie.getTarget().getName(), owner)) {
                    zombie.setTarget(target);
                }
            }
        }
    }

    @EventHandler
    public static void onDamage(EntityDamageEvent e) {
        if (!e.getEntity().getType().equals(EntityType.ZOMBIE)) return;
        Zombie bodyguard = (Zombie) e.getEntity();
        if (!bodyguard.isCustomNameVisible()) return;

        String[] name = ChatColor.stripColor(bodyguard.getCustomName()).split(" ");
        int hp = Integer.parseInt(name[2].replace("[", "").replace("HP]", ""));

        bodyguard.setCustomName(bodyguard.getCustomName().replace("[" + hp + "HP]", "[" + (hp - e.getDamage()) + "HP]"));
    }

    @EventHandler
    public static void onPlayerLeave(PlayerQuitEvent e) { // Kill any bodyguards
        for (Zombie zombie : Bukkit.getWorld("world").getEntitiesByClass(Zombie.class)) {
            if (!zombie.getCustomName().contains("Bodyguard")) continue;

            if (!getOwner(zombie).equals(e.getPlayer().getName())) return;

            zombie.remove();
        }
    }

    @EventHandler
    public static void onBodyguardDeath(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Zombie)) return;
        if (e.getEntity().getKiller() == null) return;

        Zombie bodyguard = (Zombie) e.getEntity();
        System.out.println(bodyguard.getCustomName());
        if (!bodyguard.getCustomName().contains("Bodyguard")) return;
        Player killer = e.getEntity().getKiller();

        Player owningPlayer = Bukkit.getPlayer(getOwner(bodyguard));
        if (owningPlayer == null) return;

        owningPlayer.sendMessage(ChatColor.AQUA + "Your bodyguard has been killed by " + ChatColor.RED + killer.getName());
    }

    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent e) {
        if (!(e.getEntity().getType() == EntityType.ZOMBIE)) return;
        if (!(e.getTarget() instanceof Player)) return;

        Zombie bodyguard = (Zombie) e.getEntity();
        if (!bodyguard.getCustomName().contains("Bodyguard")) return;

        String owner = getOwner(bodyguard);

        for (Entity entity : bodyguard.getNearbyEntities(10, 10, 10)) {
            if (!entity.getName().equals(owner)) {
                e.setTarget(entity);
                return;
            }
        }
        e.setTarget(Bukkit.getPlayer(owner));
    }
}
