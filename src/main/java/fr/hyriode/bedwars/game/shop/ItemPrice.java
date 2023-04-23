package fr.hyriode.bedwars.game.shop;

import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.type.BWGameType;
import fr.hyriode.bedwars.utils.InventoryUtils;
import fr.hyriode.bedwars.utils.StringUtils;
import fr.hyriode.hyrame.game.util.value.ValueProvider;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class ItemPrice {

    private ItemMoney itemMoney;
    private Supplier<ValueProvider<Integer>> amount;

    public ItemPrice(ItemMoney itemMoney, Supplier<ValueProvider<Integer>> amount){
        this.itemMoney = itemMoney;
        this.amount = amount;
    }

    public ItemPrice(ItemMoney itemMoney, int amount){
        this(itemMoney, () -> new ValueProvider<>(amount));
    }

    public ItemPrice(ItemMoney itemMoney){
        this(itemMoney, () -> new ValueProvider<>(1));
    }

    public ItemStack getItemStack() {
        return this.itemMoney.getAsItemStack();
    }

    public List<ItemStack> getItemStacks() {
        long amount = this.amount.get().get();
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

    public ItemPrice setItemStack(ItemMoney itemMoney) {
        this.itemMoney = itemMoney;
        return this;
    }

    public String getName(Player player){
        return this.itemMoney.getDisplayName().getValue(player);
    }

    public String getName(BWGamePlayer player){
        return this.itemMoney.getDisplayName().getValue(player);
    }

    public Supplier<ValueProvider<Integer>> getAmount() {
        return amount;
    }

    public void setAmount(Supplier<ValueProvider<Integer>> amount) {
        this.amount = amount;
    }

    public ChatColor getColor() {
        return this.itemMoney.getColor();
    }

    @Override
    public String toString() {
        return this.amount + " " + this.itemMoney.getAsItemStack();
    }

    public boolean hasPrice(Player owner, int amount) {
        return InventoryUtils.hasPrice(owner, this, amount);
    }
}
