package me.smeo.soupcore.listeners;

import me.smeo.soupcore.SoupCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class kitsListeners implements Listener
{



    @EventHandler
    public void onClick(InventoryClickEvent e)
    {
        if(!e.getView().getTitle().contains("Kits")){return;}
        if(e.getCurrentItem()==null){return;}
        if(e.getCurrentItem().getItemMeta()==null){return;}

        Player player = (Player) e.getWhoClicked();
        e.setCancelled(true);
        if(e.getClickedInventory().getType() == InventoryType.PLAYER){return;}

        if(e.getSlot() == 3)
        {
            //PVP Kit
            player.closeInventory();
            player.updateInventory();
        }
    }
}
