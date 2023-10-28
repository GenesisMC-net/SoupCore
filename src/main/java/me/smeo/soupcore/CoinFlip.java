package me.smeo.soupcore;

import me.smeo.soupcore.Database.Database;
import me.smeo.soupcore.listeners.CoinFlipListeners;
import net.kyori.adventure.platform.facet.Facet;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import javax.xml.crypto.Data;
import java.text.DecimalFormat;
import java.util.*;

public class CoinFlip {

    public static String cfPrefix = ChatColor.YELLOW + "[" + ChatColor.GOLD + "CF" + ChatColor.YELLOW +"] ";

    private static Inventory mainMenu(Player p)
    {
        int wins = Integer.valueOf((String) Objects.requireNonNull(Database.getPlayerData(p, "coinflip", "wins")));
        int losses = Integer.valueOf((String) Objects.requireNonNull(Database.getPlayerData(p, "coinflip", "losses")));
        int moneyMade = Integer.valueOf((String) Objects.requireNonNull(Database.getPlayerData(p, "coinflip", "moneyMade")));
        float percentageWon = (float) wins / (wins + losses) * 100;
        if (wins == 0 && losses == 0) {
            percentageWon = (float) 0;
        } else if (wins == 0) {
            percentageWon = (float) 0;
        } else if (losses == 0) {
            percentageWon = (float) 100;
        }
        DecimalFormat df = new DecimalFormat("#.#");

        Inventory inv = Bukkit.createInventory(null, 36, ChatColor.BLACK + "Coin-flip Games");

        ItemStack prevPage = new ItemStack(Material.PAPER, 1);
        ItemMeta prevPageMeta = prevPage.getItemMeta();
        prevPageMeta.setDisplayName(ChatColor.WHITE + String.valueOf(ChatColor.BOLD) + "Previous Page");
        ArrayList<String> prevPageLore = new ArrayList<>();
        prevPageLore.add(ChatColor.GRAY + "Click to go to the previous page");
        prevPageMeta.setLore(prevPageLore);
        prevPage.setItemMeta(prevPageMeta);

        ItemStack nextPage = new ItemStack(Material.PAPER, 1);
        ItemMeta nextPageMeta = nextPage.getItemMeta();
        nextPageMeta.setDisplayName(ChatColor.WHITE + String.valueOf(ChatColor.BOLD) + "Next Page");
        ArrayList<String> nextPageLore = new ArrayList<>();
        nextPageLore.add(ChatColor.GRAY + "Click to go to the next page");
        nextPageMeta.setLore(nextPageLore);
        nextPage.setItemMeta(nextPageMeta);

        ItemStack stats = new ItemStack(Material.BOOK, 1);
        ItemMeta statsMeta = stats.getItemMeta();
        statsMeta.setDisplayName(ChatColor.GOLD + String.valueOf(ChatColor.BOLD) + "Stats");
        ArrayList<String> statsLore = new ArrayList<>();
        statsLore.add(ChatColor.WHITE + "Statistics from all your games");
        statsLore.add("");
        statsLore.add(ChatColor.YELLOW + " Player: " + ChatColor.WHITE + p.getName());
        statsLore.add("");
        statsLore.add(ChatColor.YELLOW + " Wins: " + ChatColor.WHITE + wins);
        statsLore.add(ChatColor.YELLOW + " Losses: " + ChatColor.WHITE + losses);
        statsLore.add(ChatColor.YELLOW + " Total Profit: " + ChatColor.WHITE + moneyMade);
        statsLore.add(ChatColor.YELLOW + " Win Percentage: " + ChatColor.WHITE + df.format(percentageWon) + "%");
        statsMeta.setLore(statsLore);
        stats.setItemMeta(statsMeta);

        ItemStack newGame = new ItemStack(Material.INK_SACK, 1, DyeColor.LIME.getDyeData());
        ItemMeta newGameMeta = nextPage.getItemMeta();
        newGameMeta.setDisplayName(ChatColor.GREEN + String.valueOf(ChatColor.BOLD) + "Create Game");
        ArrayList<String> newGameLore = new ArrayList<>();
        newGameLore.add(ChatColor.GRAY + "Click here to create a new game!");
        newGameMeta.setLore(newGameLore);
        newGame.setItemMeta(newGameMeta);

        ItemStack emptyList = new ItemStack(Material.BARRIER, 1);
        ItemMeta emptyListMeta = emptyList.getItemMeta();
        emptyListMeta.setDisplayName(ChatColor.RED + String.valueOf(ChatColor.BOLD) + "No Active Games");
        ArrayList<String> emptyListLore = new ArrayList<>();
        emptyListLore.add(ChatColor.GRAY + "Click the lime dye to create a new game");
        emptyListMeta.setLore(emptyListLore);
        emptyList.setItemMeta(emptyListMeta);

        ItemStack stainedGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        inv.setItem(27, stainedGlass);
        inv.setItem(28, stainedGlass);
        inv.setItem(29, stainedGlass);

        inv.setItem(30, prevPage);
        inv.setItem(31, stats);
        inv.setItem(32, nextPage);

        inv.setItem(33, stainedGlass);
        inv.setItem(34, stainedGlass);

        inv.setItem(35, newGame);

        if(Database.getAllActiveCoinFlips() == null) {
            inv.setItem(13, emptyList);
            return inv;
        }

        int i = 0;
        Map<String, Integer> activeWagers = Database.getAllActiveCoinFlips();
        for (String uuid : activeWagers.keySet()) {
            UUID playerUUID = UUID.fromString(uuid);
            OfflinePlayer target = p.getServer().getOfflinePlayer(playerUUID);

            if (!target.isOnline()) { continue; }

            ItemStack targetHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta targetHeadMeta = (SkullMeta) targetHead.getItemMeta();
            targetHeadMeta.setOwner(target.getName());

            targetHeadMeta.setDisplayName(ChatColor.AQUA + target.getName());

            ArrayList<String> targetHeadLore = new ArrayList<>();
            targetHeadLore.add("");
            targetHeadLore.add(ChatColor.YELLOW + String.valueOf(ChatColor.BOLD) + "Wager: ");
            targetHeadLore.add(ChatColor.WHITE + " " + activeWagers.get(uuid) + " credits");
            targetHeadLore.add("");
            if (p.getUniqueId() == target.getUniqueId()) {
                targetHeadLore.add(ChatColor.GRAY + "Click here to" + ChatColor.RED + ChatColor.BOLD + " DELETE " + ChatColor.RESET + ChatColor.GRAY + "your bet");
            } else {
                targetHeadLore.add(ChatColor.GRAY + "Click here to" + ChatColor.GREEN + ChatColor.BOLD + " ACCEPT " + ChatColor.RESET + ChatColor.GRAY + "the bet");
            }

            targetHeadMeta.setLore(targetHeadLore);
            targetHead.setItemMeta(targetHeadMeta);

            inv.setItem(i, targetHead);
            i++;
        }
        if (i == 0) {
            inv.setItem(13, emptyList);
            return inv;
        }

        return inv;
    }

