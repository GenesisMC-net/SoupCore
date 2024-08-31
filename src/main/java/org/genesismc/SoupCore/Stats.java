package org.genesismc.SoupCore;

import org.genesismc.SoupCore.Database.Database;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Objects;

public class Stats
{
    public static String kills(Player player)
    {
        return Database.getPlayerData(player, "soupData", "kills");
    }

    public static String killStreak(Player player)
    {
        return Database.getPlayerData(player, "soupData", "killStreak");
    }

    public static String bestKillStreak(Player player) { return Database.getPlayerData(player, "soupData", "bestKillStreak"); }

    public static String deaths(Player player)
    {
        return Database.getPlayerData(player, "soupData", "deaths");
    }

    public static String kdr(Player player)
    {
        float deaths = Float.parseFloat(Objects.requireNonNull(Database.getPlayerData(player, "soupData", "deaths")));
        if(deaths < 1){deaths = 1;}
        float kdr = Float.parseFloat(Objects.requireNonNull(Database.getPlayerData(player, "soupData", "kills"))) / deaths;
        final DecimalFormat df = new DecimalFormat("0.00");
        return df.format(kdr);
    }

    public static String duelWins(Player player) { return Database.getPlayerData(player, "duelData", "wins"); }

    public static String duelLosses(Player player) { return Database.getPlayerData(player, "duelData", "losses"); }
}
