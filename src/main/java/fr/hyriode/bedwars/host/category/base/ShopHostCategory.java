package fr.hyriode.bedwars.host.category.base;

import fr.hyriode.bedwars.game.shop.ShopCategory;
import fr.hyriode.bedwars.host.category.BWHostCategory;
import fr.hyriode.bedwars.utils.BWHostUtils;
import org.bukkit.Material;

import static fr.hyriode.bedwars.game.shop.ShopCategory.*;

public class ShopHostCategory extends BWHostCategory {

    private final ShopCategory category;

    public ShopHostCategory(ShopCategory category) {
        super(BWHostUtils.categoryDisplay("shop", category.getItemStack(true)));
        this.category = category;

//        this.setNavbar();
    }

    private void setNavbar(){
        this.setSlotNavItem(0, ARMOR);
        this.setSlotNavItem(1, BLOCKS);
        this.setSlotNavItem(2, MELEE);

        this.setSlotNavItem(6, POTIONS);
        this.setSlotNavItem(7, UTILITY);
        this.setSlotNavItem(8, TOOLS);
    }

    private void setSlotNavItem(int slot, ShopCategory category){
        this.addSubCategory(slot, new ShopHostCategory(category));
    }
}
