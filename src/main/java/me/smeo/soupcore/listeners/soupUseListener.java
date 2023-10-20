package me.smeo.soupcore.listeners;

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
            if (p.getHealth() <= 14)
            {
               p.setHealth((p.getHealth())+6);
               p.getItemInHand().setType(Material.BOWL);
            }
            else if(14 < p.getHealth() && p.getHealth() < 20)
            {
                p.setHealth(p.getMaxHealth());
                p.getItemInHand().setType(Material.BOWL);
            }
        }



    }
}
