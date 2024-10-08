package org.genesismc.SoupCore;

import org.genesismc.SoupCore.Database.Database;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Credits
{
    public static boolean checkCreditBalance(Player player, Integer amount) // Checks if user has enough credits
    {
        return Integer.parseInt(Objects.requireNonNull(Database.getPlayerData(player, "soupData", "credits"))) >= amount;
    }

    public static void giveCredits(Player player, int amount)
    {
        int newBalance = Integer.parseInt(Objects.requireNonNull(Database.getPlayerData(player, "soupData", "credits"))) + amount;
        Database.setPlayerData(player, "soupData", "credits", String.valueOf(newBalance));
    }

    public static void chargeCredits(Player player, int amount)
    {
        int currentBalance = Integer.parseInt(Objects.requireNonNull(Database.getPlayerData(player, "soupData", "credits")));
        Database.setPlayerData(player, "soupData", "credits", String.valueOf(currentBalance-amount));
    }
}
