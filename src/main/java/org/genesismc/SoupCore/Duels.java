package org.genesismc.SoupCore;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.genesismc.SoupCore.Database.Database;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Duels {

    public static HashMap<UUID, UUID> activeDuelRequests = new HashMap<>();
    public static HashMap<UUID, UUID> activeDuels = new HashMap<>();

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
        String wins = Database.getPlayerData(target, "duelData", "wins");
        float losses = Float.parseFloat(Objects.requireNonNull(Database.getPlayerData(target, "duelData", "losses")));

        if (losses < 1){ losses = 1; }
        assert wins != null;
        float wlr = Float.parseFloat(wins) / losses;
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

    public static void duel(Player requester, Player accepter) {

        for (Player online : Bukkit.getOnlinePlayers()) {
            requester.hidePlayer(online);
            accepter.hidePlayer(online);
        }
        requester.showPlayer(accepter);
        accepter.showPlayer(requester);

        
    }
}
