package org.genesismc.SoupCore;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.genesismc.SoupCore.Database.Database;
import org.genesismc.SoupCore.Kits.Methods_Kits;
import org.genesismc.SoupCore.listeners.combatLogListeners;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.UUID;

public class SpigotExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "soupCore";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Donut, Smeo";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if(player == null)
        {
            return "";
        }
        switch (params) {
            case "kills":
                return Stats.kills(player);
            case "killStreak":
                return Stats.killStreak(player);
            case "deaths":
                return Stats.deaths(player);
            case "kdr":
                float kdr = Float.parseFloat(Stats.kdr(player));

                if (kdr < 1) {
                    return ChatColor.RED + Stats.kdr(player);
                } else if (kdr > 1) {
                    return ChatColor.GREEN + Stats.kdr(player);
                }
                return ChatColor.GRAY + Stats.kdr(player);
            case "credits":
                return (String) Database.getPlayerData(player, "soupData", "credits");
            case "bounty":
                return (String) Database.getPlayerData(player, "soupData", "bounty");
            case "kit":
                return Methods_Kits.getActiveKit(player);
            case "combatTimer":
                UUID playerUUID = player.getUniqueId();
                if (!combatLogListeners.antiLog.containsKey(playerUUID)) {
                    return ChatColor.RED + "N/A";
                }

                Long timer = combatLogListeners.antiLog.get(playerUUID);

                DecimalFormat df = new DecimalFormat("#.#");
                String timerFormatted = df.format((15 - ((float) timer / 20)));

                return ChatColor.RED + timerFormatted + "s";
            default:
                return null;
        }
    }
}