    private static void coinFlipInvState(Player p, DyeColor dyeColor, Inventory inv) {
        ItemStack playerSkull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta playerSkullMeta = (SkullMeta) playerSkull.getItemMeta();
        playerSkullMeta.setDisplayName(ChatColor.YELLOW + p.getName());
        playerSkullMeta.setOwner(p.getName());
        playerSkull.setItemMeta(playerSkullMeta);
        inv.setItem(13, playerSkull);

        ItemStack wool = new ItemStack(Material.WOOL, 1, dyeColor.getWoolData());
        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, dyeColor.getData());

        inv.setItem(3, wool);
        inv.setItem(4, wool);
        inv.setItem(5, wool);

        inv.setItem(12, wool);
        inv.setItem(14, wool);

        inv.setItem(21, wool);
        inv.setItem(22, wool);
        inv.setItem(23, wool);

        for (int i = 0; i < 27; i++) {
            if ((inv.getContents()[i] == null) || (inv.getContents()[i].getType() == Material.STAINED_GLASS_PANE))
            {
                inv.setItem(i, glass);
            }
        }
    }

    public static void playCoinFlip(Player acceptor, Player better) {
        int bet = 2 * Integer.valueOf((String) Objects.requireNonNull(Database.getPlayerData(better, "coinflip", "activeWager")));

        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.BLACK + "Flipping coin...");
        acceptor.closeInventory();
        acceptor.openInventory(inv);
        coinFlipInvState(acceptor, DyeColor.LIME, inv);

        Random rd = new Random();
        boolean acceptorWins = rd.nextBoolean();
        Player winner;
        Player loser;
        if (acceptorWins) {
            winner = acceptor;
            loser = better;
        } else {
            winner = better;
            loser = acceptor;
        }

        System.out.println(acceptor.getName() + " | " + winner.getName());

        Random rand = new Random();
        int stopTime = rand.nextInt(4) + 7;
        final int[] i = {0};
        System.out.println(stopTime);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!inv.getViewers().contains(acceptor)) { this.cancel(); return; }
                System.out.println(i[0]);

                if (i[0] >= stopTime) {
                    if (acceptorWins) {
                        coinFlipInvState(acceptor, DyeColor.LIME, inv);
                        acceptor.playSound(acceptor.getLocation(), Sound.LEVEL_UP, 10, 2);
                        if (acceptor.getServer().getOnlinePlayers().contains(better)) {
                            better.playSound(acceptor.getLocation(), Sound.ANVIL_LAND, 10, 1);
                        }
                    } else {
                        coinFlipInvState(better, DyeColor.RED, inv);
                        acceptor.playSound(acceptor.getLocation(), Sound.ANVIL_LAND, 10, 1);
                        if (acceptor.getServer().getOnlinePlayers().contains(better)) {
                            better.playSound(acceptor.getLocation(), Sound.LEVEL_UP, 10, 2);
                        }
                    }
                    int winnerBalance = Integer.valueOf((String) Objects.requireNonNull(Database.getPlayerData(winner, "soupData", "credits")));
                    acceptor.getServer().broadcastMessage(cfPrefix + ChatColor.GREEN + winner.getName() + ChatColor.WHITE + " beat " + ChatColor.RED + loser.getName() + ChatColor.WHITE + " in a Coin Flip worth " + ChatColor.YELLOW + bet + ChatColor.WHITE + " credits");

                    int winnerProfit = Integer.valueOf((String) Objects.requireNonNull(Database.getPlayerData(winner, "coinflip", "moneyMade")));
                    int loserProfit = Integer.valueOf((String) Objects.requireNonNull(Database.getPlayerData(loser, "coinflip", "moneyMade")));
                    int winnerWins = Integer.valueOf((String) Objects.requireNonNull(Database.getPlayerData(winner, "coinflip", "wins")));
                    int loserLosses = Integer.valueOf((String) Objects.requireNonNull(Database.getPlayerData(loser, "coinflip", "losses")));

                    Database.SetPlayerData(winner, "soupData", "credits", winnerBalance + (bet * 2));

                    Database.SetPlayerData(winner, "coinflip", "wins", winnerWins + 1);
                    Database.SetPlayerData(loser, "coinflip", "losses", loserLosses + 1);

                    Database.SetPlayerData(winner, "coinflip", "moneyMade", winnerProfit + bet);
                    Database.SetPlayerData(loser, "coinflip", "moneyMade", loserProfit - bet);


                    Database.SetPlayerData(better, "coinflip", "activeWager", 0);
                    this.cancel();
                    return;
                }
                acceptor.playSound(acceptor.getLocation(), Sound.WOOD_CLICK, 10, 1);
                if (i[0] % 2 == 0) {
                    coinFlipInvState(acceptor, DyeColor.LIME, inv);
                } else {
                    coinFlipInvState(better, DyeColor.RED, inv);
                }
                i[0]++;
            }
        }.runTaskTimer(SoupCore.plugin, 0L, 10L);

    }

    public static void newGame(Player p)
    {
        if (Integer.valueOf((String) Objects.requireNonNull(Database.getPlayerData(p, "coinflip", "activeWager"))) > 0) {
            p.playSound(p.getLocation(), Sound.ANVIL_LAND, 10, 1);
            p.sendMessage(ChatColor.RED + "You already have a coin flip active! Click the icon in /cf to delete your wager");
            p.closeInventory();
            return;
        } else if (Integer.valueOf((String) Objects.requireNonNull(Database.getPlayerData(p, "soupData", "credits"))) == 0) {
            p.playSound(p.getLocation(), Sound.ANVIL_LAND, 10, 1);
            p.sendMessage(ChatColor.RED + "You do not have enough credits to make a wager!");
            p.closeInventory();
            return;
        }

        p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
        p.closeInventory();

        CoinFlipListeners.awaitingNewGameResponse.add(p.getUniqueId());
        p.sendMessage(ChatColor.GREEN + "Please enter the amount of credits you wish to wager." + ChatColor.RED + "\n(\"cancel\" to Cancel)");
    }

    public static void openGui(Player p, String menuName)
    {
        switch (menuName){
            case "main":
                p.openInventory(mainMenu(p));
                break;
        }
    }
}
