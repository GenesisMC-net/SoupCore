package org.genesismc.SoupCore.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;

public class freezeCommand implements CommandExecutor, Listener {
    public static final List<Player> frozenPlayers = new ArrayList<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) return false;

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "There is no player with the name " + ChatColor.WHITE + args[0]);
            return true;
        }
        if (target == sender) return false;

        if (label.equals("freeze")) {
            if (frozenPlayers.contains(target)) {
                sender.sendMessage(ChatColor.WHITE + args[0] + ChatColor.RED + " is already frozen! (/unfreeze to unfreeze)");
                return true;
            }
            freeze(target, sender);
        } else {
            if (!frozenPlayers.contains(target)) {
                sender.sendMessage(ChatColor.WHITE + args[0] + ChatColor.RED + " is not frozen! (/freeze to freeze)");
                return true;
            }
            unfreeze(target, sender);
        }

        return true;
    }

    private static void unfreeze(Player target, CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "You have unfrozen " + ChatColor.WHITE + target.getName());

        target.sendMessage("");
        target.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "You have been unfrozen");
        target.sendMessage("");

        target.setWalkSpeed(0.2F);
        target.setFlySpeed(0.05F);

        spawnCommand.teleportToSpawn(target);

        frozenPlayers.remove(target);

        for (Player online : Bukkit.getOnlinePlayers()) {
            online.showPlayer(target);
            target.showPlayer(online);
        }
    }

    private static void freeze(Player target, CommandSender sender) {
        TextComponent staffMsg = new TextComponent("You have frozen ");
        staffMsg.setColor(ChatColor.GREEN);

        TextComponent name = new TextComponent(target.getName());
        name.setColor(ChatColor.WHITE);
        staffMsg.addExtra(name);

        TextComponent tp = new TextComponent("[TP]");
        tp.setColor(ChatColor.AQUA);
        tp.setBold(true);
        tp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Teleport to " + target.getName()).color(ChatColor.AQUA).create()));
        tp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + target.getName()));

        TextComponent unfreeze = new TextComponent("[UNFREEZE]");
        unfreeze.setColor(ChatColor.RED);
        unfreeze.setBold(true);
        unfreeze.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Unfreeze " + target.getName()).color(ChatColor.RED).create()));
        unfreeze.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/unfreeze " + target.getName()));

        staffMsg.addExtra(" ");
        staffMsg.addExtra(tp);
        staffMsg.addExtra(" ");
        staffMsg.addExtra(unfreeze);

        if (sender instanceof Player) {
            sender.sendMessage("");
            ((Player) sender).spigot().sendMessage(staffMsg);
            sender.sendMessage("");
        } else {
            sender.sendMessage(target.getName() + " has been frozen");
        }

        frozenPlayers.add(target);

        target.sendMessage("");
        target.sendMessage("");
        target.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "!! YOU HAVE BEEN FROZEN BY A STAFF MEMBER !!");
        target.sendMessage(ChatColor.RED + "Attempting to leave the server risks a permanent ban.");
        target.sendMessage("");
        target.sendMessage("");

        target.setWalkSpeed(0.0F);
        target.setFlySpeed(0.0F);
        target.playSound(target.getLocation(), Sound.ANVIL_LAND, 1, 1);

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online == sender) continue;
            online.hidePlayer(target);
            target.hidePlayer(online);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (frozenPlayers.contains(p)) {
            e.setTo(e.getFrom());
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (frozenPlayers.contains(p)) {
            e.setCancelled(true);
            p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1, 1);
            p.sendMessage(ChatColor.RED + "You cannot use commands whilst frozen");
        }
    }
}
