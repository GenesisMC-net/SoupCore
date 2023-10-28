package me.smeo.soupcore;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.smeo.soupcore.Database.Database;
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
        return "Donut, Smeo";
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
            return Stats.kills(player);
        }
        if(params.equals("killStreak"))
        {
            return Stats.killStreak(player);
        }
        if(params.equals("deaths"))
        {
            return Stats.deaths(player);
        }
        if(params.equals("kdr"))
        {
            return Stats.kdr(player);
        }
        if(params.equals("credits"))
        {
            return (String) Database.getPlayerData(player, "soupData", "credits");
        }
        if(params.equals("bounty"))
        {
            return (String) Database.getPlayerData(player, "soupData", "bounty");
        }

        return null;
    }
}
