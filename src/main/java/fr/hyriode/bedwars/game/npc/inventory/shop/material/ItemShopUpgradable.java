package fr.hyriode.bedwars.game.npc.inventory.shop.material;

public interface ItemShopUpgradable {

    ItemShop getTierItem(int tier);

    String getKeyName();

    int getTier();

    void setTier(int tier);

    default ItemShop getTierItem(){
        return this.getTierItem(this.getTier());
    }

    default void updateNextTier(){
        this.setTier(Math.min(this.getTier() + 1, this.getMaxTier()));
    }

    int getMaxTier();

    BWMaterial getHyriMaterial();

    default ItemShop getNextTierItem(int tier) {
        return tier + 1 < this.getMaxTier() ? this.getTierItem(tier + 1) : this.getTierItem(this.getMaxTier());
    }

    default ItemShop getNextTierItem() {
        return this.getTier() + 1 < this.getMaxTier() ? this.getTierItem(this.getTier() + 1) : this.getTierItem(this.getMaxTier());
    }

    default int getNextTier(){
        return Math.min(this.getTier() + 1, this.getMaxTier());
    }

    default boolean isMaxed(){
        return this.getTier() >= this.getMaxTier();
    }

    default ItemShop getPreviousTierItem(){
        return this.getTier() - 1 < 0 ? this.getTierItem(0) : this.getTierItem(this.getTier() - 1);
    }

    default int getPreviousItem(){
        return Math.max(this.getTier() - 1, 0);
    }

    default void downUpgrade(){
        this.setTier(this.getPreviousItem());
    }

}
