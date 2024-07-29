package org.genesismc.SoupCore.listeners.abilities;

import org.genesismc.SoupCore.SoupCore;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

import static org.genesismc.SoupCore.listeners.abilities.AbilityBlitz.pearlCooldown;
import static org.genesismc.SoupCore.listeners.abilities.AbilityFisherman.playerReelCooldown;
import static org.genesismc.SoupCore.listeners.abilities.AbilityGlider.gliderCooldown;
import static org.genesismc.SoupCore.listeners.abilities.AbilityGrappler.grapplingHookCooldown;
import static org.genesismc.SoupCore.listeners.abilities.AbilityHulk.hulkSmashCooldown;
import static org.genesismc.SoupCore.listeners.abilities.AbilityMage.fireLaunchCooldown;
import static org.genesismc.SoupCore.listeners.abilities.AbilityMage.waterAbilityCooldown;
import static org.genesismc.SoupCore.listeners.abilities.AbilityNinjaStars.ninjaStarCooldown;
import static org.genesismc.SoupCore.listeners.abilities.AbilitySoldier.iceDomeCooldown;
import static org.genesismc.SoupCore.listeners.abilities.AbilitySpiderwebs.spiderWebCooldown;
import static org.genesismc.SoupCore.listeners.abilities.AbilitySwitcher.switcherCooldown;
import static org.genesismc.SoupCore.listeners.abilities.AbilityTank.silverFishCooldown;

public class Cooldowns {
    private static final HashMap<UUID, BukkitTask> activeCooldowns = new HashMap<>();
    public static void addAbilityCooldown(Player p, HashMap<UUID, Long> abilityCooldown, int cooldown, String abilityName) {
        abilityCooldown.put(p.getUniqueId(), System.currentTimeMillis());
        final int[] i = {0};
        BukkitTask newCooldown = new BukkitRunnable() {
            @Override
            public void run() {
                if (i[0] >= 20L * cooldown) {
                    if (abilityCooldown.containsKey(p.getUniqueId())){
                        abilityCooldown.remove(p.getUniqueId());
                        p.sendMessage(ChatColor.GRAY + "You can now use " + abilityName);
                    }
                    activeCooldowns.remove(p.getUniqueId());
                    if (!activeCooldowns.containsKey(p.getUniqueId())) { // Multiple Cooldowns
                        p.setLevel(0);
                        p.setExp(0);
                    }
                    this.cancel();
                    return;
                }

                int lvlTimer = cooldown - Math.round((float) i[0] / 20);
                if (lvlTimer <= p.getLevel() || p.getLevel() == 0) {
                    p.setLevel(lvlTimer);
                    p.setExp(1 - ((float) i[0] / (float) (20L * cooldown)));
                }

                i[0]++;
            }
        }.runTaskTimerAsynchronously(SoupCore.plugin, 0L, 1L);
        activeCooldowns.put(p.getUniqueId(), newCooldown);
    }

    public static void removeCooldowns(Player player) {
        UUID playerUUID = player.getUniqueId();
        for (UUID targetUUID : activeCooldowns.keySet()) {
            if (targetUUID == playerUUID) {
                activeCooldowns.get(playerUUID).cancel();
                activeCooldowns.remove(playerUUID);
                player.setLevel(0);
                player.setExp(0);
            }
        }
        pearlCooldown.remove(playerUUID);
        playerReelCooldown.remove(playerUUID);
        gliderCooldown.remove(playerUUID);
        grapplingHookCooldown.remove(playerUUID);
        hulkSmashCooldown.remove(playerUUID);
        waterAbilityCooldown.remove(playerUUID);
        fireLaunchCooldown.remove(playerUUID);
        ninjaStarCooldown.remove(playerUUID);
        iceDomeCooldown.remove(playerUUID);
        spiderWebCooldown.remove(playerUUID);
        switcherCooldown.remove(playerUUID);
        silverFishCooldown.remove(playerUUID);
    }
}
