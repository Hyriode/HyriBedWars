package fr.hyriode.bedwars.game.shop;

import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.BiConsumer;

public class MaterialShop {

    private final List<ItemShop> items;
    private final String name;
    private final ShopCategory category;
    private final BiConsumer<BWGamePlayer, ItemStack> action;
    private boolean permanent;
    private boolean disable;

    public MaterialShop(String name, ShopCategory category, boolean permanent, List<ItemShop> itemShops) {
        this(name, category, permanent, (player, itemStack) -> {
            Bukkit.getScheduler().runTaskLater(player.getPlugin(), () -> InventoryUtils.giveInSlot(player.getPlayer(), 0 /*Faire selon la hotbar click/manager*/, itemStack), 1L);
        }, itemShops);
    }

    public MaterialShop(String name, ShopCategory category, boolean permanent, BiConsumer<BWGamePlayer, ItemStack> action, List<ItemShop> itemShops) {
        this.name = name;
        this.category = category;
        this.permanent = permanent;
        this.items = itemShops;
        this.action = action;

        if(!this.items.isEmpty()) {
            if (this.items.size() > 1) {
                for (int tier = 0; tier < this.items.size(); ++tier) {
                    this.items.get(tier).setName(name + "_tier_" + (tier + 1)).setCategory(category);
                }
            } else {
                this.items.get(0).setName(name).setCategory(category);
            }
        }
    }

    public List<ItemShop> getItems() {
        return items;
    }

    public String getName() {
        return name;
    }

    public ShopCategory getCategory() {
        return category;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public boolean isDisable() {
        return disable;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public boolean isUpgradable(){
        return this.items.size() > 1;
    }

    public boolean isArmor(){
        return this instanceof MaterialArmorShop;
    }

    public MaterialArmorShop getAsArmor(){
        return (MaterialArmorShop) this;
    }

    public ItemShop getFirstItem(){
        return this.items.get(0);
    }

    public ItemShop getItem(int i){
        return this.items.get(this.items.size() >= i + 1 ? i : this.items.size() - 1);
    }

    public boolean isFake() {
        return this instanceof FakeMaterialShop;
    }

    public int getMaxTier(){
        return this.items.size() - 1;
    }

    public void buy(BWGamePlayer player) {
        ItemStack itemStack = null;

        if(this.isUpgradable()) {
            UpgradeMaterial upgradeMaterial = player.getMaterialUpgrade(this);
            if (!player.containsMaterialUpgrade(this)) {
                upgradeMaterial = player.addUpgradeMaterial(this);
                itemStack = upgradeMaterial.getItemShopByTier().getItemStack(player);
            } else {
                ItemStack origin = upgradeMaterial.getItemShopByTier().getItemStack(player).clone();
                upgradeMaterial.addTier();
                InventoryUtils.replace(player.getPlayer(), origin, upgradeMaterial.getItemShopByTier().getItemStack(player));
                return;
            }
        } else {
            player.addItemPermanent(this);
        }

        if(itemStack == null){
            itemStack = this.getFirstItem().getItemStack(player);
        }

        this.action.accept(player, itemStack);
    }

    @Override
    public String toString() {
        return "MaterialShop{" +
                "name='" + name + '\'' +
                ", category=" + category +
                ", permanent=" + permanent +
                ", disable=" + disable +
                '}';
    }
}
