package fr.hyriode.bedwars.game.shop.material.upgrade;

import fr.hyriode.bedwars.game.shop.ItemShop;
import fr.hyriode.bedwars.game.shop.material.MaterialShop;

public class UpgradeMaterial {

    private final MaterialShop material;
    private int tier;

    public UpgradeMaterial(MaterialShop material){
        this.material = material;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public void addTier(){
        if(!this.isMaxed())
            ++this.tier;
    }

    public void removeTier(){
        if(this.tier > 0)
            --this.tier;
    }

    public MaterialShop getMaterial() {
        return material;
    }

    public int getTier() {
        return tier;
    }

    public int getNextTier(){
        return this.tier + 1;
    }

    public boolean isMaxed() {
        return this.material.getMaxTier() <= this.tier;
    }

    public ItemShop getItemShopByNextTier(){
        return this.material.getItem(this.getNextTier());
    }

    public ItemShop getItemShopByTier() {
        return this.material.getItem(this.tier);
    }
}
