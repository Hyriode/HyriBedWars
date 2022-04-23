package fr.hyriode.bedwars.game.listener;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.material.BWMaterial;
import fr.hyriode.bedwars.utils.InventoryBWUtils;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.bedwars.utils.Utils;
import fr.hyriode.hyrame.game.HyriGameState;
import fr.hyriode.hyrame.listener.HyriListener;
import fr.hyriode.hyrame.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryPlayer;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BWSwordListener extends HyriListener<HyriBedWars> {

    public BWSwordListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();

        if(event.getClickedInventory() != null && event.getView() != null && event.getView().getTopInventory() != null && event.getClickedInventory().equals(event.getView().getTopInventory()) && (MetadataReferences.isPermanent(event.getCurrentItem()) || MetadataReferences.isPermanent(event.getCursor()))) {
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

        if(event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY){
            if(MetadataReferences.isPermanent(event.getCurrentItem())){
                event.setCancelled(true);
                return;
            }
        }

        if(!(event.getClickedInventory() instanceof CraftInventoryPlayer) && MetadataReferences.isPermanent(event.getCursor())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event){
        int slot = Utils.getFirstElementOfInt(event.getInventorySlots());
        if(event.getOldCursor() != null && MetadataReferences.isPermanent(event.getOldCursor())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        e.setCancelled(manageDrop(e.getPlayer(), e.getItemDrop()));
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        e.setCancelled(managePickup(e.getItem(), e.getPlayer()));
    }

    /**
     * Remove the default swords/ bow if the picked item is more powerful.
     *
     * @return true to cancel the event
     */
    public boolean managePickup(Item item, Player player) {
        if(this.plugin.getGame().getState() == HyriGameState.WAITING || this.plugin.getGame().getState() == HyriGameState.READY) return true;
        if (ItemUtil.isSword(item.getItemStack())) {
            if(InventoryBWUtils.hasItem(player, new ItemStack(Material.WOOD_SWORD)))
                return true;
            for (ItemStack is : player.getInventory()) {
                if (is == null) continue;
                if (is.getType() == Material.AIR) continue;

                if (isSword(item.getItemStack())) {
//                    player.getInventory().remove(is);
//                    player.updateInventory();
                    return false;
                }
            }
//            player.updateInventory();
            return true;
        }
        return false;
    }

    /**
     * If the dropped sword/ bow is a default item and is more powerful
     * than the others in the inventory give it back.
     * <p>
     * If the player remains without a sword give it the swords from the default items.
     * If the player remains without a bow give it bows from the default items.
     *
     * @return true to cancel the event.
     */
    private boolean manageDrop(Player player, Item item) {
        if(this.plugin.getGame().getState() == HyriGameState.WAITING || this.plugin.getGame().getState() == HyriGameState.READY) return true;
        if(item.getItemStack().getType() == Material.WOOD_SWORD) {
            return true;
        }
        if(MetadataReferences.isPermanent(item.getItemStack()) && !ItemUtil.isSword(item.getItemStack())){
            return true;
        }

        boolean sword = false;
        for (ItemStack is : player.getInventory()) {
            if (is == null) continue;
            if (ItemUtil.isSword(is)) {
                sword = true;
                break;
            }
        }
        if (!sword) this.plugin.getGame().getPlayer(player).giveSword();
        return false;
    }

    /**
     * If the player moves a default sword or bow into another inventory
     * and he remains with a less powerful weapon restore the lost one.
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if(this.plugin.getGame().getState() == HyriGameState.WAITING || this.plugin.getGame().getState() == HyriGameState.READY) return;
        if(this.plugin.getGame().getState() != HyriGameState.PLAYING) return;
        BWGamePlayer player = this.plugin.getGame().getPlayer(e.getPlayer().getUniqueId());
        if(player.isDead() || player.isSpectator()) return;
        if (e.getInventory().getType() == InventoryType.PLAYER) return;

        boolean sword = false;
        boolean defaultSword = false;
        for (ItemStack is : e.getPlayer().getInventory()) {
            if (is == null) continue;
            if (is.getType() == Material.AIR) continue;
            if (isSword(is)) sword = true;
            if (is.getType() == Material.WOOD_SWORD) defaultSword = true;
        }

        if(e.getPlayer().getItemOnCursor().getType() == Material.WOOD_SWORD && (sword || defaultSword)){
            e.getPlayer().setItemOnCursor(null);
            return;
        }

        if(defaultSword && sword){
            e.getPlayer().getInventory().remove(Material.WOOD_SWORD);
            return;
        }

        if (!sword && !defaultSword && e.getPlayer().getItemOnCursor() != null && e.getPlayer().getItemOnCursor().getType() != Material.WOOD_SWORD) {
            this.plugin.getGame().getPlayer((Player) e.getPlayer()).giveSword();
        }
    }

    private static boolean isSword(ItemStack itemStack){
        return itemStack.getType().equals(Material.STONE_SWORD) || itemStack.getType().equals(Material.GOLD_SWORD) || itemStack.getType().equals(Material.IRON_SWORD) || itemStack.getType().equals(Material.GOLD_SWORD) || itemStack.getType().equals(Material.DIAMOND_SWORD);
    }
}
