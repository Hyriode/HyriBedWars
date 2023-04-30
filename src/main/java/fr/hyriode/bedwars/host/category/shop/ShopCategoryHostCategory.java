package fr.hyriode.bedwars.host.category.shop;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.gui.pattern.GuiPattern;
import fr.hyriode.bedwars.game.shop.ShopCategory;
import fr.hyriode.bedwars.game.shop.material.MaterialShop;
import fr.hyriode.bedwars.host.category.BWHostCategory;
import fr.hyriode.bedwars.utils.BWHostUtils;

import java.util.List;

public class ShopCategoryHostCategory extends BWHostCategory {
    public ShopCategoryHostCategory(HyriBedWars plugin, ShopCategory value) {
        super(BWHostUtils.categoryDisplay("shop-category-" + value.getId(), value.getItemStack(true)));

        final GuiPattern.Size size = new GuiPattern.Size(2, 3, 7, 3);
        List<MaterialShop> materialsShop = plugin.getShopManager().getItemShopByCategory(false, value);
        int i = 0;
        for(int y = 0; y < size.getHeight(); ++y){
            for(int x = 0; x < size.getWidth(); ++x){
                if(materialsShop.size() > i) {
                    MaterialShop materialShop = materialsShop.get(i++);

                    this.addSubCategory(slot(size.getStartX() + x, size.getStartY() + y), new ShopItemHostCategory(plugin.getShopManager(), materialShop));
                } else {
                    break;
                }
            }
        }
    }
}
