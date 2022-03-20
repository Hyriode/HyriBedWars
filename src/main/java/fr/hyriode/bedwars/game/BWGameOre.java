package fr.hyriode.bedwars.game;

import fr.hyriode.bedwars.game.material.ItemShop;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class BWGameOre {

    public static final ItemShop IRON = new ItemShop("iron", Material.IRON_INGOT).setColor(ChatColor.WHITE);
    public static final ItemShop GOLD = new ItemShop("gold", Material.GOLD_INGOT).setColor(ChatColor.GOLD);
    public static final ItemShop DIAMOND = new ItemShop("diamond", Material.DIAMOND).setColor(ChatColor.AQUA);
    public static final ItemShop EMERALD = new ItemShop("emerald", Material.EMERALD).setColor(ChatColor.DARK_GREEN);
    public static final ItemShop COAL = new ItemShop("coal", Material.COAL).setColor(ChatColor.BLACK);

}
