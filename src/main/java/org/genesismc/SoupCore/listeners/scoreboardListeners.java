package org.genesismc.SoupCore.listeners;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.*;

public class scoreboardListeners implements Listener {
    public static Objective showHearts;

    public static void initliase() {
        heartsBelowName();
    }

    private static void heartsBelowName() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();

        showHearts = board.registerNewObjective("showhearts", "health");
        showHearts.setDisplaySlot(DisplaySlot.BELOW_NAME);
        showHearts.setDisplayName(ChatColor.DARK_RED + "❤");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.setScoreboard(showHearts.getScoreboard());
    }
}
