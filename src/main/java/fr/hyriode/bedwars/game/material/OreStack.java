package fr.hyriode.bedwars.game.material;

import fr.hyriode.bedwars.game.material.upgradable.ArmorBW;
import org.bukkit.inventory.ItemStack;

public class OreStack {

    private final ItemShop item;
    private int amount;

    public OreStack(ItemShop item, int amount){
        this.item = item;
        this.amount = amount;
    }

    public OreStack(ItemShop item){
        this(item, 1);
    }

    public OreStack(ArmorBW item){
        this(item, 1);
    }

    public void addAmount(int amount){
        this.amount += amount;
    }

    public void setAmount(int amount){
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public ItemShop getItem() {
        return item;
    }

    public ItemStack getItemStack(){
        ItemStack itemStack = new ItemStack(item.getItemStack());
        itemStack.setAmount(amount);
        return itemStack;
    }

}
