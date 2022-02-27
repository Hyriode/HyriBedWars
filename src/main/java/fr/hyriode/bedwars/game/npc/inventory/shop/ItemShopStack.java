package fr.hyriode.bedwars.game.npc.inventory.shop;

import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.upgradable.ArmorBW;
import fr.hyriode.bedwars.utils.InventoryBWUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemShopStack {

    private final ItemShop item;
    private final int amount;

    public ItemShopStack(ItemShop item, int amount){
        this.item = item;
        this.amount = amount;
    }

    public ItemShopStack(ItemShop item){
        this(item, 1);
    }

    public int getAmount() {
        return amount;
    }

    public ItemShop getItem() {
        return item;
    }

    public ItemStack getItemStack(){
        ItemStack itemStack = new ItemStack(item.getMaterial());
        itemStack.setAmount(amount);
        return itemStack;
    }

    public ItemStack getItemShop(BWGamePlayer hyriPlayer){
        Player player = hyriPlayer.getPlayer();
        List<String> lore = new ArrayList<>();

        ItemShop itemShop =
                this.item.isUpgradable() ?
                    hyriPlayer.hasUpgradeMaterial(this.item.getHyriMaterial()) ?
                            hyriPlayer.getItemUpgradable(this.getItem().getHyriMaterial()).isMaxed() ? this.item.getHyriMaterial().getItemUpgradable().getTierItem(this.item.getHyriMaterial().getItemUpgradable().getMaxTier()) :
                        this.item.getHyriMaterial().getItemUpgradable().getNextTierItem()
                            :
                        this.item.getHyriMaterial().getItemUpgradable().getTierItem(0)
                : this.item;

        boolean isMaxed = itemShop.isUpgradable() && hyriPlayer.hasUpgradeMaterial(itemShop.getHyriMaterial()) && hyriPlayer.getItemUpgradable(itemShop.getHyriMaterial()).isMaxed();

        if(!isMaxed)
            lore.add(ChatColor.GRAY + "Cost: " + itemShop.getCountPriceAsString(player));

        if(this.item.isUpgradable()) {
            int tier = !hyriPlayer.hasUpgradeMaterial(this.item.getHyriMaterial()) ? 0 : hyriPlayer.getItemUpgradable(this.item.getHyriMaterial()).getNextTier();
            lore.add(ChatColor.GRAY + "Tier: " + ChatColor.YELLOW + InventoryBWUtils.getTierString(player, (isMaxed ? hyriPlayer.getItemUpgradable(this.item.getHyriMaterial()).getMaxTier() + 2 : tier + 1)));
        }

        lore.add(ChatColor.GRAY + " ");

        if(this.item.getDescription() != null) {
            String[] description = this.item.getDescription().getForPlayer(player).split("\n");
            for(String desc : description){
                lore.add(ChatColor.GRAY + desc);
            }
            lore.add(ChatColor.GRAY + " ");
        }

        if(itemShop instanceof ArmorBW)
            if(hyriPlayer.getPermanentArmor() != null && ((ArmorBW) hyriPlayer.getPermanentArmor().getItemShop().getItem()).getLevel() >= ((ArmorBW)itemShop).getLevel()){
                lore.add(ChatColor.GREEN + "UNLOCKED !");
                lore.add(" ");
            }

        boolean hasItems = InventoryBWUtils.hasItems(player, itemShop.getPrice());

        if(isMaxed)
            lore.add(ChatColor.GREEN + "MAXED !");
        else {
            lore.add((hasItems ? ChatColor.YELLOW + HyriBedWars.getLanguageManager().getValue(player, "inv.shop.click.purchase") : ChatColor.RED + HyriBedWars.getLanguageManager().getValue(player, "inv.shop.enough.item") + " " + this.item.getPriceAsString(player)));
        }

        return new ItemBuilder(this.getItemStack())
                .withName((hasItems ? ChatColor.GREEN : ChatColor.RED) + itemShop.getName().getForPlayer(player))
                .withAllItemFlags().withLore(lore).build();
    }

}
