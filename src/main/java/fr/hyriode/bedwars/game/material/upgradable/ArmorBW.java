package fr.hyriode.bedwars.game.material.upgradable;

import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopCategory;
import fr.hyriode.bedwars.game.material.BWMaterial;
import fr.hyriode.bedwars.game.material.ItemShop;
import fr.hyriode.bedwars.game.material.OreStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ArmorBW extends ItemShop {

    private final int level;

    private Material leggings;
    private Material boots;

    public ArmorBW(int level, Material material, String keyName, OreStack... price) {
        super(keyName, new ItemStack(material), BWShopCategory.ARMOR, true, price);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public ArmorBW setLeggings(Material leggings) {
        this.leggings = leggings;
        return this;
    }

    public ArmorBW setBoots(Material boots) {
        this.boots = boots;
        return this;
    }

    public Material getLeggings() {
        return this.leggings;
    }

    public Material getBoots() {
        return this.boots;
    }

    public ArmorBW setHyriMaterial(BWMaterial material){
        return (ArmorBW) super.setHyriMaterial(material);
    }
}
