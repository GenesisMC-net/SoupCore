package me.smeo.soupcore;

import org.bukkit.entity.Player;

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
