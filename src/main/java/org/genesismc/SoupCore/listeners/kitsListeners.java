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
        e.setCancelled(true);

        if(e.getCurrentItem()==null){return;}
        if(e.getCurrentItem().getItemMeta()==null){return;}
        if (e.getCurrentItem().getItemMeta().getDisplayName() == null) { return; }

        Player player = (Player) e.getWhoClicked();
        if(e.getClickedInventory().getType() == InventoryType.PLAYER){return;}

        Inventory inv = player.getOpenInventory().getTopInventory();

        String selectedKit = e.getCurrentItem().getItemMeta().getDisplayName();

        Methods_Kits.selectKit(player, selectedKit);
        player.sendMessage(ChatColor.GRAY + "You selected the " + selectedKit + ChatColor.GRAY + " kit");

        // Update GUI
        inv.setItem(1 + 9, KitDefault.guiAppearance(player));
        inv.setItem(2 + 9, KitVenom.guiAppearance(player));
        inv.setItem(3 + 9, KitSpiderman.guiAppearance(player));
        inv.setItem(4 + 9, KitBlitz.guiAppearance(player));
        inv.setItem(5 + 9, KitStealth.guiAppearance(player));
        inv.setItem(6 + 9, KitGrappler.guiAppearance(player));
        inv.setItem(7 + 9, KitFisherman.guiAppearance(player));

        inv.setItem(1 + (9 * 2), KitScientist.guiAppearance(player));
        inv.setItem(2 + (9 * 2), KitGlider.guiAppearance(player));
        inv.setItem(3 + (9 * 2), KitSoldier.guiAppearance(player));
        inv.setItem(4 + (9 * 2), KitMage.guiAppearance(player));
        inv.setItem(5 + (9 * 2), KitHulk.guiAppearance(player));
        inv.setItem(6 + (9 * 2), KitTank.guiAppearance(player));
        inv.setItem(7 + (9 * 2), KitSnail.guiAppearance(player));

        inv.setItem(1 + (9 * 3), KitSwitcher.guiAppearance(player));
        inv.setItem(2 + (9 * 3), KitTurbo.guiAppearance(player));
    }
}
