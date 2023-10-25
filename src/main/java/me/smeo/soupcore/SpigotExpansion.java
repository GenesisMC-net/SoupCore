package me.smeo.soupcore;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class SpigotExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "soupCore";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Smeo";
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
    public String onPlaceholderRequest(Player player, String params) {
        if(player == null)
        {
            return "";
        }
        if(params.equals("kills"))
        {
            return (String) Database.getPlayerData(player, "soupData", "kills");
        }
        if(params.equals("killStreak"))
        {
            return (String) Database.getPlayerData(player, "soupData", "killStreak");
        }
        if(params.equals("deaths"))
        {
            return (String) Database.getPlayerData(player, "soupData", "deaths");
        }
        if(params.equals("kdr"))
        {
            float kdr = Float.valueOf((String) Database.getPlayerData(player, "soupData", "kills")) / Float.valueOf((String) Database.getPlayerData(player, "soupData", "deaths"));
            final DecimalFormat df = new DecimalFormat("0.00");
            return df.format(kdr);
        }
        if(params.equals("credits"))
        {
            return (String) Database.getPlayerData(player, "soupData", "credits");
        }
        if(params.equals("bounty"))
        {
            Integer bounty = Integer.valueOf((String) Database.getPlayerData(player, "soupData", "bounty"));
            if(bounty > 0)
            {
                //String string = ChatColor.GRAY + " | " + ChatColor.GOLD + bounty.toString();
                return bounty.toString();

            }else
            {
               return null;
            }
        }

        return null;
    }


}
