package org.genesismc.SoupCore.listeners.abilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;
import java.util.Random;

public class AbilitySnail implements Listener {
    @EventHandler
    public void onDamagePlayer(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) {
            return;
        }
        Player target = (Player) e.getEntity();
        Player attacker = (Player) e.getDamager();
        ItemStack weapon = attacker.getItemInHand();

        if (!Objects.equals(weapon.getItemMeta().getDisplayName(), ChatColor.GREEN + "Snail Sword")) {
            return;
        }
        Random rand = new Random();
        int randomNumber = rand.nextInt(100);
        if (randomNumber <= 10) // 10% chance
        {
            PotionEffect slownessEffect = new PotionEffect(PotionEffectType.SLOW, 20 * 5, 1);
            target.addPotionEffect(slownessEffect);
        }
    }
}
