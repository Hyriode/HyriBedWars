package fr.hyriode.bedwars.game.npc.inventory.shop.material.upgradable;

import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.bedwars.game.BWGameOre;
import fr.hyriode.bedwars.game.npc.inventory.EHyriBWShopNavBar;
import fr.hyriode.bedwars.game.npc.inventory.shop.*;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

public class ItemBWPickaxe implements ItemShopUpgradable{

    private int tier = 0;

    @Override
    public ItemShop getTierItem(int tier){
        switch (tier){
            case 0:
                return new ItemShop(new ItemBuilder(Material.WOOD_PICKAXE).withEnchant(Enchantment.DIG_SPEED).unbreakable().withItemFlags(ItemFlag.HIDE_UNBREAKABLE).build(), this.getKeyName() + "_tier1", this.getKeyName(),
                        EHyriBWShopNavBar.TOOLS, true, null, new ItemShopStack(BWGameOre.IRON, 10)).setUpgradable(true)
                        .setHyriMaterial(BWMaterial.PICKAXE);
            case 1:
                return new ItemShop(new ItemBuilder(Material.IRON_PICKAXE).withEnchant(Enchantment.DIG_SPEED, 2).unbreakable().withItemFlags(ItemFlag.HIDE_UNBREAKABLE).build(), this.getKeyName() + "_tier2", this.getKeyName(),
                        EHyriBWShopNavBar.TOOLS, false, null, new ItemShopStack(BWGameOre.IRON, 10)).setUpgradable(true)
                        .setHyriMaterial(BWMaterial.PICKAXE);
            case 2:
                return new ItemShop(new ItemBuilder(Material.GOLD_PICKAXE).withEnchant(Enchantment.DIG_SPEED, 3).withEnchant(Enchantment.DAMAGE_ALL, 2).unbreakable().withItemFlags(ItemFlag.HIDE_UNBREAKABLE).build(), this.getKeyName() + "_tier3", this.getKeyName(),
                        EHyriBWShopNavBar.TOOLS, false, null, new ItemShopStack(BWGameOre.GOLD, 3)).setUpgradable(true)
                        .setHyriMaterial(BWMaterial.PICKAXE);
            case 3:
                return new ItemShop(new ItemBuilder(Material.DIAMOND_PICKAXE).withEnchant(Enchantment.DIG_SPEED, 3).unbreakable().withItemFlags(ItemFlag.HIDE_UNBREAKABLE).build(), this.getKeyName() + "_tier4", this.getKeyName(),
                        EHyriBWShopNavBar.TOOLS, false, null, new ItemShopStack(BWGameOre.GOLD, 6)).setUpgradable(true)
                        .setHyriMaterial(BWMaterial.PICKAXE);
            default:
                return null;
        }
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
