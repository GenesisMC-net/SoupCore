package me.smeo.soupcore.listeners;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.smeo.soupcore.Database.Database;
import me.smeo.soupcore.SoupCore;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class PVPRegionListeners implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){

        if (e.getFrom().getBlock().getLocation() == e.getTo().getBlock().getLocation())
        {
            return;
        }
        else {
            System.out.println("Player moved");

            Player p = e.getPlayer();

            RegionManager rgManager = SoupCore.getWorldGuard.getRegionManager(p.getWorld());

            ApplicableRegionSet rgsMovedFrom = rgManager.getApplicableRegions(e.getFrom().getBlock().getLocation());
            ApplicableRegionSet rgsMovedTo = rgManager.getApplicableRegions(e.getTo().getBlock().getLocation());

            if (rgsMovedTo.size() != rgsMovedTo.size() || rgsMovedTo.size() == 1 || rgsMovedFrom.size() == 1)
            {
                for (ProtectedRegion rgTo: rgsMovedTo) {
                    for (ProtectedRegion rgFrom: rgsMovedFrom) {
                        System.out.println("Moved from / to" + rgFrom.getId() + rgTo.getId());
                        // Enter a new region
                        if (Objects.equals(rgFrom.getId(), "spawn") && Objects.equals(rgTo.getId(), "pvp"))
                        {
                            p.sendMessage("You are now in pvp!");
                            Integer kitSelected = 1; // Default to basic kit
                            if (Database.getPlayerData(p, "kit") != null)
                            {
                                kitSelected = Database.getPlayerData(p, "kit");
                            }
                            p.sendMessage(kitSelected.toString());
                        }
                    }
                }
            }
        }
    }
}
