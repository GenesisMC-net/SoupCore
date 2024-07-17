package me.smeo.soupcore.listeners.abilities;

import me.smeo.soupcore.SoupCore;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

import static me.smeo.soupcore.listeners.abilities.AbilityFisherman.playerReelCooldown;
import static me.smeo.soupcore.listeners.abilities.AbilityGlider.gliderCooldown;
import static me.smeo.soupcore.listeners.abilities.AbilityGrappler.grapplingHookCooldown;
import static me.smeo.soupcore.listeners.abilities.AbilityHulk.hulkSmashCooldown;
import static me.smeo.soupcore.listeners.abilities.AbilityMage.fireLaunchCooldown;
import static me.smeo.soupcore.listeners.abilities.AbilityMage.waterAbilityCooldown;
import static me.smeo.soupcore.listeners.abilities.AbilityNinjaStars.ninjaStarCooldown;
import static me.smeo.soupcore.listeners.abilities.AbilitySoldier.iceDomeCooldown;
import static me.smeo.soupcore.listeners.abilities.AbilitySpiderwebs.spiderWebCooldown;
import static me.smeo.soupcore.listeners.abilities.AbilityTank.silverFishCooldown;

public class Cooldowns {
    private static final HashMap<UUID, BukkitTask> activeCooldowns = new HashMap<>();
    public static void addAbilityCooldown(Player p, HashMap<UUID, Long> abilityCooldowns, int cooldown, String abilityName) {
        final int[] i = {0};
        BukkitTask newCooldown = new BukkitRunnable() {
            @Override
            public void run() {
                if (i[0] >= 20L * cooldown) {
                    if (abilityCooldowns.containsKey(p.getUniqueId())){
                        abilityCooldowns.remove(p.getUniqueId());
                        p.sendMessage(ChatColor.GRAY + "You can now use " + abilityName);
                    }
                    p.setLevel(0);
                    p.setExp(0);

                    activeCooldowns.remove(p.getUniqueId());
                    this.cancel();
                    return;
                }

                p.setLevel(cooldown - Math.round((float) i[0] / 20));
                p.setExp(1 - ((float) i[0] / (float) (20L * cooldown)));

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
        playerReelCooldown.remove(playerUUID);
        gliderCooldown.remove(playerUUID);
        grapplingHookCooldown.remove(playerUUID);
        hulkSmashCooldown.remove(playerUUID);
        waterAbilityCooldown.remove(playerUUID);
        fireLaunchCooldown.remove(playerUUID);
        ninjaStarCooldown.remove(playerUUID);
        iceDomeCooldown.remove(playerUUID);
        spiderWebCooldown.remove(playerUUID);
        silverFishCooldown.remove(playerUUID);
    }
}
