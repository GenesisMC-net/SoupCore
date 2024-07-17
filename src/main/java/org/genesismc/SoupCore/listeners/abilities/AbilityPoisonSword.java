package org.genesismc.SoupCore.listeners.abilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;
import java.util.Random;

public class AbilityPoisonSword implements Listener {
    @EventHandler
    public void onDamagePlayer(EntityDamageByEntityEvent e) {
        if ((e.getDamager() instanceof Player) && (e.getEntity() instanceof Player)) {
            Player target = (Player) e.getEntity();
            Player attacker = (Player) e.getDamager();
            ItemStack weapon = attacker.getItemInHand();

            if (Objects.equals(weapon.getType(), Material.IRON_SWORD) && Objects.equals(weapon.getItemMeta().getDisplayName(), ChatColor.GREEN + "Dagger of Venom")) {
                Random rand = new Random();
                int randomNumber = rand.nextInt(10);
                if (randomNumber < 3) // 30% chance
                {
                    PotionEffect poisonEffect = new PotionEffect(PotionEffectType.POISON, 20 * 3, 1);
                    target.addPotionEffect(poisonEffect);
                }
            }
        }
    }
}