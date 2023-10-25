package me.smeo.soupcore.listeners;

import me.smeo.soupcore.Database.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class soupSignRefillListener implements Listener
{

    private Inventory createSoupInventory()
    {
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.GREEN + "Soup Refill");
        ItemStack soup = new ItemStack(Material.MUSHROOM_SOUP);
        for(int i=0; i<54; i++)
        {
            inv.setItem(i, soup);
        }
        return inv;
    }


    @EventHandler
    public void onRightClick(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        System.out.println(e.getClickedBlock());
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            System.out.println("Right clicked");
            if(e.getClickedBlock().getType() == Material.WALL_SIGN)
            {
                System.out.println("Player right clicked a sign");
                Integer kills = Database.getPlayerData(p, "kills");
                p.sendMessage("You have " + kills.toString() + " kills.");

                Sign sign = (Sign) e.getClickedBlock().getState();
                if(sign.getLine(1).equals("Soup Refill"))
                {
                    System.out.println("Player clicked on soup refill station");
                    p.openInventory(createSoupInventory());
                }
            }
        }

    }
}
