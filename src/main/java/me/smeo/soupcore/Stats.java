package me.smeo.soupcore;

import me.smeo.soupcore.Database.Database;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class Stats
{

    public static String kills(Player player)
    {
        return (String) Database.getPlayerData(player, "soupData", "kills");
    }

    public static String killStreak(Player player)
    {
        return (String) Database.getPlayerData(player, "soupData", "killStreak");
    }

    public static String deaths(Player player)
    {
        return (String) Database.getPlayerData(player, "soupData", "deaths");
    }

    public static String kdr(Player player)
    {
        float deaths = Float.valueOf((String) Database.getPlayerData(player, "soupData", "deaths"));
        if(deaths < 1){deaths = 1;}
        float kdr = Float.valueOf((String) Database.getPlayerData(player, "soupData", "kills")) / deaths;
        final DecimalFormat df = new DecimalFormat("0.00");
        return df.format(kdr);
    }
}
