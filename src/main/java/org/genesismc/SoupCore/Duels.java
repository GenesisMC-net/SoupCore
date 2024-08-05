package org.genesismc.SoupCore;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.genesismc.SoupCore.Database.Database;

import java.text.DecimalFormat;
import java.util.*;

import org.genesismc.SoupCore.Kits.*;

import static org.genesismc.SoupCore.commands.spawnCommand.teleportToSpawn;

public class Duels {

    public static HashMap<UUID, UUID> activeDuelRequests = new HashMap<>();
    public static HashMap<UUID, UUID> activeDuels = new HashMap<>();
    public static ArrayList<UUID> choosingKit = new ArrayList<>();
    public static HashMap<UUID, UUID> awaitingRematch = new HashMap<>();

    // Formatting
    private static final String dash = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----";

    public static void duelHelpMsg(Player player) {
        HashMap<String, String> commandUsage = new HashMap<String, String>() {{
            put("/duel <player>", "Request a duel with another player");
            put("/duel stats [player]", "View your or another player's stats");
            put("/duel accept <player>", "Accept a duel request from another player");
            put("/duel deny <player>", "Deny a duel request from another player");
            put("/duel cancel", "Cancel your duel request");
            put("/duel toggle", "Disable duel requests");
        }};

        player.sendMessage(dash + ChatColor.YELLOW + " Duels " + dash);
        player.sendMessage(ChatColor.GRAY + "<> required, [] optional");

        for (Map.Entry<String, String> line : commandUsage.entrySet()) {
            TextComponent text = new TextComponent(line.getKey());
            text.setColor(ChatColor.YELLOW);
            text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(line.getValue()).color(ChatColor.GRAY).create()));
            text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, line.getKey()));
            player.spigot().sendMessage(text);
        }
    }

    public static void toggleDuels(Player player) {
        if (Objects.equals(Database.getPlayerData(player, "duelData", "duelsEnabled"), "true")) {
            Database.setPlayerData(player, "duelData", "duelsEnabled", "false");
            player.sendMessage(ChatColor.GRAY + "You have " + ChatColor.RED + "disabled" + ChatColor.GRAY + " duel requests");
        } else {
            Database.setPlayerData(player, "duelData", "duelsEnabled", "true");
            player.sendMessage(ChatColor.GRAY + "You have " + ChatColor.GREEN + "enabled" + ChatColor.GRAY + " duel requests");
        }
    }

    public static void duelStats(Player p, Player target) {
        String name = target.getDisplayName() + "'s";
        if (p == target) { name = "Your"; }
        String duelsEnabled = Database.getPlayerData(target, "duelData", "duelsEnabled");
        String duelRequests;
        if (Objects.equals(duelsEnabled, "true")) {
            duelRequests = ChatColor.GREEN + "Enabled";
        } else {
            duelRequests = ChatColor.RED + "Disabled";
        }
        int wins = Integer.parseInt(Objects.requireNonNull(Database.getPlayerData(target, "duelData", "wins")));
        int losses = Integer.parseInt(Objects.requireNonNull(Database.getPlayerData(target, "duelData", "losses")));

        if (losses < 1){ losses = 1; }
        float wlr = (float) wins / losses;
        final DecimalFormat df = new DecimalFormat("0.00");
        String wlrFormatted;
        if (wlr < 1) {
            wlrFormatted = ChatColor.RED + df.format(wlr);
        } else {
            wlrFormatted = ChatColor.GREEN + df.format(wlr);
        }

        p.sendMessage(dash + " " + ChatColor.YELLOW + name + " Duel Stats " + dash);
        p.sendMessage("");
        p.sendMessage(ChatColor.YELLOW + "Duel Requests: " + duelRequests);
        p.sendMessage(ChatColor.YELLOW + "Wins: " + ChatColor.GREEN + wins);
        p.sendMessage(ChatColor.YELLOW + "Losses: " + ChatColor.RED + losses);
        p.sendMessage(ChatColor.YELLOW + "W/L Ratio: " + wlrFormatted);
    }

    public static void duelRequest(Player requester, Player target) {
        // The player who sent the request
        requester.sendMessage(ChatColor.GRAY + "You have challenged " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', target.getDisplayName()) + ChatColor.GRAY + " to a duel");

        TextComponent cancel = new TextComponent("[CANCEL]");
        cancel.setColor(ChatColor.RED);
        cancel.setBold(true);
        cancel.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Cancel the request").color(ChatColor.RED).create()));
        cancel.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel cancel"));

        requester.spigot().sendMessage(cancel);

        // The player who received the request
        target.sendMessage(ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', requester.getDisplayName()) + ChatColor.GRAY + " has challenged you to a duel!");

        TextComponent acceptDeny = new TextComponent("[ACCEPT]");
        acceptDeny.setColor(ChatColor.GREEN);
        acceptDeny.setBold(true);
        acceptDeny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Accept the request").color(ChatColor.GREEN).create()));
        acceptDeny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel accept " + requester.getName()));

        TextComponent deny = new TextComponent("[DENY]");
        deny.setColor(ChatColor.RED);
        deny.setBold(true);
        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Deny the request").color(ChatColor.RED).create()));
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel deny " + requester.getName()));

        acceptDeny.addExtra(" ");
        acceptDeny.addExtra(deny);
        target.spigot().sendMessage(acceptDeny);

        activeDuelRequests.put(requester.getUniqueId(), target.getUniqueId());
    }

    private static String generateRandomMap() {
        FileConfiguration config = SoupCore.plugin.getConfig();
        ConfigurationSection duelMaps = config.getConfigurationSection("duel-maps");
        Object[] mapArray = duelMaps.getKeys(false).toArray();

        ArrayList<String> enabledMaps = new ArrayList<>();
        for (Object map : mapArray) {
            String name = map.toString();
            boolean enabled = config.getBoolean("duel-maps." + name + ".enabled");
            if (!enabled) { continue; }
            enabledMaps.add(name);
        }

        Random rand = new Random();
        return enabledMaps.get(rand.nextInt(enabledMaps.size() - 1));
    }

    public static void initialiseDuel(Player requester, Player acceptor) {
        awaitingRematch.remove(requester.getUniqueId(), acceptor.getUniqueId());
        awaitingRematch.remove(acceptor.getUniqueId(), requester.getUniqueId());
        for (Player online : Bukkit.getOnlinePlayers()) {
            requester.hidePlayer(online);
            acceptor.hidePlayer(online);
        }
        requester.showPlayer(acceptor);
        acceptor.showPlayer(requester);

        FileConfiguration config = SoupCore.plugin.getConfig();

        String map = generateRandomMap();
        String path = "duel-maps." + map + ".";
        World world = Bukkit.getWorld(config.getString(path + "world-name"));

        String[] co_ords = config.getString(path + "pos1").split(", ");
        Location pos1 = new Location(world,
                Double.parseDouble(co_ords[0] + 0.5),
                Double.parseDouble(co_ords[1]),
                Double.parseDouble(co_ords[2] + 0.5),
                Float.parseFloat(co_ords[3]),
                0);
        co_ords = config.getString(path + "pos2").split(", ");
        Location pos2 = new Location(world,
                Double.parseDouble(co_ords[0] + 0.5),
                Double.parseDouble(co_ords[1]),
                Double.parseDouble(co_ords[2] + 0.5),
                Float.parseFloat(co_ords[3]),
                0);

        requester.teleport(pos1.getBlock().getLocation());
        acceptor.teleport(pos2.getBlock().getLocation());

        requester.getInventory().clear();
        acceptor.getInventory().clear();

        activeDuelRequests.remove(requester.getUniqueId());
        activeDuels.put(requester.getUniqueId(), acceptor.getUniqueId());

        requester.setWalkSpeed(0.0F);
        acceptor.setWalkSpeed(0.0F);

        PotionEffect noJump = new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 255, false, false);
        requester.addPotionEffect(noJump);
        acceptor.addPotionEffect(noJump);

        chooseDuelKit(requester);
        choosingKit.add(requester.getUniqueId());
        acceptor.sendMessage("");
        acceptor.sendMessage(dash + ChatColor.YELLOW + " Duel " + dash);
        acceptor.sendMessage(ChatColor.YELLOW + "Waiting for " + ChatColor.translateAlternateColorCodes('&', requester.getDisplayName()) + ChatColor.GRAY + " to choose the kit");
    }

    private static String enabledDuelKits() {
        return "";
    }

    public static void chooseDuelKit(Player decider) {
        Inventory inv = Bukkit.createInventory(null, 9,"Duel Kit Selection");

        ItemStack item = KitDefault.guiAppearance(decider);
        item.setItemMeta(null);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(org.bukkit.ChatColor.WHITE + "Default");
        List<String> lore = new ArrayList<>();
        lore.add(org.bukkit.ChatColor.GREEN + "Click to select");

        meta.setLore(lore);
        item.setItemMeta(meta);

        inv.setItem(0, item);

        decider.openInventory(inv);
    }

    public static void beginFight(Player p1, Player p2, String kit) {
        for (Player p : new Player[]{p1, p2}) {
            p.setFlying(false);

            p.sendMessage("");
            if (p == p1) {
                p.sendMessage(ChatColor.GRAY + "You have chosen the " + ChatColor.WHITE + kit + ChatColor.GRAY + " kit");
            } else {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', p1.getDisplayName()) + ChatColor.GRAY + " has chosen the " + ChatColor.WHITE + kit + ChatColor.GRAY + " kit");
            }
            p.sendMessage("");

            final int[] i = {0};
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (i[0] >= 5) {
                        p.setWalkSpeed(0.2F);
                        p.removePotionEffect(PotionEffectType.JUMP);
                        p.playSound(p.getLocation(), Sound.NOTE_PLING , 1, 2);
                        p.sendMessage(ChatColor.GREEN + "Fight!");

                        this.cancel();
                        return;
                    }
                    p.sendMessage(ChatColor.GRAY + "Beginning fight in " + ChatColor.RED + (5-i[0]));
                    p.playSound(p.getLocation(), Sound.NOTE_PLING, 1, 0);
                    i[0]++;
                }
            }.runTaskTimer(SoupCore.plugin, 0L, 20L);
        }
    }

    public static void showAllPlayers(Player p) {
        for (Player hidden : p.spigot().getHiddenPlayers()) {
            p.showPlayer(hidden);
        }
    }

    private static void rematchInventory(Player p) {
        Inventory inv = p.getInventory();

        ItemStack rematch = new ItemStack(Material.INK_SACK, 1, DyeColor.LIME.getDyeData());
        ItemMeta rematchMeta = rematch.getItemMeta();
        rematchMeta.setDisplayName(ChatColor.GREEN + "Rematch");
        rematch.setItemMeta(rematchMeta);

        ItemStack quit = new ItemStack(Material.REDSTONE, 1);
        ItemMeta quitMeta = quit.getItemMeta();
        quitMeta.setDisplayName(ChatColor.RED + "Exit to Spawn");
        quit.setItemMeta(quitMeta);

        inv.setItem(3, rematch);
        inv.setItem(5, quit);
    }

    public static void endFight(Player winner, Player loser) {
        for (Player p : new Player[]{winner, loser}) {
            p.setAllowFlight(true);
            p.setFlying(true);
            p.teleport(p.getWorld().getSpawnLocation());
            rematchInventory(p);
            p.sendMessage("");
            duelStats(p, p);
        }
        winner.hidePlayer(loser);
        loser.hidePlayer(winner);
        awaitingRematch.put(winner.getUniqueId(), loser.getUniqueId());
        awaitingRematch.put(loser.getUniqueId(), winner.getUniqueId());

        int wins = Integer.parseInt(Objects.requireNonNull(Database.getPlayerData(winner, "duelData", "wins")));
        int losses = Integer.parseInt(Objects.requireNonNull(Database.getPlayerData(loser, "duelData", "losses")));

        Database.setPlayerData(winner, "duelData", "wins", String.valueOf(wins + 1));
        Database.setPlayerData(loser, "duelData", "losses", String.valueOf(losses + 1));

        winner.sendMessage(ChatColor.GREEN + "You have won the duel!");
        loser.sendMessage(ChatColor.RED + "You have lost the duel");
    }

    public static void opponentLeft(Player p) {
        p.sendMessage(ChatColor.RED + "Your opponent has left the game!");
        p.sendMessage(ChatColor.GRAY + "Sending you to spawn...");

        activeDuels.remove(p.getUniqueId());
        activeDuels.values().remove(p.getUniqueId());
        awaitingRematch.remove(p.getUniqueId());
        awaitingRematch.values().remove(p.getUniqueId());

        new BukkitRunnable() {
            @Override
            public void run() {
                showAllPlayers(p);
                teleportToSpawn(p);
            }
        }.runTaskLater(SoupCore.plugin, 20L * 3L);
    }

    public static void rematch(Player p) {
        if (activeDuelRequests.containsValue(p.getUniqueId())) { // The other player has also requested a rematch shortcut to accepting rematch
            activeDuelRequests.values().remove(p.getUniqueId());
            Player target = Bukkit.getPlayer(awaitingRematch.get(p.getUniqueId()));
            if (target == null || !target.isOnline()) {
                opponentLeft(p);
                return;
            }

            p.sendMessage(ChatColor.GREEN + "Beginning Rematch");
            target.sendMessage(ChatColor.GREEN + "Beginning Rematch");

            initialiseDuel(target.getPlayer(), p);
        }

        if (!awaitingRematch.containsKey(p.getUniqueId())) return;

        Player target = Bukkit.getPlayer(awaitingRematch.get(p.getUniqueId()));
        if (target == null || !target.isOnline()) {
            opponentLeft(p);
            return;
        }

        Bukkit.dispatchCommand(p, "duel " + target.getName());
    }
}
