package me.smeo.soupcore;

import me.smeo.soupcore.Database.Database;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Credits
{

    public static boolean checkCreditBalance(Player player, Integer amount) // Checks if user has enough credits
    {
        return Integer.parseInt((String) Objects.requireNonNull(Database.getPlayerData(player, "soupData", "credits"))) >= amount;
    }

    public static void giveCredits(Player player, int amount)
    {
        int newBalance = Integer.parseInt((String) Objects.requireNonNull(Database.getPlayerData(player, "soupData", "credits"))) + amount;
        Database.SetPlayerData(player, "soupData", "credits", newBalance);
    }

    public static void chargeCredits(Player player, int amount)
    {
        int currentBalance = Integer.parseInt((String) Objects.requireNonNull(Database.getPlayerData(player, "soupData", "credits")));
        Database.SetPlayerData(player, "soupData", "credits", currentBalance-amount);
    }


}
