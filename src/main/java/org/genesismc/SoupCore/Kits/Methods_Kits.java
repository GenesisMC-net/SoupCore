package org.genesismc.SoupCore.Kits;

import org.bukkit.Sound;
import org.bukkit.inventory.meta.ItemMeta;
import org.genesismc.SoupCore.Database.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Objects;

public class Methods_Kits {
    // Kit GUI

    public static void createKitInventory(Player player)
    {
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLACK + "Kit Selection");

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

        guiBorder(inv);

        player.openInventory(inv);
    }

    public static Inventory previewInventory(String kitName) {
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLACK + kitName + " Preview");

        guiBorder(inv);

        ItemStack backButton = new ItemStack(Material.BARRIER, 1);
        ItemMeta backButtonmeta = backButton.getItemMeta();
        backButtonmeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Back");
        backButton.setItemMeta(backButtonmeta);
        inv.setItem(49, backButton);

        return inv;
    }

    private static void guiBorder(Inventory inv) {
        ItemStack redGlassPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
        ItemStack whiteGlassPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0);
        inv.setItem(0, redGlassPane);
        inv.setItem(1, redGlassPane);
        inv.setItem(9, redGlassPane);

        inv.setItem(7, redGlassPane);
        inv.setItem(8, redGlassPane);
        inv.setItem(17, redGlassPane);

        inv.setItem(36, redGlassPane);
        inv.setItem(45, redGlassPane);
        inv.setItem(46, redGlassPane);

        inv.setItem(44, redGlassPane);
        inv.setItem(53, redGlassPane);
        inv.setItem(52, redGlassPane);

        for (int i = 0; i < 54; i++) {
            if (i % 9 == 0 || (i + 1) % 9 == 0 || i - 9 < 0 || i + 9 > 54)
            {
                if ((inv.getContents()[i] == null) || (inv.getContents()[i].getType() == Material.AIR))
                {
                    inv.setItem(i, whiteGlassPane);
                }
            }
        }
    }

    // Methods

    public static String getActiveKit(Player player)
    {
        return Objects.requireNonNull(Database.getPlayerData(player, "soupData", "kit"));
    }

    public static boolean checkKitPermission(Player player, String kit)
    {
        // TODO Check permission
        return true;
    }

    public static void giveSoup(Player p) {
        for (ItemStack item : p.getInventory().getContents())
        {
            if ((item == null) || item.getType() == Material.AIR)
            {
                p.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP, 1));
            }
        }
    }

    public static void giveKit(Player player, String kit)
    {
        for (PotionEffect effect : player.getActivePotionEffects())
        {
            player.removePotionEffect(effect.getType());
        }
        switch (ChatColor.stripColor(kit)){
            case "Default":
                KitDefault.giveItems(player);
                break;
            case "Venom":
                KitVenom.giveItems(player);
                break;
            case "Spiderman":
                KitSpiderman.giveItems(player);
                break;
            case "Blitz":
                KitBlitz.giveItems(player);
                break;
            case "Stealth":
                KitStealth.giveItems(player);
                break;
            case "Grappler":
                KitGrappler.giveItems(player);
                break;
            case "Fisherman":
                KitFisherman.giveItems(player);
                break;
            case "Scientist":
                KitScientist.giveItems(player);
                break;
            case "Glider":
                KitGlider.giveItems(player);
                break;
            case "Soldier":
                KitSoldier.giveItems(player);
                break;
            case "Mage":
                KitMage.giveItems(player);
                break;
            case "Hulk":
                KitHulk.giveItems(player);
                break;
            case "Tank":
                KitTank.giveItems(player);
                break;
            case "Snail":
                KitSnail.giveItems(player);
                break;
            case "Switcher":
                KitSwitcher.giveItems(player);
                break;
            case "Turbo":
                KitTurbo.giveItems(player);
                break;
        }
        giveSoup(player);
    }

    public static void giveKitPermission(Player player, int kit)
    {
        // TODO Add permission
    }

    public static void selectKit(Player player, String kit)
    {
        if (!checkKitPermission(player, kit))
        {
            player.sendMessage("&cYou do not own this kit!");
            player.closeInventory();
            player.updateInventory();
            return;
        }

        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 0.5F, 0);
        Database.setPlayerData(player, "soupData", "kit", kit);
    }
}
