package fr.hyriode.bedwars.game.listener.item;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.utils.InventoryUtils;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.hyrame.game.HyriGameState;
import fr.hyriode.hyrame.listener.HyriListener;
import fr.hyriode.hyrame.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryPlayer;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemListener extends HyriListener<HyriBedWars> {

    public ItemListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event){
        Player player = event.getPlayer();
        BWGamePlayer bwPlayer = this.getGamePlayer(player);
        Item itemPickup = event.getItem();
        ItemStack itemStackPickup = itemPickup.getItemStack().clone();

        //TODO ignore condition for host

        if(itemStackPickup.getType() == Material.WOOD_SWORD){
            event.setCancelled(true);
            return;
        }

        if(ItemUtil.isSword(itemStackPickup) && player.getInventory().contains(bwPlayer.getSword())){
            player.getInventory().remove(bwPlayer.getSword());
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        Player player = event.getPlayer();
        Item itemDropped = event.getItemDrop();
        ItemStack itemStackDropped = itemDropped.getItemStack();

        if(MetadataReferences.isPermanent(itemStackDropped)){
            itemDropped.remove();
            InventoryUtils.giveInSlot(player, 0, itemStackDropped);
            return;
        }
        if(!containSword(player)){
            this.getGamePlayer(player).giveSword();
        }
    }

    @EventHandler
    public void onInteractInventory(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        BWGamePlayer bwPlayer = this.plugin.getGame().getPlayer(player);
        ItemStack itemSword = bwPlayer.getSword();
        if(bwPlayer.isSpectator() || bwPlayer.isDead()) return;

        if(event.getSlotType() == InventoryType.SlotType.CRAFTING){
            event.setCancelled(true);
            return;
        }

        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.clearOverSword(player, itemSword), 2L);

        if(event.getClickedInventory() != null && event.getView() != null && event.getView().getTopInventory() != null
                && event.getClickedInventory().equals(event.getView().getTopInventory())
                && (MetadataReferences.isPermanent(event.getCurrentItem())
                || MetadataReferences.isPermanent(event.getCursor()))) {
            event.setCancelled(true);
            return;
        }

        if(event.getAction() == InventoryAction.HOTBAR_SWAP || event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD){
            ItemStack itemToSwitch = player.getInventory().getItem(event.getHotbarButton());
            if(MetadataReferences.isPermanent(itemToSwitch) && !(event.getClickedInventory() instanceof CraftInventoryPlayer)){
                event.setCancelled(true);
                return;
            }
        }

        if(event.getView().getTopInventory().getType() == InventoryType.CRAFTING) return;

        if(event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY){
            if(MetadataReferences.isPermanent(event.getCurrentItem())){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event){
        BWGamePlayer bwPlayer = this.getGamePlayer((Player) event.getWhoClicked());
        if(bwPlayer.isSpectator() || bwPlayer.isDead()) return;
        int slot = InventoryUtils.getFirstElementOfInt(event.getRawSlots());

        if(event.getInventory().getType() == InventoryType.CRAFTING){
            if(slot >= 1 && slot <= 4) {
                event.setCancelled(true);
            }
            return;
        }

        if(event.getOldCursor() != null && MetadataReferences.isPermanent(event.getOldCursor())) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();
        BWGamePlayer bwPlayer = this.getGamePlayer(player);
        ItemStack itemSword = bwPlayer.getSword();
        if(bwPlayer.isSpectator() || bwPlayer.isDead()) return;
        if(this.plugin.getGame().getState() != HyriGameState.PLAYING) return;

        if(!containSword(player) && !event.getPlayer().getItemOnCursor().isSimilar(itemSword)){
            bwPlayer.giveSword();
            return;
        }
        this.clearOverSword(player, itemSword);
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event){
        if(event.getItem().getType() == Material.POTION){
            PlayerInventory inventory = event.getPlayer().getInventory();
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> inventory.setItemInHand(null), 1L);
        }
    }

    private void clearOverSword(Player player, ItemStack itemSword){
        if(InventoryUtils.hasItem(player, itemSword) && containSword(player, itemSword)) {
            player.getInventory().remove(itemSword);
        }
    }

    private boolean containSword(Player player){
        return containSword(player, null);
    }

    private boolean containSword(Player player, ItemStack ignored){
        List<Material> items = new ArrayList<>(Arrays.asList(InventoryUtils.getSwords()));
        items.removeIf(material -> ignored != null && material == ignored.getType());
        return items.stream().anyMatch(material -> player.getInventory().contains(material));
    }

    private BWGamePlayer getGamePlayer(Player player) {
        return this.plugin.getGame().getPlayer(player);
    }
}
