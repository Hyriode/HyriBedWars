package fr.hyriode.bedwars.game.shop;

import fr.hyriode.hyrame.language.HyriLanguageMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class ItemMoney {

    public static final ItemMoney IRON = new ItemMoney("iron", Material.IRON_INGOT, ChatColor.WHITE);
    public static final ItemMoney GOLD = new ItemMoney("gold", Material.GOLD_INGOT, ChatColor.GOLD);
    public static final ItemMoney DIAMOND = new ItemMoney("diamond", Material.DIAMOND, ChatColor.AQUA);
    public static final ItemMoney EMERALD = new ItemMoney("emerald", Material.EMERALD, ChatColor.DARK_GREEN);

    public static final ItemMoney[] VALUES = new ItemMoney[]{IRON, GOLD, DIAMOND, EMERALD};

    private final String name;
    private final Material material;
    private final ChatColor color;

    public ItemMoney(String name, Material material, ChatColor color){
        this.name = name;
        this.material = material;
        this.color = color;
    }

    public static boolean contains(ItemStack itemStack) {
        return Arrays.stream(VALUES).anyMatch(itemMoney -> itemMoney.getMaterial().equals(itemStack.getType()));
    }

    public static ItemMoney asMoney(ItemStack itemStack) {
        return Arrays.stream(VALUES).filter(itemMoney -> itemMoney.getMaterial().equals(itemStack.getType()))
                .findFirst().orElse(null);
    }

    public String getName() {
        return name;
    }

    public HyriLanguageMessage getDisplayName(){
        return HyriLanguageMessage.get("item." + name + ".name");
    }

    public Material getMaterial() {
        return this.material;
    }

    public ItemStack getAsItemStack(){
        return new ItemStack(this.material).clone();
    }

    public ItemStack getAsItemStack(int amount){
        return new ItemStack(this.material, amount).clone();
    }

    public ChatColor getColor() {
        return this.color;
    }
}
