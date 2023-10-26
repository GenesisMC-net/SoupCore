package me.smeo.soupcore;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.smeo.soupcore.Database.Database;
import me.smeo.soupcore.commands.*;
import me.smeo.soupcore.commands.bountyCommand;
import me.smeo.soupcore.listeners.*;
import me.smeo.soupcore.listeners.abilities.*;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;


public final class SoupCore extends JavaPlugin {

    public static SoupCore plugin;
    public static WorldGuardPlugin getWorldGuard;
    public static LuckPerms luckPerms;
    public static Inventory kits;
    private static String connectionURL;
    public static List<Integer> killStreakMilestones = new ArrayList<Integer>();

    public static String getConnectionURL() {
        return connectionURL;
    }

    @Override
    public void onEnable() {
        plugin = this;
        killStreakMilestones.add(10);
        killStreakMilestones.add(25);
        killStreakMilestones.add(30);
        killStreakMilestones.add(50);
        killStreakMilestones.add(60);
        getServer().getPluginManager().registerEvents(new soupDropListener(), this);
        getServer().getPluginManager().registerEvents(new soupUseListener(), this);
        getServer().getPluginManager().registerEvents(new kitsListeners(), this);
        getServer().getPluginManager().registerEvents(new PlayerKillListener(), this);
        getServer().getPluginManager().registerEvents(new soupSignRefillListener(), this);
        getServer().getPluginManager().registerEvents(new spongeLaunchListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);

        getServer().getPluginManager().registerEvents(new PlayerJoinLeaveMessages(), this);

        getServer().getPluginManager().registerEvents(new combatLogListeners(), this);

        getServer().getPluginManager().registerEvents(new PVPRegionListeners(), this);
        // VVV Abilities VVV
        getServer().getPluginManager().registerEvents(new AbilityPoisonSword(), this);
        getServer().getPluginManager().registerEvents(new AbilityNinjaStars(), this);
        getServer().getPluginManager().registerEvents(new AbilitySpiderwebs(), this);
        getServer().getPluginManager().registerEvents(new AbilityBlitz(), this);
        getServer().getPluginManager().registerEvents(new AbilityGrappler(), this);
        getServer().getPluginManager().registerEvents(new AbilityFisherman(), this);
        getServer().getPluginManager().registerEvents(new AbilityScientist(), this);
        getServer().getPluginManager().registerEvents(new AbilityGlider(), this);
        getServer().getPluginManager().registerEvents(new AbilitySoldier(), this);
        getServer().getPluginManager().registerEvents(new AbilityMage(), this);
        getServer().getPluginManager().registerEvents(new AbilityHulk(), this);
        getServer().getPluginManager().registerEvents(new AbilityTank(), this);
        
        getCommand("ping").setExecutor(new ping());
        getCommand("kits").setExecutor(new kitsCommand());
        getCommand("placeholderTestCommand").setExecutor(new placeholderTestCommand());
        getCommand("refill").setExecutor(new refill());
        getCommand("repair").setExecutor(new repair());
        getCommand("bounty").setExecutor(new bountyCommand());
        getCommand("stats").setExecutor(new statsCommand());
        // VVV ADMIN COMMANDS VVV
        getCommand("adminGiveCredits").setExecutor(new adminGiveCredits());
        getCommand("giveAbilityItem").setExecutor(new giveAbilityItem());

        if(Bukkit.getPluginManager().getPlugin("PlaceHolderAPI") != null)
        {
            new SpigotExpansion().register();
        }

        getWorldGuard = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();
        }

        connectionURL = "jdbc:h2:" + getDataFolder().getAbsolutePath() + "/data/database";
        System.out.println(connectionURL);
        Database.initialiseDatabase();

        System.out.println("SoupCore has been enabled!");

    }


}
