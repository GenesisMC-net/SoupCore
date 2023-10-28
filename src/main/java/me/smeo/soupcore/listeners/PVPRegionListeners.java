package me.smeo.soupcore.listeners;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.smeo.soupcore.Kits.Methods_Kits;
import me.smeo.soupcore.SoupCore;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PVPRegionListeners implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){

        if (e.getFrom().getBlock().getLocation() == e.getTo().getBlock().getLocation())
        {
            return;
        }
        else {
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
                            p.sendMessage(ChatColor.GRAY + "You are no longer protected");
                            Methods_Kits.giveKit(p, Methods_Kits.getActiveKit(p));
                        }
                    }
                }
            }
        }
    }
}
