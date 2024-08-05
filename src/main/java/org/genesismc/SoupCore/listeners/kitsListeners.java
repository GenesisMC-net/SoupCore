package org.genesismc.SoupCore.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.genesismc.SoupCore.Kits.*;

import java.util.Objects;

import static org.genesismc.SoupCore.Kits.Methods_Kits.createKitInventory;
import static org.genesismc.SoupCore.Kits.Methods_Kits.previewInventory;

public class kitsListeners implements Listener
{
    @EventHandler
    public void onClick(InventoryClickEvent e)
    {
        Player player = (Player) e.getWhoClicked();
        String title = ChatColor.stripColor(e.getView().getTitle());
        if (Objects.equals(title, "Kit Selection")) {
            e.setCancelled(true);

            if(e.getCurrentItem()==null){return;}
            if(e.getCurrentItem().getItemMeta()==null){return;}
            if (e.getCurrentItem().getItemMeta().getDisplayName() == null) { return; }

            if(e.getClickedInventory().getType() == InventoryType.PLAYER){return;}

            String selectedKit = e.getCurrentItem().getItemMeta().getDisplayName();

            if (e.getClick().equals(ClickType.LEFT)) {
                selectKit(player, selectedKit);
            } else if (e.getClick().equals(ClickType.RIGHT)) {
                previewKit(player, selectedKit);
            }
        } else if (e.getView().getTitle().contains("Preview")) {
            e.setCancelled(true);

            if(e.getCurrentItem()==null){return;}
            if(e.getCurrentItem().getItemMeta()==null){return;}
            if (e.getCurrentItem().getItemMeta().getDisplayName() == null) { return; }

            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Back")) {
                player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.8F, 1F);
                createKitInventory(player);
            }
        }
    }

    private void selectKit(Player player, String kit) {
        Inventory inv = player.getOpenInventory().getTopInventory();

        Methods_Kits.selectKit(player, kit);
        player.sendMessage(ChatColor.GRAY + "You selected the " + kit + ChatColor.GRAY + " kit");

        // Update GUI
        inv.setItem(1 + 9, KitDefault.guiAppearance(player));
        inv.setItem(2 + 9, KitVenom.guiAppearance(player));
        inv.setItem(3 + 9, KitSpiderman.guiAppearance(player));
        inv.setItem(4 + 9, KitBlitz.guiAppearance(player));
        inv.setItem(5 + 9, KitStealth.guiAppearance(player));
        inv.setItem(6 + 9, KitGrappler.guiAppearance(player));
        inv.setItem(7 + 9, KitFisherman.guiAppearance(player));

        inv.setItem(1 + (9 * 2), KitScientist.guiAppearance(player));
        inv.setItem(2 + (9 * 2), KitGlider.guiAppearance(player));
        inv.setItem(3 + (9 * 2), KitSoldier.guiAppearance(player));
        inv.setItem(4 + (9 * 2), KitMage.guiAppearance(player));
        inv.setItem(5 + (9 * 2), KitHulk.guiAppearance(player));
        inv.setItem(6 + (9 * 2), KitTank.guiAppearance(player));
        inv.setItem(7 + (9 * 2), KitSnail.guiAppearance(player));

        inv.setItem(1 + (9 * 3), KitSwitcher.guiAppearance(player));
        inv.setItem(2 + (9 * 3), KitTurbo.guiAppearance(player));
    }

    private void previewKit(Player player, String kit) {
        Inventory inv = previewInventory(ChatColor.stripColor(kit));
        switch (ChatColor.stripColor(kit)) {
            case "Default":
                KitDefault.preview(inv);
                break;
            case "Venom":
                KitVenom.preview(inv);
                break;
            case "Spiderman":
                KitSpiderman.preview(inv);
                break;
            case "Blitz":
                KitBlitz.preview(inv);
                break;
            case "Stealth":
                KitStealth.preview(inv);
                break;
            case "Grappler":
                KitGrappler.preview(inv);
                break;
            case "Fisherman":
                KitFisherman.preview(inv);
                break;
            case "Scientist":
                KitScientist.preview(inv);
                break;
            case "Glider":
                KitGlider.preview(inv);
                break;
            case "Soldier":
                KitSoldier.preview(inv);
                break;
            case "Mage":
                KitMage.preview(inv);
                break;
            case "Hulk":
                KitHulk.preview(inv);
                break;
            case "Tank":
                KitTank.preview(inv);
                break;
            case "Snail":
                KitSnail.preview(inv);
                break;
            case "Switcher":
                KitSwitcher.preview(inv);
                break;
            case "Turbo":
                KitTurbo.preview(inv);
                break;
        }
        player.openInventory(inv);
    }
}
