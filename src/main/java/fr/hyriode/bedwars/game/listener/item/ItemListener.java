package fr.hyriode.bedwars.game.listener.item;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.shop.ItemMoney;
import fr.hyriode.bedwars.utils.InventoryUtils;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.hyrame.game.HyriGameState;
import fr.hyriode.hyrame.listener.HyriListener;
import fr.hyriode.hyrame.utils.ItemUtil;
import fr.hyriode.hyrame.utils.PlayerUtil;
import fr.hyriode.hyrame.utils.block.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemListener extends HyriListener<HyriBedWars> {

    public ItemListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        if(this.plugin.getGame().getState() != HyriGameState.PLAYING) return;
        Player player = event.getPlayer();
        BWGamePlayer bwPlayer = this.getGamePlayer(player);
        Item itemPickup = event.getItem();
        ItemStack itemStackPickup = itemPickup.getItemStack().clone();

        //TODO ignore condition for host

        if(itemStackPickup.getType() == Material.WOOD_SWORD) {
            event.setCancelled(true);
            return;
        }

        if(ItemUtil.isSword(itemStackPickup) && player.getInventory().contains(bwPlayer.getSword().getType())) {
            player.getInventory().remove(bwPlayer.getSword().getType());
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(this.plugin.getGame().getState() != HyriGameState.PLAYING) return;
        Player player = event.getPlayer();
        Item itemDropped = event.getItemDrop();
        ItemStack itemStackDropped = itemDropped.getItemStack();

        if(MetadataReferences.isPermanent(itemStackDropped)) {
            itemDropped.remove();
            InventoryUtils.giveInSlot(player, 0, itemStackDropped);
            return;
        }

        if(!containSword(player)) {
            this.getGamePlayer(player).giveSword();
        }

        if(ItemMoney.contains(itemStackDropped)) {
            Location loc = player.getLocation();
            Cuboid cuboid = new Cuboid(loc.clone().subtract(1, 2, 1), loc.clone().add(1, 1, 1));

            for (Block block : cuboid) {
                if (block.getType() != Material.AIR) {
                    return;
                }
            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteractInventory(InventoryClickEvent event) {
        if(this.plugin.getGame().getState() != HyriGameState.PLAYING) return;
        Player player = (Player) event.getWhoClicked();
        BWGamePlayer bwPlayer = this.plugin.getGame().getPlayer(player);
        ItemStack itemSword = bwPlayer.getSword();
        if(bwPlayer.isSpectator() || bwPlayer.isDead()) return;

        if(event.getSlotType() == InventoryType.SlotType.CRAFTING || event.getSlotType() == InventoryType.SlotType.ARMOR){
            event.setCancelled(true);
            return;
        }

        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.clearOverSword(player, itemSword.getType()), 2L);

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

        if(event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            if(MetadataReferences.isPermanent(event.getCurrentItem())){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if(this.plugin.getGame().getState() != HyriGameState.PLAYING) return;
        BWGamePlayer bwPlayer = this.getGamePlayer((Player) event.getWhoClicked());
        if(bwPlayer.isSpectator() || bwPlayer.isDead()) return;
        int slot = InventoryUtils.getFirstElementOfInt(event.getRawSlots());

        if(event.getInventory().getType() == InventoryType.CRAFTING) {
            if(slot >= 1 && slot <= 8) {
                event.setCancelled(true);
            }
            return;
        }

        if(event.getOldCursor() != null && MetadataReferences.isPermanent(event.getOldCursor())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event) {
        if(this.plugin.getGame().getState() != HyriGameState.PLAYING) return;
        Player player = (Player) event.getPlayer();
        BWGamePlayer bwPlayer = this.getGamePlayer(player);
        if(bwPlayer.isSpectator() || bwPlayer.isDead()) return;

        if(!containSword(player)){
            bwPlayer.giveSword();
        }
        this.clearOverSword(player, Material.WOOD_SWORD);
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        if(this.plugin.getGame().getState() != HyriGameState.PLAYING) return;
        if(item.getType() == Material.POTION) {
            Player player = event.getPlayer();
            PlayerInventory inventory = player.getInventory();
            System.out.println("test");

            Bukkit.getScheduler().runTaskLater(this.plugin, () -> inventory.setItemInHand(null), 1L);
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta instanceof PotionMeta && ((PotionMeta) itemMeta).getCustomEffects().get(0).getType().getName().equals(PotionEffectType.INVISIBILITY.getName())) {
                System.out.println("uiui");
                BWGamePlayer bwPlayer = this.getGamePlayer(player);
                System.out.println("hide");
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        bwPlayer.hideArmor();
                        if(!player.hasPotionEffect(PotionEffectType.INVISIBILITY)){
                            bwPlayer.showArmor();
                            this.cancel();
                        }
                    }
                }.runTaskTimer(this.plugin, 0L, 1L);
            }
        }
    }

    private void clearOverSword(Player player, Material itemSword) {
        if(InventoryUtils.hasMaterial(player, itemSword) && containSword(player, itemSword)) {
            player.getInventory().remove(itemSword);
        }
    }

    private boolean containSword(Player player) {
        return containSword(player, null);
    }

    private boolean containSword(Player player, Material ignored) {
        List<Material> items = new ArrayList<>(Arrays.asList(InventoryUtils.getSwords()));
        items.removeIf(material -> ignored != null && material == ignored);
        return items.stream().anyMatch(material -> player.getInventory().contains(material));
    }

    private BWGamePlayer getGamePlayer(Player player) {
        return this.plugin.getGame().getPlayer(player);
    }
}
