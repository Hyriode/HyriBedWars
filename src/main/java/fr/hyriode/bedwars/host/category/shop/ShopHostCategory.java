package fr.hyriode.bedwars.host.category.shop;

import fr.hyriode.bedwars.game.shop.ShopCategory;
import fr.hyriode.bedwars.host.BWShopValues;
import fr.hyriode.bedwars.host.category.BWHostCategory;
import fr.hyriode.bedwars.option.PercentageOption;
import fr.hyriode.bedwars.utils.BWHostUtils;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ShopHostCategory extends BWHostCategory {

    public ShopHostCategory() {
        super(BWHostUtils.categoryDisplay("shop", Material.WOOL));

        int i = 2;
        for (ShopCategory value : Arrays.stream(ShopCategory.values()).filter(category -> category != ShopCategory.QUICK_BUY).collect(Collectors.toList())) {
            this.addSubCategory(slot(i++, 3), new ShopCategoryHostCategory(value));
        }

        this.addOption(slot(5, 4), new PercentageOption(BWHostUtils.optionDisplay("shop-prices", Material.GOLD_INGOT), BWShopValues.PRICE_GLOBAL.getDefaultValue(), 50, 1000, 50));
    }

}
