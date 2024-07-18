package org.genesismc.SoupCore.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.genesismc.SoupCore.Kits.*;

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
                Methods_Kits.selectKit(player, ChatColor.WHITE + "Default");
                inv.setItem(1 + 9, KitDefault.guiAppearance(player));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.WHITE + " Default " + ChatColor.GRAY + "kit");
                break;
            case 3 + (9):
                Methods_Kits.selectKit(player, ChatColor.GREEN + "Venom");
                inv.setItem(3 + 9, KitVenom.guiAppearance(player));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.GREEN + " Venom " + ChatColor.GRAY + "kit");
                break;
            case 5 + (9):
                Methods_Kits.selectKit(player, ChatColor.RED + "Spiderman");
                inv.setItem(5 + 9, KitSpiderman.guiAppearance(player));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.RED + " Spiderman " + ChatColor.GRAY + "kit");
                break;
            case 7 + (9):
                Methods_Kits.selectKit(player, ChatColor.YELLOW + "Blitz");
                inv.setItem(7 + 9, KitBlitz.guiAppearance(player));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.YELLOW + " Blitz " + ChatColor.GRAY + "kit");
                break;
            case 2 + (9*2):
                Methods_Kits.selectKit(player, ChatColor.DARK_GRAY + "Stealth");
                inv.setItem(2 + (9 * 2), KitStealth.guiAppearance(player));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.DARK_GRAY + " Stealth " + ChatColor.GRAY + "kit");
                break;
            case 4 + (9*2):
                Methods_Kits.selectKit(player, ChatColor.WHITE + "Grappler");
                inv.setItem(4 + (9 * 2), KitGrappler.guiAppearance(player));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.WHITE + " Grappler " + ChatColor.GRAY + "kit");
                break;
            case 6 + (9*2):
                Methods_Kits.selectKit(player, ChatColor.DARK_GREEN + "Fisherman");
                inv.setItem(6 + (9 * 2), KitFisherman.guiAppearance(player));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.DARK_GREEN + " Fisherman " + ChatColor.GRAY + "kit");
                break;
            case 1 + (9*3):
                Methods_Kits.selectKit(player, ChatColor.DARK_PURPLE + "Scientist");
                inv.setItem(1 + (9 * 3), KitScientist.guiAppearance(player));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.DARK_PURPLE + " Scientist " + ChatColor.GRAY + "kit");
                break;
            case 3 + (9*3):
                Methods_Kits.selectKit(player, ChatColor.YELLOW + "Glider");
                inv.setItem(3 + (9 * 3), KitGlider.guiAppearance(player));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.YELLOW + " Glider " + ChatColor.GRAY + "kit");
                break;
            case 5 + (9*3):
                Methods_Kits.selectKit(player, ChatColor.DARK_AQUA + "Soldier");
                inv.setItem(5 + (9 * 3), KitSoldier.guiAppearance(player));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.DARK_AQUA + " Soldier " + ChatColor.GRAY + "kit");
                break;
            case 7 + (9*3):
                Methods_Kits.selectKit(player, ChatColor.BLUE + "Mage");
                inv.setItem(7 + (9 * 3), KitMage.guiAppearance(player));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.BLUE + " Mage " + ChatColor.GRAY + "kit");
                break;
            case 2 + (9*4):
                Methods_Kits.selectKit(player, ChatColor.DARK_GREEN + "Hulk");
                inv.setItem(2 + (9 * 4), KitHulk.guiAppearance(player));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.DARK_GREEN + " Hulk " + ChatColor.GRAY + "kit");
                break;
            case 4 + (9*4):
                Methods_Kits.selectKit(player, ChatColor.DARK_RED + "Tank");
                inv.setItem(4 + (9 * 4), KitTank.guiAppearance(player));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.DARK_RED + " Tank " + ChatColor.GRAY + "kit");
                break;
            case 6 + (9*4):
                Methods_Kits.selectKit(player, ChatColor.GREEN + "Snail");
                inv.setItem(6 + (9 * 4), KitSnail.guiAppearance(player));
                player.sendMessage(ChatColor.GRAY + "You selected the" + ChatColor.GREEN + " Snail " + ChatColor.GRAY + "kit");
        }
    }
}
