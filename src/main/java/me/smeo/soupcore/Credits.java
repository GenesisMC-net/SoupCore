package me.smeo.soupcore;

import me.smeo.soupcore.Database.Database;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Credits
{

    public static void giveCredits(Player player, int amount)
    {
        int newBalance = Database.getPlayerData(player, "credits") + amount;
        Database.SetPlayerData(player, "credits", newBalance);
    }


}
