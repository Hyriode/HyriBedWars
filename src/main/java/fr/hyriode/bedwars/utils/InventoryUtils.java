package fr.hyriode.bedwars.utils;

import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.shop.ItemMoney;
import fr.hyriode.bedwars.game.shop.ItemPrice;
import fr.hyriode.bedwars.game.shop.material.MaterialShop;
import fr.hyriode.bedwars.game.type.BWGameType;
import fr.hyriode.hyrame.game.util.value.ValueProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class InventoryUtils {

    public static void setInSlot(Player player, int slot, ItemStack itemStack){
        setInSlot(player, slot, itemStack, false);
    }

    public static void setInSlot(Player player, int slot, ItemStack itemStack, boolean hard) {
        PlayerInventory inventory = player.getInventory();
        ItemStack itemSlot = slot >= 0 ? inventory.getItem(slot) : null;

        if(slot < 0) {
            inventory.addItem(itemStack);
            return;
        }
        inventory.setItem(slot, itemStack);
        if(itemSlot != null && !hard){
            inventory.addItem(itemSlot);
        }
    }

    public static void giveInSlot(Player player, int slot, ItemStack itemStack) {
        PlayerInventory inventory = player.getInventory();

        if(canGiveInSlot(player, slot, itemStack)){
            ItemStack output = itemStack.clone();
            ItemStack itemSlot = inventory.getItem(slot);

            if(itemSlot != null && itemSlot.isSimilar(output)){
                output.setAmount(itemSlot.getAmount() + itemStack.getAmount());
            }
            inventory.setItem(slot, output);
            return;
        }
        inventory.addItem(itemStack);
    }

    public static void giveInSlot(BWGamePlayer player, MaterialShop materialShop){
        if(materialShop.getCategory() != null && materialShop.getCategory().getHotbar() != null){
            List<Integer> slots = player.getAccount().getSlotByHotbar(materialShop.getCategory().getHotbar());
            PlayerInventory inventory = player.getPlayer().getInventory();

            for (int slot = 0; slot < 9; slot++) {
                ItemStack itemStack = inventory.getItem(slot);
                if (itemStack != null && itemStack.getType() == Material.AIR) {
                    if (slots.contains(slot)) {
                        giveInSlot(player.getPlayer(), slot, materialShop.getItemShopForPlayer(player).getItemStack(player));
                        return;
                    }
                }
            }
            return;
        }
        giveInSlot(player.getPlayer(), 0, materialShop.getItemShopForPlayer(player).getItemStack(player));
    }

    public static void replace(Player player, ItemStack itemOrigin, ItemStack itemReplace) {
        int slotOrigin = InventoryUtils.getSlotByItemStack(player, itemOrigin);

        setInSlot(player, slotOrigin, itemReplace, true);
    }

    public static boolean canSetInSlot(Player player, int slot) {
        return player.getInventory().getItem(slot) == null;
    }

    public static boolean canGiveInSlot(Player player, int slot, ItemStack itemStack) {
        PlayerInventory inventory = player.getInventory();
        ItemStack itemInventory = inventory.getItem(slot);

        return itemInventory == null
                || itemInventory.isSimilar(itemStack)
                && itemInventory.getAmount() + itemStack.getAmount() <= itemStack.getMaxStackSize();
    }

    public static long getHasPrice(BWGameType gameType, Player player, ItemPrice price) {
        PlayerInventory inventory = player.getInventory();

        int money = price.getAmount().apply(gameType).get();
        for (ItemStack item : inventory) {
            if(item != null && item.isSimilar(price.getItemStack())){
                money -= item.getAmount();
            }
            if(money <= 0) break;
        }
        return money;
    }

    public static void removeMoney(BWGameType gameType, Player player, ItemPrice price) {
        PlayerInventory inventory = player.getInventory();

        int amount = price.getAmount().apply(gameType).get();
        for(int slot = 0;slot < inventory.getSize();++slot){
            ItemStack itemStack = inventory.getItem(slot);
            if(itemStack != null && itemStack.isSimilar(price.getItemStack())){
                if(itemStack.getAmount() - amount <= 0){
                    inventory.setItem(slot, null);
                }else {
                    itemStack.setAmount((int) (itemStack.getAmount() - amount));
                }
                amount -= itemStack.getAmount();
                if(amount <= 0){
                    break;
                }
            }
        }
    }

    public static void remove(Player player, ItemStack itemStack){
        forEachInventory(player, (inventory, slot) -> {
            ItemStack item = inventory.getItem(slot);
            if(item != null && item.isSimilar(itemStack)){
                if(item.getAmount() > 1){
                    item.setAmount(item.getAmount() - 1);
                    return true;
                }
                inventory.setItem(slot, null);
                return true;
            }
            return false;
        });
    }

    public static void removeInHand(Player player){
        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInHand();
        if(item != null){
            if(item.getAmount() > 1){
                item.setAmount(item.getAmount() - 1);
                return;
            }
            inventory.setItemInHand(null);
        }
    }

    public static int getSlotByMaterial(Player player, Material type) {
        PlayerInventory inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack itemInSlot = inventory.getItem(slot);
            if(itemInSlot != null && itemInSlot.getType() == type){
                return slot;
            }
        }
        return -1;
    }

    private static int getSlotByItemStack(Player player, ItemStack itemStack) {
        PlayerInventory inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack itemInSlot = inventory.getItem(slot);
            if(itemInSlot != null && itemInSlot.isSimilar(itemStack)){
                return slot;
            }
        }
        return -1;
    }

    public static void forEachInventory(Player player, BiFunction<PlayerInventory, Integer, Boolean> finish){
        PlayerInventory inventory = player.getInventory();

        for (int slot = 0; slot < inventory.getSize(); slot++) {
            if(finish.apply(inventory, slot)){
                return;
            }
        }
    }

    public static Material[] getSwords(){
        return new Material[]{
                Material.DIAMOND_SWORD,
                Material.GOLD_SWORD,
                Material.IRON_SWORD,
                Material.STONE_SWORD,
                Material.WOOD_SWORD
        };
    }

    public static Material[] getAxes(){
        return new Material[]{
                Material.DIAMOND_AXE,
                Material.GOLD_AXE,
                Material.IRON_AXE,
                Material.STONE_AXE,
                Material.WOOD_AXE
        };
    }

    public static Material[] getPickaxes(){
        return new Material[]{
                Material.DIAMOND_PICKAXE,
                Material.GOLD_PICKAXE,
                Material.IRON_PICKAXE,
                Material.STONE_PICKAXE,
                Material.WOOD_PICKAXE
        };
    }

    public static Material[] getShovels(){
        return new Material[]{
                Material.DIAMOND_SPADE,
                Material.GOLD_SPADE,
                Material.IRON_SPADE,
                Material.STONE_SPADE,
                Material.WOOD_SPADE
        };
    }

    public static Material[] getHoes(){
        return new Material[]{
                Material.DIAMOND_HOE,
                Material.GOLD_HOE,
                Material.IRON_HOE,
                Material.STONE_HOE,
                Material.WOOD_HOE
        };
    }

    public static int getFirstElementOfInt(Set<Integer> set) {
        for (Integer i : set) {
            return i;
        }
        return -1;
    }

    public static boolean hasItem(Player player, ItemStack itemStack) {
        return getAmount(player, itemStack) > 0;
    }

    public static boolean hasMaterial(Player player, Material material) {
        return getAmount(player, material) > 0;
    }

    public static int getAmount(Player player, ItemStack itemStack){
        int amount = 0;
        for (ItemStack stack : player.getInventory()) {
            if(stack != null && stack.isSimilar(itemStack)){
                amount += stack.getAmount();
            }
        }
        return amount;
    }

    public static int getAmount(Player player, Material material){
        int amount = 0;
        for (ItemStack stack : player.getInventory()) {
            if(stack != null && stack.getType() == material){
                amount += stack.getAmount();
            }
        }
        return amount;
    }

    public static void removeOverStack(Player player, ItemStack itemStack){
        PlayerInventory inventory = player.getInventory();
        int amount = InventoryUtils.getAmount(player, itemStack);
        if(amount <= 1) return;
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if(amount > 1){
                if(stack != null && stack.isSimilar(itemStack)){
                    --amount;
                    inventory.setItem(slot, null);
                }
                continue;
            }
            break;
        }
    }

    public static boolean hasPrice(BWGameType gameType, Player player, ItemPrice price) {
        int amount = 0;
        for (ItemStack itemStack : player.getInventory()) {
            if(itemStack != null && itemStack.isSimilar(price.getItemStack())){
                amount += itemStack.getAmount();
            }
        }
        return amount >= price.getAmount().apply(gameType).get();
    }

    public static List<ItemPrice> getMoney(BWGameType gameType, PlayerInventory inventory) {
        List<ItemPrice> money = new ArrayList<>();

        for (int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack itemStack = inventory.getItem(slot);
            if(itemStack != null && ItemMoney.contains(itemStack)){
                if(!money.stream().map(price -> ItemMoney.asMoney(price.getItemStack())).collect(Collectors.toList())
                        .contains(ItemMoney.asMoney(itemStack))) {
                    money.add(new ItemPrice(ItemMoney.asMoney(itemStack), itemStack.getAmount()));
                }else {
                    money.stream().filter(price -> price.getItemStack().getType() == itemStack.getType()).findFirst()
                            .ifPresent(price -> {
                                int amount = price.getAmount().apply(gameType).get() + itemStack.getAmount();
                                price.setAmount((__) -> new ValueProvider<>(amount));
                            });
                }
            }
        }

        return money;
    }

    public static boolean isFull(Player player) {
        PlayerInventory inventory = player.getInventory();
        for (int slot = inventory.getSize(); slot >= 0; slot--) {
            ItemStack itemStack = inventory.getItem(slot);
            if(itemStack == null || itemStack.getType() == Material.AIR){
                return false;
            }
        }
        return true;
    }

    public static int getSlotByXY(int x, int y) {
        x -= 1;
        y -= 1;
        return x+(y*9);
    }
}
