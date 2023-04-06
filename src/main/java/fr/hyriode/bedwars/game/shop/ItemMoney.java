package fr.hyriode.bedwars.game.shop;

import fr.hyriode.api.language.HyriLanguageMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public enum ItemMoney {
    IRON("iron", Material.IRON_INGOT, ChatColor.WHITE),
    GOLD("gold", Material.GOLD_INGOT, ChatColor.GOLD),
    DIAMOND("diamond", Material.DIAMOND, ChatColor.AQUA),
    EMERALD("emerald", Material.EMERALD, ChatColor.DARK_GREEN);

    private final String name;
    private final Material material;
    private final ChatColor color;

    ItemMoney(String name, Material material, ChatColor color){
        this.name = name;
        this.material = material;
        this.color = color;
    }

    public static boolean contains(ItemStack itemStack) {
        return Arrays.stream(ItemMoney.values()).anyMatch(itemMoney -> itemMoney.getMaterial().equals(itemStack.getType()));
    }

    public static ItemMoney asMoney(ItemStack itemStack) {
        return Arrays.stream(ItemMoney.values()).filter(itemMoney -> itemMoney.getMaterial().equals(itemStack.getType()))
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
