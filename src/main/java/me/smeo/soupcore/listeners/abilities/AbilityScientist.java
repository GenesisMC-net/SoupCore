package me.smeo.soupcore.listeners.abilities;

import me.smeo.soupcore.Database.Database;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

import java.util.Objects;

public class AbilityScientist implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerKill(PlayerDeathEvent e)
    {
        System.out.println("Player died");
        Player p = e.getEntity();
        if(p.getKiller() != null) {
            System.out.println("Killer exists (lol)");

            Player killer = p.getKiller();

            Integer activeKit = Integer.valueOf((String) Objects.requireNonNull(Database.getPlayerData(killer, "soupData", "kit")));
            if (activeKit == null) {
                return;
            }
            System.out.println("Killer has a kit active");

            if (Objects.equals(activeKit, 7)) {
                System.out.println("Killer is a scientist");
                PlayerInventory inv = killer.getInventory();
                ItemStack poisonPot = new ItemStack(Material.POTION, 1, (short) 16388);

                boolean emptySlot;
                if (!inv.contains(poisonPot)) {
                    System.out.println("Killer has no poison");
                    emptySlot = false;
                    for (ItemStack item : p.getInventory().getContents()) {
                        if ((item == null) || item.getType() == Material.AIR) {
                            inv.addItem(poisonPot);
                            emptySlot = true;
                            break;
                        }
                    }
                    if (!emptySlot) {
                        inv.setItem(2, poisonPot);
                    }

                }

                int currentDmgPots = 0;
                emptySlot = false;
                for (ItemStack item : inv.getContents()) {
                    if (item == null){
                         emptySlot = true;
                    } else if (Objects.equals(item.getDurability(), (short) 16428)) {
                        currentDmgPots += 1;
                    }
                }
                if (currentDmgPots == 0) {
                    if (!emptySlot) {
                        inv.setItem(1, new ItemStack(Material.POTION, 1, (short) 16428));
                    } else {
                        inv.addItem(new ItemStack(Material.POTION, 1, (short) 16428));
                    }
                } else if (currentDmgPots < 3) {
                    if (!emptySlot) {
                        inv.setItem(1, new ItemStack(Material.POTION, currentDmgPots + 1, (short) 16428));
                    } else {
                        inv.addItem(new ItemStack(Material.POTION, 1, (short) 16428));
                    }
                }
            }
        }
    }
}