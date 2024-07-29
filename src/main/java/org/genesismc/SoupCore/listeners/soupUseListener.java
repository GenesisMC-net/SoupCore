package org.genesismc.SoupCore.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class soupUseListener implements Listener
{
    @EventHandler
    public void onRightClick(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        if((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && p.getItemInHand().getType() == Material.MUSHROOM_SOUP)
        {
            if (p.getHealth() <= 12)
            {
               p.setHealth((p.getHealth()) + 8); // +4 Hearts
               p.getItemInHand().setType(Material.BOWL);
            }
            else if(p.getHealth() < 20)
            {
                p.setHealth(p.getMaxHealth());
                p.getItemInHand().setType(Material.BOWL);
            }
        }
    }
}
