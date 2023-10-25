package me.smeo.soupcore.Kits;

import me.smeo.soupcore.Database.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class Methods_Kits {

    // KITS - Kit Value in order (0, 1, 2 etc) //

    private static void stealth(Player p)
    {
        PlayerInventory inv = p.getInventory();
        inv.clear();

        ItemStack[] armor = new ItemStack[] {new ItemStack(Material.LEATHER_HELMET), new ItemStack(Material.LEATHER_CHESTPLATE), new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(Material.LEATHER_BOOTS)};

        for (ItemStack armorPiece: armor) {
            armorPiece.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
            armorPiece.addEnchantment(Enchantment.DURABILITY, 20);
        }

        inv.setArmorContents(armor);

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));

        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        sword.addEnchantment(Enchantment.DURABILITY, 3);

        inv.setItem(0, sword);

        // Ability
        ItemStack ninjaStar = new ItemStack(Material.NETHER_STAR, 4);

        ItemMeta ninjaStarMeta = ninjaStar.getItemMeta();

        ArrayList<String> ninjaStarLore = new ArrayList<>();
        ninjaStarLore.add("");
        ninjaStarLore.add(ChatColor.WHITE + "Right Click: " + ChatColor.RED + "Star Throw");
        ninjaStarLore.add(ChatColor.GRAY + "Throw a ninja star that deals blindness");
        ninjaStarLore.add(ChatColor.GRAY + "for 5 seconds to any player it hits!");
        ninjaStarLore.add("");
        ninjaStarLore.add(ChatColor.WHITE + "Every kill with the Ninja Kit:" + ChatColor.GREEN + " +1 Ninja Star");
        ninjaStarMeta.setLore(ninjaStarLore);

        ninjaStarMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Ninja Star");

        ninjaStar.setItemMeta(ninjaStarMeta);
        inv.setItem(1, ninjaStar);
    }

    private static void fisherman(Player p)
    {
        PlayerInventory inv = p.getInventory();
        inv.clear();
        ItemStack helmet = new ItemStack(Material.GOLD_HELMET);
        helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        helmet.addEnchantment(Enchantment.DURABILITY, 3);
        inv.setHelmet(helmet);

        inv.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        inv.setLeggings(new ItemStack(Material.IRON_LEGGINGS));

        ItemStack boots = new ItemStack(Material.GOLD_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        boots.addEnchantment(Enchantment.DURABILITY, 3);
        inv.setBoots(boots);

        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        inv.setItem(0, sword);

        // TODO ability

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));

    }

    // Kit GUI

    public static void createKitInventory(Player player)
    {
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLACK + "Kit Selection");

        inv.setItem(1 + 9, KitDefault.guiAppearance(player, inv));
        inv.setItem(3 + 9, KitVenom.guiAppearance(player, inv));
        inv.setItem(5 + 9, KitSpiderman.guiAppearance(player, inv));
        inv.setItem(7 + 9, KitBlitz.guiAppearance(player, inv));
        inv.setItem(1 + (9 * 3), KitStealth.guiAppearance(player, inv));
        inv.setItem(3 + (9 * 3), KitGrappler.guiAppearance(player, inv));
        inv.setItem(5 + (9 * 3), KitFisherman.guiAppearance(player, inv));
        inv.setItem(7 + (9 * 3), KitScientist.guiAppearance(player, inv));

        for (int i = 0; i < 54; i++) {
            if ((inv.getContents()[i] == null) || (inv.getContents()[i].getType() == Material.AIR))
            {
                inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
            }
        }

        player.openInventory(inv);
    }

    // Methods

    public static int getActiveKit(Player player)
    {
        Integer activeKit = Database.getPlayerData(player, "kit");
        if (activeKit == null)
        {
            activeKit = 0;
        }
        return activeKit;
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
//            case 8:
//                Kit.giveItems(player);
//                break;
//            case 9:
//                Kit.giveItems(player);
//                break;
//            case 10:
//                Kit.giveItems(player);
//                break;
//            case 11:
//                Kit.giveItems(player);
//                break;
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

        Database.SetPlayerData(player, "kit", kit);
    }
}
