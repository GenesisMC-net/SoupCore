package org.genesismc.SoupCore.listeners;

import org.bukkit.OfflinePlayer;
import org.genesismc.SoupCore.CoinFlip;
import org.genesismc.SoupCore.Database.Database;
import org.genesismc.SoupCore.SoupCore;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CoinFlipListeners implements Listener {

    public static final List<UUID> awaitingNewGameResponse = new ArrayList<>();

    @EventHandler
    public void onPlayerMessage(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (!awaitingNewGameResponse.contains(p.getUniqueId())) {
            return;
        }

        e.setCancelled(true);

        if (ChatColor.stripColor(e.getMessage()).contains("cancel")) {
            awaitingNewGameResponse.remove(p.getUniqueId());
            p.sendMessage(ChatColor.GRAY + "Cancelled coin flip creation");
            return;
        }

        int wagerAmount;
        try {
            wagerAmount = Integer.parseInt(ChatColor.stripColor(e.getMessage()));
        } catch (NumberFormatException nfe) {
            p.sendMessage(ChatColor.RED + "Invalid number. Please try again \n(\"cancel\" to Cancel)");
            return;
        }

        if (wagerAmount <= 0) {
            p.playSound(p.getLocation(), Sound.ANVIL_LAND, 0.5F, 1);
            p.sendMessage(ChatColor.RED + "Your wager is too small! Please try again \n(\"cancel\" to Cancel)");
            return;
        }

        int currentCredits = Integer.parseInt(Objects.requireNonNull(Database.getPlayerData(p, "soupData", "credits")));
        if (currentCredits < wagerAmount) {
            p.playSound(p.getLocation(), Sound.ANVIL_LAND, 0.5F, 1);
            p.sendMessage(ChatColor.RED + "Insufficient funds! You require " + ChatColor.GREEN + (wagerAmount - currentCredits) + ChatColor.RED + " more credits to complete this action");
            awaitingNewGameResponse.remove(p.getUniqueId());
            return;
        }

        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 0.5F, 0);
        p.sendMessage(ChatColor.GRAY + "You created a new coin flip for " + ChatColor.GREEN + wagerAmount + ChatColor.GRAY + " credits");

        Database.setPlayerData(p, "soupData", "credits", String.valueOf(currentCredits - wagerAmount));
        Database.setPlayerData(p, "coinflip", "activeWager", String.valueOf(wagerAmount));
        awaitingNewGameResponse.remove(p.getUniqueId());
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().contains("Flipping coin...")) {
            e.setCancelled(true);
            return;
        }
        if (!e.getView().getTitle().contains("Coin-flip Games")) {
            return;
        }
        if (e.getCurrentItem() == null) {
            return;
        }
        if (e.getCurrentItem().getItemMeta() == null) {
            return;
        }

        Player p = (Player) e.getWhoClicked();
        e.setCancelled(true);
        if (e.getClickedInventory().getType() == InventoryType.PLAYER) {
            return;
        }

        Inventory inv = p.getOpenInventory().getTopInventory();

        System.out.println(e.getSlot());

        if (e.getSlot() < 27) {
            ItemStack clickedGame = inv.getItem(e.getSlot());
            SkullMeta clickedGameMeta = (SkullMeta) clickedGame.getItemMeta();
            if (Objects.equals(clickedGameMeta.getOwner(), p.getName())) {
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 0.75F, 0);
                int currentWager = Integer.parseInt(Objects.requireNonNull(Database.getPlayerData(p, "coinflip", "activeWager")));
                int currentCredits = Integer.parseInt(Objects.requireNonNull(Database.getPlayerData(p, "soupData", "credits")));
                p.sendMessage(ChatColor.GRAY + "You deleted your coin flip game with a wager of " + ChatColor.GREEN + currentWager + ChatColor.GRAY + " credits");
                p.closeInventory();
                Database.setPlayerData(p, "soupData", "credits", String.valueOf(currentCredits + currentWager));
                Database.setPlayerData(p, "coinflip", "activeWager", "0");
                return;
            }

            OfflinePlayer target = p.getServer().getOfflinePlayer(clickedGameMeta.getOwner());

            if (!target.isOnline()) {
                p.playSound(p.getLocation(), Sound.ANVIL_LAND, 0.5F, 1);

                ItemStack offlineItem = new ItemStack(Material.BARRIER, 1);
                ItemMeta offlineItemMeta = offlineItem.getItemMeta();

                offlineItemMeta.setDisplayName(ChatColor.RED + String.valueOf(ChatColor.BOLD) + "Player is Offline");

                ArrayList<String> offlineItemLore = new ArrayList<>();
                offlineItemLore.add(ChatColor.GRAY + "This player is no longer online. Please reopen this menu and try again");
                offlineItemMeta.setLore(offlineItemLore);
                offlineItem.setItemMeta(offlineItemMeta);

                inv.setItem(e.getSlot(), offlineItem);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (inv.getViewers().contains(p)) {
                            inv.setItem(e.getSlot(), clickedGame);
                        }
                    }
                }.runTaskLater(SoupCore.plugin, 20L * 2L);
                return;
            }

            int accepterBalance = Integer.parseInt(Objects.requireNonNull(Database.getPlayerData(p, "soupData", "credits")));
            int bet = Integer.parseInt(Objects.requireNonNull(Database.getPlayerData(target.getPlayer(), "coinflip", "activeWager")));

            if (bet <= 0) {
                p.playSound(p.getLocation(), Sound.ANVIL_LAND, 0.5F, 1);

                ItemStack noFundsItem = new ItemStack(Material.BARRIER, 1);
                ItemMeta noFundsItemMeta = noFundsItem.getItemMeta();

                noFundsItemMeta.setDisplayName(ChatColor.RED + String.valueOf(ChatColor.BOLD) + "Error");

                ArrayList<String> noFundsItemLore = new ArrayList<>();
                noFundsItemLore.add(ChatColor.GRAY + "This game no longer exists!");
                noFundsItemMeta.setLore(noFundsItemLore);
                noFundsItem.setItemMeta(noFundsItemMeta);

                inv.setItem(e.getSlot(), noFundsItem);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (inv.getViewers().contains(p)) {
                            inv.setItem(e.getSlot(), null);
                        }
                    }
                }.runTaskLater(SoupCore.plugin, 20L * 2L);
                return;
            } else if (accepterBalance < bet) {
                p.playSound(p.getLocation(), Sound.ANVIL_LAND, 0.5F, 1);

                ItemStack noFundsItem = new ItemStack(Material.BARRIER, 1);
                ItemMeta noFundsItemMeta = noFundsItem.getItemMeta();

                noFundsItemMeta.setDisplayName(ChatColor.RED + String.valueOf(ChatColor.BOLD) + "Insufficient Funds");

                ArrayList<String> noFundsItemLore = new ArrayList<>();
                noFundsItemLore.add(ChatColor.GRAY + "You require " + ChatColor.RED + (bet - accepterBalance) + ChatColor.GRAY + " more credits!");
                noFundsItemMeta.setLore(noFundsItemLore);
                noFundsItem.setItemMeta(noFundsItemMeta);

                inv.setItem(e.getSlot(), noFundsItem);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (inv.getViewers().contains(p)) {
                            inv.setItem(e.getSlot(), clickedGame);
                        }
                    }
                }.runTaskLater(SoupCore.plugin, 20L * 2L);
                return;
            }
            CoinFlip.playCoinFlip(p, target.getPlayer());
            Database.setPlayerData(p, "soupData", "credits", String.valueOf((Integer.parseInt(Objects.requireNonNull(Database.getPlayerData(p, "soupData", "credits"))) - bet))); // What a fucking mess LMAO

        } else if (e.getSlot() == 35) {
            CoinFlip.newGame(p);
//        } else if (e.getSlot() == 30) {
//            // TODO previous page
//        } else if (e.getSlot() == 32) {
//            // TODO next page
        }
    }
}
