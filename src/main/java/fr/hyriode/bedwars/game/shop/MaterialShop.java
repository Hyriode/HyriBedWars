package fr.hyriode.bedwars.game.shop;

import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.utils.InventoryUtils;
import fr.hyriode.bedwars.utils.TriConsumer;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MaterialShop {

    private final List<ItemShop> items;
    private final String name;
    private final ShopCategory category;
    private final TriConsumer<BWGamePlayer, ItemStack, Integer> action;
    private boolean permanent;
    private boolean disable;

    public MaterialShop(String name, ShopCategory category, boolean permanent, List<ItemShop> itemShops) {
        this(name, category, permanent, (player, itemStack, slot) -> {
            Bukkit.getScheduler().runTaskLater(player.getPlugin(), () -> {
                if(slot == -1) {
                    InventoryUtils.giveInSlot(player.getPlayer(), 0, itemStack);
                    return;
                }
                InventoryUtils.setInSlot(player.getPlayer(), slot, itemStack);
            }, 1L);
        }, itemShops);
    }

    public MaterialShop(String name, ShopCategory category, boolean permanent, TriConsumer<BWGamePlayer, ItemStack, Integer> action, List<ItemShop> itemShops) {
        this.name = name;
        this.category = category;
        this.permanent = permanent;
        this.items = itemShops;
        this.action = action;

        if(!this.items.isEmpty()) {
            for (int tier = 0; tier < this.items.size(); ++tier) {
                this.items.get(tier).setName(name + (this.items.size() > 1 ? "_tier_" + (tier + 1) : ""))
                        .setMaterialName(name)
                        .setCategory(category);
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

    public void buy(InventoryClickEvent event, BWGamePlayer player) {
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

        int hotbar = player.getAccount().getSlotByHotbar(player.getPlayer(), itemStack, this.getCategory().getHotbar());
        int slot = event.getHotbarButton() < 0
                ? hotbar
                : event.getHotbarButton();

        this.action.accept(player, itemStack, slot);
        if(this.getCategory() == ShopCategory.MELEE){
            player.applySharpness();
        }

    }

    /**
     * Get item shop for the player to buy
     *
     * @param player The player
     * @return ItemShop to give to the player
     */
    public ItemShop getItemShopForPlayer(BWGamePlayer player) {
        if (this.isUpgradable() && player.containsMaterialUpgrade(this)) {
            UpgradeMaterial m = player.getMaterialUpgrade(this);
            return m.getItemShopByNextTier();
        }
        return this.getFirstItem();
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
