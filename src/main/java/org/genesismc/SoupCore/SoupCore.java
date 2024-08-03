package org.genesismc.SoupCore;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.genesismc.SoupCore.Database.Database;
import org.genesismc.SoupCore.commands.*;
import org.genesismc.SoupCore.listeners.*;
import org.genesismc.SoupCore.listeners.abilities.*;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public final class SoupCore extends JavaPlugin {

    public static SoupCore plugin;
    public static WorldGuardPlugin getWorldGuard;
    public static LuckPerms luckPerms;
    public static Inventory kits;
    private static String connectionURL;
    public static final List<Integer> killStreakMilestones = new ArrayList<>();

    public static String getConnectionURL() {
        return connectionURL;
    }

    public static boolean playerInSpawn(Player p) {
        for (ProtectedRegion rg : WGBukkit.getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation())){
            if (Objects.equals(rg.getId(), "spawn")) {
                return true;
            }
        }
        return false;
    }

    // TODO: transfer Colourise() method from LegacyGenesisCore

    @Override
    public void onEnable() {
        plugin = this;
        killStreakMilestones.add(10);
        killStreakMilestones.add(25);
        killStreakMilestones.add(30);
        killStreakMilestones.add(50);
        killStreakMilestones.add(60);
        getServer().getPluginManager().registerEvents(new ItemDropListener(), this);
        getServer().getPluginManager().registerEvents(new soupUseListener(), this);
        getServer().getPluginManager().registerEvents(new kitsListeners(), this);
        getServer().getPluginManager().registerEvents(new PlayerKillListener(), this);
        getServer().getPluginManager().registerEvents(new soupSignRefillListener(), this);
        getServer().getPluginManager().registerEvents(new spongeLaunchListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinLeaveListeners(), this);
        getServer().getPluginManager().registerEvents(new combatLogListeners(), this);
        getServer().getPluginManager().registerEvents(new PVPRegionListeners(), this);
        getServer().getPluginManager().registerEvents(new SpawnHotbarListeners(), this);
        getServer().getPluginManager().registerEvents(new SpawnLaunchListener(), this);
        getServer().getPluginManager().registerEvents(new CoinFlipListeners(), this);
        getServer().getPluginManager().registerEvents(new cancelFallDmgListener(), this);
        getServer().getPluginManager().registerEvents(new scoreboardListeners(), this);

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
        getServer().getPluginManager().registerEvents(new AbilitySnail(), this);
        getServer().getPluginManager().registerEvents(new AbilitySwitcher(), this);
        getServer().getPluginManager().registerEvents(new AbilityTurbo(), this);

        getCommand("kits").setExecutor(new kitsCommand());
        getCommand("refill").setExecutor(new refillCommand());
        getCommand("repair").setExecutor(new repairCommand());
        getCommand("bounty").setExecutor(new bountyCommand());
        getCommand("stats").setExecutor(new statsCommand());
        getCommand("spawn").setExecutor(new spawnCommand());
        getCommand("coinflip").setExecutor(new coinflipCommand());
        getCommand("pay").setExecutor(new payCommand());
        getCommand("balance").setExecutor(new balCommand());
        // VVV ADMIN COMMANDS VVV
        getCommand("adminGiveCredits").setExecutor(new adminGiveCredits());

        if(Bukkit.getPluginManager().getPlugin("PlaceHolderAPI") != null)
        {
            new SpigotExpansion().register();
        }

        scoreboardListeners.enableHeartsBelowName();

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
