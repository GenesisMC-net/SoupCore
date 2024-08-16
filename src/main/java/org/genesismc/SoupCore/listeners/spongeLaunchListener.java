package org.genesismc.SoupCore.listeners;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class spongeLaunchListener implements Listener
{
    @EventHandler
    public void onSponge(PlayerMoveEvent e)
    {
        Player p = e.getPlayer();
        if(p.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.SPONGE) { return; }

        int boostHeight = 3; // Default
        // Check if boost height has been adjusted
        if (p.getLocation().subtract(0, 2, 0).getBlock().getType() == Material.SIGN_POST) {
            Sign sign = (Sign) p.getLocation().subtract(0, 2, 0).getBlock().getState();
            boostHeight = Integer.parseInt(sign.getLine(1));
        }

        if (p.getVelocity().getY() >= 0 && !p.isOnGround()) { // Player has jumped
            p.setVelocity(new Vector(p.getVelocity().getX(), (double) boostHeight / 2, p.getVelocity().getZ()));
            cancelFallDmgListener.addPlayer(p);
        }
    }
}