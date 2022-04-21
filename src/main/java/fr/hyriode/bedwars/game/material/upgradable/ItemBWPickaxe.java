package fr.hyriode.bedwars.game.material.upgradable;

import fr.hyriode.bedwars.game.material.BWMaterial;
import fr.hyriode.bedwars.game.material.ItemShop;
import fr.hyriode.bedwars.game.material.ItemShopUpgradable;
import fr.hyriode.bedwars.game.material.OreStack;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.bedwars.game.BWGameOre;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopCategory;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

public class ItemBWPickaxe implements ItemShopUpgradable {

    private int tier = 0;

    private static ItemShop tier1;
    private static ItemShop tier2;
    private static ItemShop tier3;
    private static ItemShop tier4;

    public void init(){
        tier1 = this.getPickaxeTier(1, new ItemBuilder(Material.WOOD_PICKAXE).withEnchant(Enchantment.DIG_SPEED), new OreStack(BWGameOre.IRON, 10));
        tier2 = this.getPickaxeTier(2, new ItemBuilder(Material.IRON_PICKAXE).withEnchant(Enchantment.DIG_SPEED, 2), new OreStack(BWGameOre.IRON, 10));
        tier3 = this.getPickaxeTier(3, new ItemBuilder(Material.GOLD_PICKAXE).withEnchant(Enchantment.DIG_SPEED, 3).withEnchant(Enchantment.DAMAGE_ALL, 2), new OreStack(BWGameOre.GOLD, 3));
        tier4 = this.getPickaxeTier(4, new ItemBuilder(Material.DIAMOND_PICKAXE).withEnchant(Enchantment.DIG_SPEED, 3), new OreStack(BWGameOre.GOLD, 6));
    }

    @Override
    public ItemShop getTierItem(int tier){
        switch (tier){
            case 0:
                return tier1;
            case 1:
                return tier2;
            case 2:
                return tier3;
            case 3:
                return tier4;
            default:
                return null;
        }
    }

    private ItemShop getPickaxeTier(int tier, ItemBuilder item, OreStack price){
        return new ItemShop(this.getKeyName() + "_tier" + tier, item.unbreakable().withItemFlags(ItemFlag.HIDE_UNBREAKABLE).build(),
                BWShopCategory.TOOLS, true, price).setUpgradable(true).setHyriMaterial(BWMaterial.PICKAXE);
    }

    @Override
    public String getKeyName(){
        return "pickaxe";
    }

    @Override
    public int getTier() {
        return this.tier;
    }

    @Override
    public void setTier(int tier) {
        this.tier = tier;
    }

    @Override
    public int getMaxTier() {
        return 3;
    }

    @Override
    public BWMaterial getHyriMaterial() {
        return BWMaterial.PICKAXE;
    }

}
