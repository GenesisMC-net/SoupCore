package org.genesismc.SoupCore.listeners;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.genesismc.SoupCore.Duels;
import org.genesismc.SoupCore.Kits.Methods_Kits;
import org.genesismc.SoupCore.SoupCore;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;

import static org.genesismc.SoupCore.commands.modmodeCommand.modmodeActive;
import static org.genesismc.SoupCore.listeners.cancelFallDmgListener.cancelFallDamage;

public class PVPRegionListeners implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        if (e.getFrom().getBlock().getLocation() == e.getTo().getBlock().getLocation())
        {
            return;
        }

        Player p = e.getPlayer();

        RegionManager rgManager = SoupCore.getWorldGuard.getRegionManager(p.getWorld());

        ApplicableRegionSet rgsMovedFrom = rgManager.getApplicableRegions(e.getFrom().getBlock().getLocation());
        ApplicableRegionSet rgsMovedTo = rgManager.getApplicableRegions(e.getTo().getBlock().getLocation());

        if (rgsMovedTo.size() != rgsMovedTo.size() || rgsMovedTo.size() == 1 || rgsMovedFrom.size() == 1)
        {
            for (ProtectedRegion rgTo: rgsMovedTo) {
                for (ProtectedRegion rgFrom: rgsMovedFrom) {
                    // Enter a new region
                    if (Objects.equals(rgFrom.getId(), "spawn") && Objects.equals(rgTo.getId(), "pvp"))
                    {
                        if (!modmodeActive.contains(p.getUniqueId())) {
                            p.sendMessage(ChatColor.GRAY + "You are no longer protected");
                            Methods_Kits.giveKit(p, Methods_Kits.getActiveKit(p));
                        }
                        if (!cancelFallDamage.contains(p.getUniqueId())) {
                            cancelFallDmgListener.addPlayer(p);
                        }
                        if (Duels.activeDuelRequests.containsKey(p.getUniqueId())) {
                            Bukkit.dispatchCommand(p, "/duel cancel");
                        }
                    }
                }
            }
        }
    }
}
