package org.genesismc.SoupCore.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class soupUseListener implements Listener
{
    @EventHandler
    public void onRightClick(PlayerInteractEvent e)
    {
        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)) return;

        Player p = e.getPlayer();
        ItemStack item = p.getItemInHand();
        if (item == null) return;
        if (!(p.getItemInHand().getType() == Material.MUSHROOM_SOUP)) return;

        if (p.getHealth() >= p.getMaxHealth()) return;

        if (!item.hasItemMeta() || item.getItemMeta().getDisplayName() == null) { // Normal Soup
            if (p.getHealth() <= 12)
            {
                p.setHealth((p.getHealth()) + 8); // +4 Hearts
                item.setType(Material.BOWL);
            }
            else
            {
                p.setHealth(p.getMaxHealth());
                item.setType(Material.BOWL);
            }
        } else if (item.getItemMeta().getDisplayName().contains("Grandma's Soup")) {
            p.setHealth(p.getMaxHealth());
            if (item.getAmount() == 1) {
                item.setType(Material.BOWL);
                return;
            }
            item.setAmount(item.getAmount() - 1);
        }
    }
}
