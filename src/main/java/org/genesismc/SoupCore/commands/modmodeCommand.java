package org.genesismc.SoupCore.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
public class modmodeCommand implements CommandExecutor, Listener {

    public static final List<UUID> modmodeActive = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        if (modmodeActive.contains(p.getUniqueId())) {
            modmodeActive.remove(p.getUniqueId());
            p.getInventory().clear();
            p.sendMessage(ChatColor.RED + "Disabled Mod Mode");
        } else {
            modmodeActive.add(p.getUniqueId());
            modInventory(p);
            p.sendMessage(ChatColor.GREEN + "Enabled Mod Mode");
        }

        return true;
    }

    private static void modInventory(Player p) {
        Inventory inv = p.getInventory();
        inv.clear();

        ItemStack dye = new ItemStack(Material.INK_SACK, 1, DyeColor.GRAY.getDyeData());
        ItemMeta dyeMeta = dye.getItemMeta();
        dyeMeta.setDisplayName(ChatColor.YELLOW + "Vanish" + ChatColor.DARK_GRAY + ChatColor.BOLD + " > " + ChatColor.RESET + ChatColor.RED + "Disabled" + ChatColor.GRAY + " (Right-Click)");
        dye.setItemMeta(dyeMeta);

        ItemStack compass = new ItemStack(Material.COMPASS);

        ItemStack paper = new ItemStack(Material.PAPER);

        inv.setItem(3, compass);
        inv.setItem(5, paper);
        inv.setItem(8, dye);
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!modmodeActive.contains(p.getUniqueId())) return;

        ItemStack item = p.getItemInHand();
        if (item == null) return;
        if (item.getItemMeta() == null) return;

        String action = ChatColor.stripColor(item.getItemMeta().getDisplayName());
        if (action.contains("Vanish")) {
            ItemStack dye;
            ItemMeta dyeMeta;
            if (action.contains("Disabled")) {
                Bukkit.dispatchCommand(p, "vanish on");
                dye = new ItemStack(Material.INK_SACK, 1, DyeColor.LIME.getDyeData());
                dyeMeta = dye.getItemMeta();
                dyeMeta.setDisplayName(ChatColor.YELLOW + "Vanish" + ChatColor.DARK_GRAY + ChatColor.BOLD + " > " + ChatColor.RESET + ChatColor.GREEN + "Enabled" + ChatColor.GRAY + " (Right-Click)");
            } else {
                Bukkit.dispatchCommand(p, "vanish off");
                dye = new ItemStack(Material.INK_SACK, 1, DyeColor.GRAY.getDyeData());
                dyeMeta = dye.getItemMeta();
                dyeMeta.setDisplayName(ChatColor.YELLOW + "Vanish" + ChatColor.DARK_GRAY + ChatColor.BOLD + " > " + ChatColor.RESET + ChatColor.RED + "Disabled" + ChatColor.GRAY + " (Right-Click)");
            }
            dye.setItemMeta(dyeMeta);
            p.setItemInHand(dye);
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (modmodeActive.contains(e.getWhoClicked().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {
        if (modmodeActive.contains(e.getEntity().getUniqueId())) {
            e.setCancelled(true);
            e.setDamage(0);
        }
    }
}
