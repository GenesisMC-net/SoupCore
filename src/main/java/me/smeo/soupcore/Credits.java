package me.smeo.soupcore;

import me.smeo.soupcore.Database.Database;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.xml.crypto.Data;

public class Credits
{

    public static boolean checkCreditBalance(Player player, Integer amount) // Checks if user has enough credits
    {
        if(Database.getPlayerData(player, "credits") >= amount)
        {
            return true;
        }
        return false;
    }

    public static void giveCredits(Player player, int amount)
    {
        int newBalance = Database.getPlayerData(player, "credits") + amount;
        Database.SetPlayerData(player, "credits", newBalance);
    }

    public static void chargeCredits(Player player, int amount)
    {
        int currentBalance = Database.getPlayerData(player, "credits");
        Database.SetPlayerData(player, "credits", currentBalance-amount);
    }


}
