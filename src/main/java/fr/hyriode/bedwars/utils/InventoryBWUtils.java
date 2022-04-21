package fr.hyriode.bedwars.utils;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGameOre;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.material.BWMaterial;
import fr.hyriode.bedwars.game.material.ItemShop;
import fr.hyriode.bedwars.game.material.OreStack;
import fr.hyriode.hyrame.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InventoryBWUtils {

    public static boolean hasPrice(Player player, List<OreStack> itemStack){
        int i = 0;
        for (OreStack itemToSell : itemStack) {
            if(hasPrice(player, itemToSell))
                ++i;
        }
        return i == itemStack.size();
    }

    public static boolean hasPrice(Player player, OreStack price){
        return getAmountItems(player, price) >= price.getAmount();
    }

    public static boolean hasItem(Player player, ItemStack itemStack){
        return getAmountItems(player, itemStack.getType()) > 0;
    }

    public static boolean hasItems(Player player, ItemStack... itemStack){
        int i = 0;
        for(ItemStack item : itemStack){
            if(getAmountItems(player, item.getType()) > 0)
                ++i;
        }
        return i > 0;
    }

    public static boolean hasItems(Player player, List<ItemStack> itemInventory, ItemStack... itemsInventory){
        itemInventory.addAll(Arrays.asList(itemsInventory));
        return hasItems(player, itemInventory.toArray(new ItemStack[0]));
    }

    public static int getAmountItems(Player player, OreStack itemStack){
        int amount = 0;
        for (ItemStack itemInventory : player.getInventory()) {
            if(itemInventory != null && itemStack.getItem().getItemStack().getType() == itemInventory.getType())
                amount += itemInventory.getAmount();
        }
        return amount;
    }

    public static int getAmountItems(Player player, Material material){
        int amount = 0;
        for (ItemStack itemInventory : player.getInventory()) {
            if(itemInventory != null && material == itemInventory.getType())
                amount += itemInventory.getAmount();
        }
        return amount;
    }

    public static void removeItems(Player player, OreStack buys){
        int amount = buys.getAmount();
        for(int i = 0; i < player.getInventory().getSize() ; ++i){
            ItemStack itemInventory = player.getInventory().getItem(i);
            if(itemInventory != null && itemInventory.getType() == buys.getItem().getItemStack().getType() && amount > 0){
                if(itemInventory.getAmount() > amount){
                    itemInventory.setAmount(itemInventory.getAmount() - amount);
                    break;
                }else{
                    player.getInventory().setItem(i, null);
                    amount -= itemInventory.getAmount();
                }
            }
        }
        player.updateInventory();
    }

    public static void removeItem(Player player, ItemStack itemStack){
        int amount = itemStack.getAmount();
        for(int i = 0; i < player.getInventory().getSize() ; ++i){
            ItemStack itemInventory = player.getInventory().getItem(i);
            if(itemInventory != null && itemInventory.getType() == itemStack.getType() && amount > 0){
                if(itemInventory.getAmount() > amount){
                    itemInventory.setAmount(itemInventory.getAmount() - amount);
                    break;
                }else{
                    player.getInventory().setItem(i, null);
                    amount -= itemInventory.getAmount();
                }
            }
        }
        player.updateInventory();
    }

    public static void replaceItem(Player player, ItemStack previousItem, ItemStack newItem){
        for(int i = 0; i < player.getInventory().getSize() ; ++i){
            ItemStack itemInventory = player.getInventory().getItem(i);
            if(itemInventory != null && itemInventory.getType() == previousItem.getType()){
                player.getInventory().setItem(i, newItem);
            }
        }
        player.updateInventory();
    }

    public static String getTierString(Player player, int tier){
        return HyriBedWars.getLanguageManager().getValue(player, "tier." + tier);
    }

    public static int getItemSlot(Player player, ItemStack itemStack){
        for(int i = 0 ; i < player.getInventory().getSize() ; ++i){
            if(player.getInventory().getItem(i).getType() == itemStack.getType())
                return i;
        }
        return -1;
    }

    public static void changeItemsSlot(Player player, Consumer<ItemStack> action, ItemStack... itemStacks){
        for(int i = 0 ; i < player.getInventory().getSize() ; ++i){
            for(ItemStack itemStack : itemStacks) {
                if (player.getInventory().getItem(i) != null && player.getInventory().getItem(i).getType() == itemStack.getType()){
                    action.accept(player.getInventory().getItem(i));
                }
            }
        }
    }

    public static void setItemsSlot(Player player, Function<Integer, ItemStack> action, ItemStack... itemStacks){
        for(int i = 0 ; i < player.getInventory().getSize() ; ++i){
            for(ItemStack itemStack : itemStacks) {
                if (player.getInventory().getItem(i) != null && player.getInventory().getItem(i).getType() == itemStack.getType()){
                    player.getInventory().setItem(i, action.apply(i));
                }
            }
        }
    }

    public static List<OreStack> getOresInventory(Player player, ItemShop... ores) {
        List<OreStack> items = new ArrayList<>();
        for(ItemStack itemStack : player.getInventory()) {
            for (ItemShop itemSearch : ores) {
                if (itemStack != null && itemStack.getType() == itemSearch.getItemStack().getType()) {
                    if(!items.stream().map(oreStack -> oreStack.getItemStack().getType()).collect(Collectors.toList()).contains(itemStack.getType())){
                        items.add(new OreStack(itemSearch, itemStack.getAmount()));
                    }else{
                        items.forEach(oreStack -> {
                            if(oreStack.getItemStack().getType() == itemSearch.getItemStack().getType()){
                                oreStack.addAmount(itemStack.getAmount());
                            }
                        });
                    }
                }
            }
        }
        return items;
    }

    public static boolean hasItemInSlot(Player player, int slot){
        return player.getInventory().getItem(slot) != null;
    }

    public static void addItem(Player player, int slot, ItemStack itemStack){
        if(slot == -1) {
            player.getInventory().addItem(itemStack);
            return;
        }
        ItemStack item = player.getInventory().getItem(slot) != null ? player.getInventory().getItem(slot).clone() : null;
        player.getInventory().setItem(slot, itemStack);
        if(item != null) {
            player.getInventory().addItem(item);
        }
    }

    public static boolean isFull(Player player, ItemStack itemStack){
        for (ItemStack stack : player.getInventory()) {
            if(stack == null)
                return false;
        }
        return true;
    }

    public static boolean isItem(ItemStack base, ItemStack... items) {
        for(ItemStack item : items){
            if(base.getType() == item.getType()){
                return true;
            }
        }
        return false;
    }

    public static boolean isItem(ItemStack base, List<ItemStack> items){
        return isItem(base, items);
    }

    public static boolean removeDuplicates(Player player, Material material){
        int amount = getAmountItems(player, material);
        if(amount > 0){
            for(int i = 0; i < player.getInventory().getSize() ;++i){
                ItemStack item = player.getInventory().getItem(i);
                if(item != null && item.getType() == material){
                    player.getInventory().setItem(i, null);
                    --amount;
                }
                if(amount <= 0){
                    break;
                }
            }
        }
        return false;
    }
}
