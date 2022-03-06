package fr.hyriode.bedwars.game.npc.inventory.shop.material.upgradable;

import fr.hyriode.bedwars.game.npc.inventory.shop.material.OreStack;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.bedwars.game.BWGameOre;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopCategory;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.BWMaterial;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.ItemShop;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.ItemShopUpgradable;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

public class ItemBWAxe implements ItemShopUpgradable{

    private int tier = 0;

    @Override
    public ItemShop getTierItem(int tier){
        switch (tier){
            case 0:
                return this.getAxeTier(1, Material.WOOD_AXE, 1, true, new OreStack(BWGameOre.IRON, 10));
            case 1:
                return this.getAxeTier(2, Material.IRON_AXE, 1, false, new OreStack(BWGameOre.IRON, 10));
            case 2:
                return this.getAxeTier(3, Material.GOLD_AXE, 2, false, new OreStack(BWGameOre.GOLD, 3));
            case 3:
                return this.getAxeTier(3, Material.DIAMOND_AXE, 3, false, new OreStack(BWGameOre.GOLD, 6));
            default:
                return null;
        }
    }

    private ItemShop getAxeTier(int tier, Material material, int level, boolean permanent, OreStack price){
        return new ItemShop(this.getKeyName() + "_tier" + tier,
                new ItemBuilder(material).withEnchant(Enchantment.DIG_SPEED, level)
                        .unbreakable().withItemFlags(ItemFlag.HIDE_UNBREAKABLE).build(),
                BWShopCategory.TOOLS, permanent, price)
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
