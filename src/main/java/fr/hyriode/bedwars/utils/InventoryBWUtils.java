package fr.hyriode.bedwars.utils;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.npc.inventory.shop.ItemShopStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InventoryBWUtils {

    public static boolean hasItems(Player player, List<ItemShopStack> itemStack){
        for (ItemShopStack itemToSell : itemStack) {
            if(getAmountItems(player, itemToSell) >= itemToSell.getAmount())
                return true;
        }
        return false;
    }

    public static boolean hasItems(Player player, ItemStack itemStack){
        return getAmountItems(player, itemStack) > 0;
    }

    public static int getAmountItems(Player player, ItemShopStack itemStack){
        int amount = 0;
        for (ItemStack itemInventory : player.getInventory()) {
            if(itemInventory != null && itemStack.getItem().getMaterial().getType() == itemInventory.getType())
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

    public static void removeItems(Player player, List<ItemShopStack> buys){
        for(ItemShopStack itemStack : buys){
            int amount = itemStack.getAmount();
            for(int i = 0; i < player.getInventory().getSize() ; ++i){
                ItemStack itemInventory = player.getInventory().getItem(i);
                if(itemInventory != null && itemInventory.getType() == itemStack.getItem().getMaterial().getType() && amount > 0){
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
}
