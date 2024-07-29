package org.genesismc.SoupCore.listeners.abilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class AbilityFisherman implements Listener {
    public static final HashMap<UUID, Long> playerReelCooldown = new HashMap<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        playerReelCooldown.remove(e.getEntity().getPlayer().getUniqueId());
    }

    @EventHandler
    public void onFish(PlayerFishEvent e)
    {
        Player p = e.getPlayer();

        ItemStack itemInHand = p.getItemInHand().clone();

        if (!(e.getCaught() instanceof Player)) {
            return;
        }
        if (!Objects.equals(itemInHand.getItemMeta().getDisplayName(), ChatColor.DARK_GREEN + "Fishing Rod")) {
            return;
        }
        if (playerReelCooldown.containsKey(p.getUniqueId())) {
            if (System.currentTimeMillis() - playerReelCooldown.get(p.getUniqueId()) < 30 * 1000) {
                return;
            }
            playerReelCooldown.remove(p.getUniqueId());
        }

        e.getCaught().teleport(p);
        Cooldowns.addAbilityCooldown(p, playerReelCooldown, 30, ChatColor.DARK_GREEN + "Player Reel");
    }
}
