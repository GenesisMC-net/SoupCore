package me.smeo.soupcore;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.smeo.soupcore.Database.Database;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.crypto.Data;

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
            Integer kdr = Database.getPlayerData(player, "kills") - Database.getPlayerData(player, "deaths");
            return kdr.toString();
        }

        return null;
    }
}
