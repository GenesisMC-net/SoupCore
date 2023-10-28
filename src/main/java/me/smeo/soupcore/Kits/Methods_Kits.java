package me.smeo.soupcore.Kits;

import me.smeo.soupcore.Database.Database;
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

        inv.setItem(1 + 9, KitDefault.guiAppearance(player, inv));
        inv.setItem(3 + 9, KitVenom.guiAppearance(player, inv));
        inv.setItem(5 + 9, KitSpiderman.guiAppearance(player, inv));
        inv.setItem(7 + 9, KitBlitz.guiAppearance(player, inv));

        inv.setItem(2 + (9 * 2), KitStealth.guiAppearance(player, inv));
        inv.setItem(4 + (9 * 2), KitGrappler.guiAppearance(player, inv));
        inv.setItem(6 + (9 * 2), KitFisherman.guiAppearance(player, inv));

        inv.setItem(1 + (9 * 3), KitScientist.guiAppearance(player, inv));
        inv.setItem(3 + (9 * 3), KitGlider.guiAppearance(player, inv));
        inv.setItem(5 + (9 * 3), KitSoldier.guiAppearance(player, inv));
        inv.setItem(7 + (9 * 3), KitMage.guiAppearance(player, inv));

        inv.setItem(2 + (9 * 4), KitHulk.guiAppearance(player, inv));
        inv.setItem(4 + (9 * 4), KitTank.guiAppearance(player, inv));

        ItemStack glassPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
        inv.setItem(0, glassPane);
        inv.setItem(1, glassPane);
        inv.setItem(9, glassPane);

        inv.setItem(7, glassPane);
        inv.setItem(8, glassPane);
        inv.setItem(17, glassPane);

        inv.setItem(36, glassPane);
        inv.setItem(45, glassPane);
        inv.setItem(46, glassPane);

        inv.setItem(44, glassPane);
        inv.setItem(53, glassPane);
        inv.setItem(52, glassPane);


        for (int i = 0; i < 54; i++) {
            if ((inv.getContents()[i] == null) || (inv.getContents()[i].getType() == Material.AIR))
            {
                inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0));
            }
        }

        player.openInventory(inv);
    }

    // Methods

    public static int getActiveKit(Player player)
    {
        return Integer.parseInt((String) Objects.requireNonNull(Database.getPlayerData(player, "soupData", "kit")));
    }

    public static boolean checkKitPermission(Player player, int kit)
    {
        // TODO Check permission
        return true;
    }

    public static void giveKit(Player player, int kit)
    {
        for (PotionEffect effect : player.getActivePotionEffects())
        {
            player.removePotionEffect(effect.getType());
        }
        switch (kit){
            case 0:
                KitDefault.giveItems(player);
                break;
            case 1:
                KitVenom.giveItems(player);
                break;
            case 2:
                KitSpiderman.giveItems(player);
                break;
            case 3:
                KitBlitz.giveItems(player);
                break;
            case 4:
                KitStealth.giveItems(player);
                break;
            case 5:
                KitGrappler.giveItems(player);
                break;
            case 6:
                KitFisherman.giveItems(player);
                break;
            case 7:
                KitScientist.giveItems(player);
                break;
            case 8:
                KitGlider.giveItems(player);
                break;
            case 9:
                KitSoldier.giveItems(player);
                break;
            case 10:
                KitMage.giveItems(player);
                break;
            case 11:
                KitHulk.giveItems(player);
                break;
            case 12:
                KitTank.giveItems(player);
                break;
        }

        for (ItemStack item : player.getInventory().getContents())
        {
            if ((item == null) || item.getType() == Material.AIR)
            {
                player.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP, 1));
            }
        }
    }

    public static void giveKitPermission(Player player, int kit)
    {
        // TODO Add permission
    }

    public static void selectKit(Player player, int kit)
    {
        if (!checkKitPermission(player, kit))
        {
            player.sendMessage("&cYou do not own this kit!");
            player.closeInventory();
            player.updateInventory();
            return;
        }

        Database.SetPlayerData(player, "soupData", "kit", kit);
    }
}
