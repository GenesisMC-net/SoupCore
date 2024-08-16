package org.genesismc.SoupCore;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.genesismc.SoupCore.listeners.abilities.AbilityNuke;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KillStreaks {
    public static final List<Integer> killStreakMilestones = new ArrayList<>();

    public static void initialise() {
        killStreakMilestones.add(5);
        killStreakMilestones.add(10);
        killStreakMilestones.add(15);
        killStreakMilestones.add(25);
        killStreakMilestones.add(35);
        killStreakMilestones.add(50);
    }

    public static void rewardPlayer(Player p, int ks) {
        if (!Objects.equals(p.getWorld().getName(), "world")) return;

        Inventory inv = p.getInventory();
        String reward;
        switch (ks) {
            case 5:
                ItemStack gapples = new ItemStack(Material.GOLDEN_APPLE, 3, (short) 1);
                if (inv.getItem(1).getType() == Material.MUSHROOM_SOUP) {
                    inv.setItem(1, gapples);
                } else {
                    inv.setItem(2, gapples);
                }
                reward = "3x God Apples";
                break;
            case 10:
                // TODO: Random perk (requires perks being done)
                reward = "Perk (TBD)";
                break;
            case 15:
                PotionEffect strengthTwo = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 60 * 3, 1);
                p.addPotionEffect(strengthTwo);
                reward = "Strength II (3 mins)";
                break;
            case 25:
                Zombie bodyguard = (Zombie) p.getWorld().spawnEntity(p.getLocation().add(new Vector(1, 0, 1)), EntityType.ZOMBIE);
                bodyguard.setCustomName(ChatColor.WHITE + p.getName() + "'s Bodyguard " + ChatColor.RED + "[100HP]");
                bodyguard.setCustomNameVisible(true);
                bodyguard.setMaxHealth(100);
                bodyguard.setHealth(100);
                bodyguard.setCanPickupItems(false);
                bodyguard.setBaby(false);
                bodyguard.setVillager(false);
                bodyguard.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0), false);
                bodyguard.setTarget(p);

                ItemStack[] armour = new ItemStack[]{
                        new ItemStack(Material.IRON_BOOTS),
                        new ItemStack(Material.IRON_LEGGINGS),
                        new ItemStack(Material.IRON_CHESTPLATE),
                        new ItemStack(Material.IRON_HELMET)
                };
                bodyguard.getEquipment().setArmorContents(armour);

                reward = "Body Guard";
                break;
            case 35:
                // "Grandma's Soup" (16x enchant soup - heals to max)
                ItemStack gSoup = new ItemStack(Material.MUSHROOM_SOUP, 16);
                ItemMeta gSoupMeta = gSoup.getItemMeta();
                gSoupMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Grandma's Soup");
                gSoupMeta.addEnchant(Enchantment.LUCK, 1, true);
                gSoupMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                ArrayList<String> lore = new ArrayList<>();
                lore.add("");
                lore.add(ChatColor.WHITE + "Eat soup: " + ChatColor.RED + "Regain max health");
                lore.add(ChatColor.GRAY + "Specially cooked by 418k's Grandma");

                gSoupMeta.setLore(lore);
                gSoup.setItemMeta(gSoupMeta);

                if (inv.getItem(1).getType() == Material.MUSHROOM_SOUP) {
                    inv.setItem(1, gSoup);
                } else {
                    inv.setItem(2, gSoup);
                }
                reward = "16x Grandma's Soup";
                break;
            case 50:
                reward = "Nuke";
                break;
            default:
                return;
        }

        for (Player online : p.getWorld().getPlayers()) {
            online.sendMessage(ChatColor.GREEN + p.getName() + ChatColor.AQUA + " has reached a killstreak of " + ChatColor.GREEN + ks);
        }
        p.sendMessage(ChatColor.AQUA + "You have been rewarded: " + ChatColor.YELLOW + reward);

        if (reward.equals("Nuke")) { // so the messages show in the correct order
            AbilityNuke.spawnNuke(p);
        }
    }
}
