package org.genesismc.SoupCore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.genesismc.SoupCore.Kits.*;
import org.genesismc.SoupCore.SoupCore;

import java.util.*;

import static org.genesismc.SoupCore.Duels.*;
import static org.genesismc.SoupCore.commands.spawnCommand.teleportToSpawn;

public class duelListeners implements Listener {
    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Inventory inv = e.getClickedInventory();
        if (inv == null) return;
        if (inv.getTitle() == null) return;
        if (inv.getType() == InventoryType.PLAYER) return;
        if (!Objects.equals(inv.getTitle(), "Duel Kit Selection")) {
            return;
        }
        e.setCancelled(true);

        Player p = (Player) e.getWhoClicked();
        if (!activeDuels.containsKey(p.getUniqueId())) return;

        ItemStack clicked = inv.getItem(e.getSlot());
        if (clicked == null) return;
        if (clicked.getItemMeta()==null) return;
        if (clicked.getItemMeta().getDisplayName() == null) return;
        String selectedKit = e.getCurrentItem().getItemMeta().getDisplayName();

        Player otherPlayer = Bukkit.getPlayer(activeDuels.get(p.getUniqueId()));

        switch (ChatColor.stripColor(selectedKit)) {
            case "Default":
                KitDefault.giveItems(p);
                KitDefault.giveItems(otherPlayer);
                break;
        }
        Methods_Kits.giveSoup(p);
        Methods_Kits.giveSoup(otherPlayer);

        choosingKit.remove(p.getUniqueId());
        new BukkitRunnable() {
            @Override
            public void run() {
                p.closeInventory();
            }
        }.runTaskLater(SoupCore.plugin, 0L);
        beginFight(p, otherPlayer, selectedKit);
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        if (!choosingKit.contains(e.getPlayer().getUniqueId())) return;
        if (!Objects.equals(e.getInventory().getName(), "Duel Kit Selection")) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                e.getPlayer().openInventory(e.getInventory());
            }
        }.runTaskLater(SoupCore.plugin, 0L);
    }

    @EventHandler
    public void onHotbarClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getItemInHand() == null || !p.getItemInHand().hasItemMeta()) return;
        String itemName = ChatColor.stripColor(p.getItemInHand().getItemMeta().getDisplayName());
        if (Objects.equals(itemName, "Rematch"))
        {
            e.setCancelled(true);
            rematch(p);
        } else if (Objects.equals(itemName, "Exit to Spawn")) {
            e.setCancelled(true);
            showAllPlayers(p);
            teleportToSpawn(p);
            awaitingRematch.remove(p.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (activeDuels.containsKey(p.getUniqueId()) || activeDuels.containsValue(p.getUniqueId())) {
            e.setTo(e.getFrom());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player loser = e.getEntity();
        Player killer = loser.getKiller();
        if (killer == null) return;
        if (!(activeDuels.containsKey(loser.getUniqueId()) || activeDuels.containsValue(loser.getUniqueId()))) {
            return;
        }
        activeDuels.remove(loser.getUniqueId());
        activeDuels.remove(killer.getUniqueId());

        new BukkitRunnable()
        {
            @Override
            public void run() {
                loser.spigot().respawn();
                killer.getInventory().clear();
                loser.getInventory().clear();
                killer.setExp(0);
                loser.setExp(0);
                killer.setHealth(20);
                loser.setHealth(20);

                loser.getWorld().strikeLightningEffect(loser.getLocation());

                endFight(killer, loser);
            }
        }.runTaskLater(SoupCore.plugin, 1L);
    }

    @EventHandler
    public void playerAttack(EntityDamageByEntityEvent e) {
        if (awaitingRematch.containsKey(e.getDamager().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onLightningStrike(EntityDamageEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.LIGHTNING) return;
        if (!(e.getEntity() instanceof Player)) return;
        if (Objects.equals(e.getEntity().getWorld().getName(), "world")) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Player target;
        if (activeDuels.containsKey(p.getUniqueId())) {
            target = Bukkit.getPlayer(activeDuels.get(p.getUniqueId()));
            if (target == null) return;
            opponentLeft(target);
            return;
        }
        for (Map.Entry<UUID, UUID> entry : activeDuels.entrySet()) {
            if (entry.getValue() == p.getUniqueId()) {
                target = Bukkit.getPlayer(activeDuels.get(p.getUniqueId()));
                if (target == null) return;
                opponentLeft(target);
                return;
            }
        }
    }
}
