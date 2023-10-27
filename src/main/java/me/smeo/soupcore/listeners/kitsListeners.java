package me.smeo.soupcore.listeners;

import me.smeo.soupcore.Kits.Methods_Kits;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class kitsListeners implements Listener
{
    @EventHandler
    public void onClick(InventoryClickEvent e)
    {
        if(!e.getView().getTitle().contains("Kit Selection")){return;}
        if(e.getCurrentItem()==null){return;}
        if(e.getCurrentItem().getItemMeta()==null){return;}

        Player player = (Player) e.getWhoClicked();
        e.setCancelled(true);
        if(e.getClickedInventory().getType() == InventoryType.PLAYER){return;}

        switch (e.getSlot()){
            case 1 + (9):
                Methods_Kits.selectKit(player, 0);
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.WHITE + " Default " + ChatColor.GRAY + "kit");
                break;
            case 3 + (9):
                Methods_Kits.selectKit(player, 1);
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.GREEN + " Venom " + ChatColor.GRAY + "kit");
                break;
            case 5 + (9):
                Methods_Kits.selectKit(player, 2);
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.RED + " Spiderman " + ChatColor.GRAY + "kit");
                break;
            case 7 + (9):
                Methods_Kits.selectKit(player, 3);
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.YELLOW + " Blitz " + ChatColor.GRAY + "kit");
                break;
            case 2 + (9*2):
                Methods_Kits.selectKit(player, 4);
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.DARK_GRAY + " Stealth " + ChatColor.GRAY + "kit");
                break;
            case 4 + (9*2):
                Methods_Kits.selectKit(player, 5);
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.WHITE + " Grappler " + ChatColor.GRAY + "kit");
                break;
            case 6 + (9*2):
                Methods_Kits.selectKit(player, 6);
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.DARK_GREEN + " Fisherman " + ChatColor.GRAY + "kit");
                break;
            case 1 + (9*3):
                Methods_Kits.selectKit(player, 7);
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.DARK_PURPLE + " Scientist " + ChatColor.GRAY + "kit");
                break;
            case 3 + (9*3):
                Methods_Kits.selectKit(player, 8);
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.YELLOW + " Glider " + ChatColor.GRAY + "kit");
                break;
            case 5 + (9*3):
                Methods_Kits.selectKit(player, 9);
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.DARK_AQUA + " Soldier " + ChatColor.GRAY + "kit");
                break;
            case 7 + (9*3):
                Methods_Kits.selectKit(player, 10);
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.BLUE + " Mage " + ChatColor.GRAY + "kit");
                break;
            case 2 + (9*4):
                Methods_Kits.selectKit(player, 11);
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.DARK_GREEN + " Hulk " + ChatColor.GRAY + "kit");
                break;
            case 4 + (9*4):
                Methods_Kits.selectKit(player, 12);
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.DARK_RED + " Tank " + ChatColor.GRAY + "kit");
                break;
        }
        player.closeInventory();
        Methods_Kits.createKitInventory(player);
    }
}
