package fr.hyriode.bedwars.game.team.upgrade;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.OreStack;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class BWUpgradeTier {

    private final String keyName;
    private final int tier;
    private final ItemStack itemStack;
    private final List<OreStack> price;

    public BWUpgradeTier(int tier, String keyName, ItemStack itemStack, OreStack... price){
        this.tier = tier;
        this.keyName = keyName;
        this.itemStack = itemStack;
        this.price = Arrays.asList(price);
    }

    public int getTier() {
        return tier;
    }

    public String getKeyName() {
        return keyName;
    }

    public HyriLanguageMessage getName(){
        return HyriBedWars.getLanguageManager().getMessage("upgrade." + this.keyName + ".name");
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public List<OreStack> getPrice() {
        return price;
    }
}
