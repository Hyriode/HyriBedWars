package fr.hyriode.bedwars.game;

import fr.hyriode.bedwars.game.npc.inventory.shop.ItemShop;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class BWGameOre {

    public static final ItemShop IRON = new ItemShop(Material.IRON_INGOT, "iron", ChatColor.WHITE);
    public static final ItemShop GOLD = new ItemShop(Material.GOLD_INGOT, "gold", ChatColor.GOLD);
    public static final ItemShop DIAMOND = new ItemShop(Material.DIAMOND, "diamond", ChatColor.AQUA);
    public static final ItemShop EMERALD = new ItemShop(Material.EMERALD, "emerald", ChatColor.DARK_GREEN);
    public static final ItemShop COAL = new ItemShop(Material.COAL, "coal", ChatColor.BLACK);

}
