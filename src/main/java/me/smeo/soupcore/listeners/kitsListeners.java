package me.smeo.soupcore.listeners;

import me.smeo.soupcore.Kits.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

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

        Inventory inv = player.getOpenInventory().getTopInventory();

        switch (e.getSlot()){
            case 1 + (9):
                Methods_Kits.selectKit(player, 0);
                inv.setItem(1 + 9, KitDefault.guiAppearance(player, inv));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.WHITE + " Default " + ChatColor.GRAY + "kit");
                break;
            case 3 + (9):
                Methods_Kits.selectKit(player, 1);
                inv.setItem(3 + 9, KitVenom.guiAppearance(player, inv));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.GREEN + " Venom " + ChatColor.GRAY + "kit");
                break;
            case 5 + (9):
                Methods_Kits.selectKit(player, 2);
                inv.setItem(5 + 9, KitSpiderman.guiAppearance(player, inv));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.RED + " Spiderman " + ChatColor.GRAY + "kit");
                break;
            case 7 + (9):
                Methods_Kits.selectKit(player, 3);
                inv.setItem(7 + 9, KitBlitz.guiAppearance(player, inv));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.YELLOW + " Blitz " + ChatColor.GRAY + "kit");
                break;
            case 2 + (9*2):
                Methods_Kits.selectKit(player, 4);
                inv.setItem(2 + (9 * 2), KitStealth.guiAppearance(player, inv));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.DARK_GRAY + " Stealth " + ChatColor.GRAY + "kit");
                break;
            case 4 + (9*2):
                Methods_Kits.selectKit(player, 5);
                inv.setItem(4 + (9 * 2), KitGrappler.guiAppearance(player, inv));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.WHITE + " Grappler " + ChatColor.GRAY + "kit");
                break;
            case 6 + (9*2):
                Methods_Kits.selectKit(player, 6);
                inv.setItem(6 + (9 * 2), KitFisherman.guiAppearance(player, inv));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.DARK_GREEN + " Fisherman " + ChatColor.GRAY + "kit");
                break;
            case 1 + (9*3):
                Methods_Kits.selectKit(player, 7);
                inv.setItem(1 + (9 * 3), KitScientist.guiAppearance(player, inv));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.DARK_PURPLE + " Scientist " + ChatColor.GRAY + "kit");
                break;
            case 3 + (9*3):
                Methods_Kits.selectKit(player, 8);
                inv.setItem(3 + (9 * 3), KitGlider.guiAppearance(player, inv));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.YELLOW + " Glider " + ChatColor.GRAY + "kit");
                break;
            case 5 + (9*3):
                Methods_Kits.selectKit(player, 9);
                inv.setItem(5 + (9 * 3), KitSoldier.guiAppearance(player, inv));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.DARK_AQUA + " Soldier " + ChatColor.GRAY + "kit");
                break;
            case 7 + (9*3):
                Methods_Kits.selectKit(player, 10);
                inv.setItem(7 + (9 * 3), KitMage.guiAppearance(player, inv));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.BLUE + " Mage " + ChatColor.GRAY + "kit");
                break;
            case 2 + (9*4):
                Methods_Kits.selectKit(player, 11);
                inv.setItem(2 + (9 * 4), KitHulk.guiAppearance(player, inv));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.DARK_GREEN + " Hulk " + ChatColor.GRAY + "kit");
                break;
            case 4 + (9*4):
                Methods_Kits.selectKit(player, 12);
                inv.setItem(4 + (9 * 4), KitTank.guiAppearance(player, inv));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.DARK_RED + " Tank " + ChatColor.GRAY + "kit");
                break;
        }
    }
}
