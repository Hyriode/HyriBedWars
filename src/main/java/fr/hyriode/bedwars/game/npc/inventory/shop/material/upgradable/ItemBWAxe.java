package fr.hyriode.bedwars.game.npc.inventory.shop.material.upgradable;

import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.bedwars.game.BWGameOre;
import fr.hyriode.bedwars.game.npc.inventory.EHyriBWShopNavBar;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWMaterial;
import fr.hyriode.bedwars.game.npc.inventory.shop.ItemShop;
import fr.hyriode.bedwars.game.npc.inventory.shop.ItemShopStack;
import fr.hyriode.bedwars.game.npc.inventory.shop.ItemShopUpgradable;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

public class ItemBWAxe implements ItemShopUpgradable{

    private int tier = 0;

    @Override
    public ItemShop getTierItem(int tier){
        switch (tier){
            case 0:
                return new ItemShop(new ItemBuilder(Material.WOOD_AXE).withEnchant(Enchantment.DIG_SPEED).unbreakable().withItemFlags(ItemFlag.HIDE_UNBREAKABLE).build(), this.getKeyName() + "_tier1", this.getKeyName(),
                        EHyriBWShopNavBar.TOOLS, true, null, new ItemShopStack(BWGameOre.IRON, 10)).setUpgradable(true)
                        .setHyriMaterial(BWMaterial.AXE);
            case 1:
                return new ItemShop(new ItemBuilder(Material.IRON_AXE).withEnchant(Enchantment.DIG_SPEED).unbreakable().withItemFlags(ItemFlag.HIDE_UNBREAKABLE).build(), this.getKeyName() + "_tier2", this.getKeyName(),
                        EHyriBWShopNavBar.TOOLS, false, null, new ItemShopStack(BWGameOre.IRON, 10)).setUpgradable(true)
                        .setHyriMaterial(BWMaterial.AXE);
            case 2:
                return new ItemShop(new ItemBuilder(Material.GOLD_AXE).withEnchant(Enchantment.DIG_SPEED, 2).unbreakable().withItemFlags(ItemFlag.HIDE_UNBREAKABLE).build(), this.getKeyName() + "_tier3", this.getKeyName(),
                         EHyriBWShopNavBar.TOOLS, false, null, new ItemShopStack(BWGameOre.GOLD, 3)).setUpgradable(true)
                        .setHyriMaterial(BWMaterial.AXE);
            case 3:
                return new ItemShop(new ItemBuilder(Material.DIAMOND_AXE).withEnchant(Enchantment.DIG_SPEED, 3).unbreakable().withItemFlags(ItemFlag.HIDE_UNBREAKABLE).build(), this.getKeyName() + "_tier4", this.getKeyName(),
                        EHyriBWShopNavBar.TOOLS, false, null, new ItemShopStack(BWGameOre.GOLD, 6)).setUpgradable(true)
                        .setHyriMaterial(BWMaterial.AXE);
            default:
                return null;
        }
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
