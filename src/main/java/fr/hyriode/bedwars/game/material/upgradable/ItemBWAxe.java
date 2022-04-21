package fr.hyriode.bedwars.game.material.upgradable;

import fr.hyriode.bedwars.game.material.OreStack;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.bedwars.game.BWGameOre;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopCategory;
import fr.hyriode.bedwars.game.material.BWMaterial;
import fr.hyriode.bedwars.game.material.ItemShop;
import fr.hyriode.bedwars.game.material.ItemShopUpgradable;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

public class ItemBWAxe implements ItemShopUpgradable{

    private int tier = 0;

    private static ItemShop tier1;
    private static ItemShop tier2;
    private static ItemShop tier3;
    private static ItemShop tier4;

    public void init(){
        tier1 = this.getAxeTier(1, Material.WOOD_AXE, 1, new OreStack(BWGameOre.IRON, 10));
        tier2 = this.getAxeTier(2, Material.IRON_AXE, 1, new OreStack(BWGameOre.IRON, 10));
        tier3 = this.getAxeTier(3, Material.GOLD_AXE, 2, new OreStack(BWGameOre.GOLD, 3));
        tier4 = this.getAxeTier(3, Material.DIAMOND_AXE, 3, new OreStack(BWGameOre.GOLD, 6));
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

    private ItemShop getAxeTier(int tier, Material material, int level, OreStack price){
        return new ItemShop(this.getKeyName() + "_tier" + tier,
                new ItemBuilder(material).withEnchant(Enchantment.DIG_SPEED, level)
                        .unbreakable().withItemFlags(ItemFlag.HIDE_UNBREAKABLE).build(),
                BWShopCategory.TOOLS, true, price)
                .setUpgradable(true).setHyriMaterial(BWMaterial.AXE);
    }

    @Override
    public String getKeyName(){
        return "axe";
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
        return BWMaterial.AXE;
    }


}
