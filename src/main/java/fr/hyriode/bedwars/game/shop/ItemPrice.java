package fr.hyriode.bedwars.game.shop;

import fr.hyriode.bedwars.utils.InventoryUtils;
import fr.hyriode.bedwars.utils.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ItemPrice {

    private ItemMoney itemMoney;
    private long amount;

    public ItemPrice(ItemMoney itemMoney, long amount){
        this.itemMoney = itemMoney;
        this.amount = amount;
    }

    public ItemPrice(ItemMoney itemMoney){
        this(itemMoney, 1);
    }

    public ItemStack getItemStack() {
        return this.itemMoney.getAsItemStack();
    }

    public List<ItemStack> getItemStacks() {
        List<ItemStack> itemStacks = new ArrayList<>();
        int maxStack = this.itemMoney.getAsItemStack().getMaxStackSize();
        int quotient = (int) (this.amount / maxStack);
        int rest = (int) (this.amount % maxStack);
        for(int i = 0; i < quotient; i++){
            itemStacks.add(this.itemMoney.getAsItemStack(maxStack));
        }
        if(rest > 0){
            itemStacks.add(this.itemMoney.getAsItemStack(rest));
        }
        return itemStacks;
    }

    public void setItemStack(ItemMoney itemMoney) {
        this.itemMoney = itemMoney;
    }

    public String getName(Player player){
        return this.itemMoney.getDisplayName().getForPlayer(player);
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public ChatColor getColor() {
        return this.itemMoney.getColor();
    }

    public long getHasPrice(Player player){
        return InventoryUtils.getHasPrice(player, this);
    }

    public String getDisplayCostPrice(Player player){
        return StringUtils.getDisplayCostPrice(player, this);
    }

    public String getDisplayPrice(Player player){
        return StringUtils.getDisplayPrice(player, this);
    }

    @Override
    public String toString() {
        return this.amount + " " + this.itemMoney.getAsItemStack();
    }

    public boolean hasPrice(Player owner) {
        return InventoryUtils.hasPrice(owner, this);
    }
}
