package org.genesismc.SoupCore.listeners;

import org.bukkit.*;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.genesismc.SoupCore.Database.Database;
import org.genesismc.SoupCore.Duels;
import org.genesismc.SoupCore.Kits.*;
import org.genesismc.SoupCore.SoupCore;

import java.util.*;

import static org.genesismc.SoupCore.Duels.*;
import static org.genesismc.SoupCore.commands.spawnCommand.teleportToSpawn;

public class duelListeners implements Listener {

    private static void invalidSelection(Player p, Inventory inv, ItemStack original, int slot, String title, String text) {
        p.playSound(p.getLocation(), Sound.ANVIL_LAND, 0.5F, 1);

        ItemStack offlineItem = new ItemStack(Material.BARRIER, 1);
        ItemMeta offlineItemMeta = offlineItem.getItemMeta();

        offlineItemMeta.setDisplayName(ChatColor.RED + String.valueOf(ChatColor.BOLD) + title);

        ArrayList<String> offlineItemLore = new ArrayList<>();
        offlineItemLore.add(ChatColor.GRAY + text);
        offlineItemMeta.setLore(offlineItemLore);
        offlineItem.setItemMeta(offlineItemMeta);

        inv.setItem(slot, offlineItem);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (inv.getViewers().contains(p)) {
                    inv.setItem(slot, original);
                    Duels.duelGui(p, 1);
                }
            }
        }.runTaskLater(SoupCore.plugin, 20L * 2L);
    }

    private static void duelSelection(Inventory inv, Player p, ItemStack item, int slot) {
        if (item.getItemMeta() == null) return;
        if (item.getItemMeta().getDisplayName() == null) return;

        String action = item.getItemMeta().getDisplayName();

        if (item.getType() == Material.PAPER) {
            String[] pageInfo = action.split(" ")[1].split("/"); // { current, max }
            int currentPage = Integer.parseInt(pageInfo[0].replace("(", ""));
            int maxPages = Integer.parseInt(pageInfo[0].replace(")", ""));
            if (currentPage == maxPages) return;

            if (action.contains("Next Page")) {
                duelGui(p, currentPage + 1);
            } else if (action.contains("Previous Page")) {
                if (currentPage == 1) return;
                duelGui(p, currentPage - 1);
            }
            return;
        }

        if (item.getType() == Material.SKULL_ITEM) {
            SkullMeta targetMeta = (SkullMeta) item.getItemMeta();

            OfflinePlayer target = Bukkit.getOfflinePlayer(targetMeta.getOwner());
            if (target == null || !target.isOnline()) {
                invalidSelection(p, inv, item, slot, "Player is offline", "This player is no longer online");
                return;
            }

            String duelsEnabled = Database.getPlayerData(target.getPlayer(), "duelData", "duelsEnabled");
            if (Objects.equals(duelsEnabled, "false")) {
                invalidSelection(p, inv, item, slot, "Could not send request", "This player is no longer accepting duel requests");
                return;
            }

            p.closeInventory();
            Bukkit.dispatchCommand(p, "duel " + target.getName());
        }
    }

    private static void kitSelection(Player p, ItemStack item) {
        if (item.getItemMeta() == null) return;
        if (item.getItemMeta().getDisplayName() == null) return;

        String selectedKit = item.getItemMeta().getDisplayName();

        Player otherPlayer = Bukkit.getPlayer(activeDuels.get(p.getUniqueId()));

        Methods_Kits.giveKit(p, ChatColor.stripColor(selectedKit));
        Methods_Kits.giveKit(otherPlayer, ChatColor.stripColor(selectedKit));
        PotionEffect speedOne = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0);

        if (!p.hasPotionEffect(PotionEffectType.SPEED)) {
            p.addPotionEffect(speedOne);
            otherPlayer.addPotionEffect(speedOne);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                p.closeInventory();
            }
        }.runTaskLater(SoupCore.plugin, 0L);
        beginFight(p, otherPlayer, selectedKit);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Inventory inv = e.getClickedInventory();
        if (inv == null) return;
        if (inv.getTitle() == null) return;
        if (inv.getType() == InventoryType.PLAYER) return;

        Player p = (Player) e.getWhoClicked();

        ItemStack item = inv.getItem(e.getSlot());
        if (item == null) return;

        String title = ChatColor.stripColor(e.getView().getTitle());
        if (Objects.equals(title, "Duels")) {
            e.setCancelled(true);
            duelSelection(inv, p, item, e.getSlot());
        } else if (Objects.equals(title, "Duel Kit Selection")) {
            e.setCancelled(true);
            kitSelection(p, item);
        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        if (!awaitingStart.containsKey(e.getPlayer().getUniqueId())) return;
        if (!Objects.equals(e.getInventory().getTitle(), "Duel Kit Selection")) return;
        if (e.getPlayer().getInventory().getItemInHand() == null || e.getPlayer().getInventory().getItemInHand().getType() == Material.AIR) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    e.getPlayer().openInventory(e.getInventory());
                }
            }.runTaskLater(SoupCore.plugin, 0L);
        }
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
            p.setFlying(false);
            awaitingRematch.remove(p.getUniqueId());
            showAllPlayers(p);
            teleportToSpawn(p);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (awaitingStart.containsKey(p.getUniqueId()) || awaitingStart.containsValue(p.getUniqueId())) {
            e.setTo(e.getFrom());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player loser = e.getEntity();
        Player killer = loser.getKiller();
        if (!(activeDuels.containsKey(loser.getUniqueId()) || activeDuels.containsValue(loser.getUniqueId()))) {
            return;
        }
        if (killer == null) killer = Bukkit.getPlayer(activeDuels.get(loser.getUniqueId()));
        if (killer == null) {
            for (UUID uuid : activeDuels.keySet()) {
                if (activeDuels.get(uuid) == loser.getUniqueId()) {
                    killer = Bukkit.getPlayer(uuid);
                }
            }
        }
        if (killer == null) return;
        activeDuels.remove(loser.getUniqueId());
        activeDuels.remove(killer.getUniqueId());

        Player finalKiller = killer;
        new BukkitRunnable()
        {
            @Override
            public void run() {
                loser.spigot().respawn();
                finalKiller.getInventory().clear();
                loser.getInventory().clear();
                finalKiller.setExp(0);
                loser.setExp(0);
                finalKiller.setHealth(20);
                loser.setHealth(20);

                loser.getWorld().strikeLightningEffect(loser.getLocation());

                endFight(finalKiller, loser);
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
        awaitingRematch.remove(p.getUniqueId());
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
