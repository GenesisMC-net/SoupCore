package org.genesismc.SoupCore.listeners;

import org.genesismc.SoupCore.Kits.Methods_Kits;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class SpawnHotbarListeners implements Listener {
    @EventHandler
    public void onClick(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        String itemName = ChatColor.stripColor(p.getItemInHand().getItemMeta().getDisplayName());
        if (p.getItemInHand() == null || !p.getItemInHand().hasItemMeta()) {return;}
        if (Objects.equals(itemName, "Kit Selection"))
        {
            e.setCancelled(true);
            Methods_Kits.createKitInventory(p);
        } else if (Objects.equals(itemName, "Duels")) {

        }
    }
}
