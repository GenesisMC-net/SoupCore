package me.smeo.soupcore.listeners.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Objects;

public class AbilityScientist implements Listener {
    @EventHandler
    public void onDamagePlayer(PlayerDeathEvent e)
    {
        Player p = e.getEntity();
        if(p.getKiller() != null)
        {
            Player killer = p.getKiller();
            if (Objects.equals(Integer.valueOf((String) Database.getPlayerData(killer, "soupData", "kit"), 7))
            {
                PlayerInventory inv = killer.getInventory();
                ItemStack poisonPot = new ItemStack(Material.POTION, 1, (short)16388);

                if (!inv.contains(poisonPot))
                {
                    if (inv.contains((Material) null))
                    {
                        inv.addItem(poisonPot);
                    } else {
                        inv.setItem(2, poisonPot);
                    }
                }

                for (ItemStack item: inv.getContents()) {
                    if (Objects.equals(item.getDurability(), (short)16428)) { // Instant Damage
                        if (item.getAmount() < 3) {
                            inv.setItem(1, new ItemStack(Material.POTION, item.getAmount() + 1, (short)16428));
                        }
                        break;
                    } else {
                        if (inv.contains((Material) null))
                        {
                            inv.addItem(new ItemStack(Material.POTION, 1, (short)16428));
                        } else {
                            inv.setItem(1, new ItemStack(Material.POTION, 1, (short)16428));
                        }
                        break;
                    }
                }
            }
        }
    }
}