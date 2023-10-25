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
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Soup Refill");
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
            if(e.getClickedBlock().getType() == Material.WALL_SIGN)
            {

                Sign sign = (Sign) e.getClickedBlock().getState();
                if(sign.getLine(1).equals("Soup Refill"))
                {
                    p.openInventory(createSoupInventory());
                }
            }
        }

    }
}
