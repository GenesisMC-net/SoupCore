package me.smeo.soupcore;

import org.bukkit.entity.Player;

public class Credits
{

    public static boolean checkCreditBalance(Player player, Integer amount) // Checks if user has enough credits
    {
        if(Integer.valueOf((String) Database.getPlayerData(player, "soupData", "credits")) >= amount)
        {
            return true;
        }
        return false;
    }

    public static void giveCredits(Player player, int amount)
    {
        int newBalance = Integer.valueOf((String) Database.getPlayerData(player, "soupData", "credits")) + amount;
        Database.SetPlayerData(player, "soupData", "credits", newBalance);
    }

    public static void chargeCredits(Player player, int amount)
    {
        int currentBalance = Integer.valueOf((String) Database.getPlayerData(player, "soupData", "credits"));
        Database.SetPlayerData(player, "soupData", "credits", currentBalance-amount);
    }


}
