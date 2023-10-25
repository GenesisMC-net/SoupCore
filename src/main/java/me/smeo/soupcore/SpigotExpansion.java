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
            return Database.getPlayerData(player, "kills").toString();
        }
        if(params.equals("killStreak"))
        {
            return Database.getPlayerData(player, "killStreak").toString();
        }
        if(params.equals("deaths"))
        {
            return Database.getPlayerData(player, "deaths").toString();
        }
        if(params.equals("kdr"))
        {
            float kdr = (float) Database.getPlayerData(player, "kills") / (float) Database.getPlayerData(player, "deaths");
            final DecimalFormat df = new DecimalFormat("0.00");
            return df.format(kdr);
        }
        if(params.equals("credits"))
        {
            return Database.getPlayerData(player, "credits").toString();
        }
        if(params.equals("bounty"))
        {
            Integer bounty = Database.getPlayerData(player, "bounty");
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
