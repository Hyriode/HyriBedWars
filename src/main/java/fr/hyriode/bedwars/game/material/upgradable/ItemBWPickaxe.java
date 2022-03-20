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

    @Override
    public ItemShop getTierItem(int tier){
        switch (tier){
            case 0:
                return this.getPickaxeTier(1, new ItemBuilder(Material.WOOD_PICKAXE).withEnchant(Enchantment.DIG_SPEED), true, new OreStack(BWGameOre.IRON, 10));
            case 1:
                return this.getPickaxeTier(2, new ItemBuilder(Material.IRON_PICKAXE).withEnchant(Enchantment.DIG_SPEED, 2), false, new OreStack(BWGameOre.IRON, 10));
            case 2:
                return this.getPickaxeTier(3, new ItemBuilder(Material.GOLD_PICKAXE).withEnchant(Enchantment.DIG_SPEED, 3).withEnchant(Enchantment.DAMAGE_ALL, 2), false, new OreStack(BWGameOre.GOLD, 3));
            case 3:
                return this.getPickaxeTier(4, new ItemBuilder(Material.DIAMOND_PICKAXE).withEnchant(Enchantment.DIG_SPEED, 3), false, new OreStack(BWGameOre.GOLD, 6));
            default:
                return null;
        }
    }

    private ItemShop getPickaxeTier(int tier, ItemBuilder item, boolean permanent, OreStack price){
        return new ItemShop(this.getKeyName() + "_tier" + tier, item.unbreakable().withItemFlags(ItemFlag.HIDE_UNBREAKABLE).build(),
                BWShopCategory.TOOLS, permanent, price)
                .setUpgradable(true).setHyriMaterial(BWMaterial.PICKAXE);
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
