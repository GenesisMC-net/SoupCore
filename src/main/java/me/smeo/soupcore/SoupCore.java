package me.smeo.soupcore;

import me.smeo.soupcore.Database.Database;
import me.smeo.soupcore.commands.*;
import me.smeo.soupcore.commands.bounty_system.bountyCommand;
import me.smeo.soupcore.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;


public final class SoupCore extends JavaPlugin {

    public static SoupCore plugin;
    public static Inventory kits;
    private static String connectionURL;
    public static List<Integer> killStreakMilestones = new ArrayList<Integer>();





    private void createInventory()
    {
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_PURPLE + "Kits");

        ItemStack item = new ItemStack((Material.DIAMOND_SWORD));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_RED + "PVP Kit");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("Click to activate the kit!");
        meta.addEnchant(Enchantment.LUCK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        inv.setItem(3, item);

        item.setType(Material.STICK);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Next Kit");
        item.setItemMeta(meta);
        inv.setItem(5, item);

        kits = inv;
    }

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
        getCommand("ping").setExecutor(new ping());
        getCommand("kits").setExecutor(new kitsCommand());
        getCommand("placeholderTestCommand").setExecutor(new placeholderTestCommand());
        getCommand("refill").setExecutor(new refill());
        getCommand("repair").setExecutor(new repair());
        getCommand("bounty").setExecutor(new bountyCommand());
        // VVV ADMIN COMMANDS VVV
        getCommand("adminGiveCredits").setExecutor(new adminGiveCredits());

        createInventory();

        if(Bukkit.getPluginManager().getPlugin("PlaceHolderAPI") != null)
        {
            new SpigotExpansion().register();
        }

        connectionURL = "jdbc:h2:" + getDataFolder().getAbsolutePath() + "/data/database";
        System.out.println(connectionURL);
        Database.initialiseDatabase();

        System.out.println("SoupCore has been enabled!");

    }


}
