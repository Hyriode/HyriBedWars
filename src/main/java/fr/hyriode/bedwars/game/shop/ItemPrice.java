package fr.hyriode.bedwars.game.shop;

import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.type.BWGameType;
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
    private Function<BWGameType, Long> amount;

    public ItemPrice(ItemMoney itemMoney, Function<BWGameType, Long> amount){
        this.itemMoney = itemMoney;
        this.amount = amount;
    }

    public ItemPrice(ItemMoney itemMoney, long amount){
        this(itemMoney, (__) -> amount);
    }

    public ItemPrice(ItemMoney itemMoney){
        this(itemMoney, (__) -> 1L);
    }

    public ItemStack getItemStack() {
        return this.itemMoney.getAsItemStack();
    }

    public List<ItemStack> getItemStacks(BWGameType gameType) {
        long amount = this.amount.apply(gameType);
        List<ItemStack> itemStacks = new ArrayList<>();
        int maxStack = this.itemMoney.getAsItemStack().getMaxStackSize();
        int quotient = (int) (amount / maxStack);
        int rest = (int) (amount % maxStack);
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
        return this.itemMoney.getDisplayName().getValue(player);
    }

    public String getName(BWGamePlayer player){
        return this.itemMoney.getDisplayName().getValue(player);
    }

    public Function<BWGameType, Long> getAmount() {
        return amount;
    }

    public void setAmount(Function<BWGameType, Long> amount) {
        this.amount = amount;
    }

    public ChatColor getColor() {
        return this.itemMoney.getColor();
    }

    public String getDisplayCostPrice(BWGameType gameType, Player player){
        return StringUtils.getDisplayCostPrice(gameType, player, this);
    }

    public String getDisplayPrice(BWGameType gameType, Player player){
        return StringUtils.getDisplayPrice(gameType, player, this);
    }

    @Override
    public String toString() {
        return this.amount + " " + this.itemMoney.getAsItemStack();
    }

    public boolean hasPrice(BWGameType gameType, Player owner) {
        return InventoryUtils.hasPrice(gameType, owner, this);
    }
}
