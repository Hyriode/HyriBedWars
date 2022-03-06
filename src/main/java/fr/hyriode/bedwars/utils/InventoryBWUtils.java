package fr.hyriode.bedwars.utils;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.OreStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class InventoryBWUtils {

    public static boolean hasItems(Player player, List<OreStack> itemStack){
        for (OreStack itemToSell : itemStack) {
            if(getAmountItems(player, itemToSell) >= itemToSell.getAmount())
                return true;
        }
        return false;
    }

    public static boolean hasItems(Player player, ItemStack itemStack){
        return getAmountItems(player, itemStack) > 0;
    }

    public static int getAmountItems(Player player, OreStack itemStack){
        int amount = 0;
        for (ItemStack itemInventory : player.getInventory()) {
            if(itemInventory != null && itemStack.getItem().getItemStack().getType() == itemInventory.getType())
                amount += itemInventory.getAmount();
        }
        return amount;
    }

    public static int getAmountItems(Player player, ItemStack itemStack){
        int amount = 0;
        for (ItemStack itemInventory : player.getInventory()) {
            if(itemInventory != null && itemStack.getType() == itemInventory.getType())
                amount += itemInventory.getAmount();
        }
        return amount;
    }

    public static void removeItems(Player player, List<OreStack> buys){
        for(OreStack itemStack : buys){
            int amount = itemStack.getAmount();
            for(int i = 0; i < player.getInventory().getSize() ; ++i){
                ItemStack itemInventory = player.getInventory().getItem(i);
                if(itemInventory != null && itemInventory.getType() == itemStack.getItem().getItemStack().getType() && amount > 0){
                    if(itemInventory.getAmount() > amount){
                        itemInventory.setAmount(itemInventory.getAmount() - amount);
                        break;
                    }else{
                        player.getInventory().setItem(i, null);
                        amount -= itemInventory.getAmount();
                    }
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

    public static List<ItemStack> getItemsInventory(Player player, ItemStack... itemStacks) {
        List<ItemStack> items = new ArrayList<>();
        for(ItemStack itemStack : player.getInventory())
            for(ItemStack itemSearch : itemStacks)
                if(itemStack != null && itemStack.getType() == itemSearch.getType())
                    items.add(itemStack.clone());
        return items;
    }
}
