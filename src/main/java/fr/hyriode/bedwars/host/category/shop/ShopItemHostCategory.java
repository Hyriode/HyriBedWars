package fr.hyriode.bedwars.host.category.shop;

import fr.hyriode.bedwars.game.shop.ItemShop;
import fr.hyriode.bedwars.game.shop.material.MaterialShop;
import fr.hyriode.bedwars.host.category.BWHostCategory;
import fr.hyriode.bedwars.host.option.BetterIntegerOption;
import fr.hyriode.bedwars.utils.BWHostUtils;
import fr.hyriode.hyrame.host.option.BooleanOption;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.Material;

public class ShopItemHostCategory extends BWHostCategory {
    public ShopItemHostCategory(MaterialShop materialShop) {
        super(BWHostUtils.categoryDisplay("shop-category-" + materialShop.getCategory().getId() + "-" + materialShop.getName(), new ItemBuilder(materialShop.getFirstItem().getItem()).withAllItemFlags().build()));

        int slotEnable = slot(5, 3);
        if(materialShop.getItems().size() > 1) {
            slotEnable = slot(3, 3);
            int i = 5;
            for (int j = 0; j < materialShop.getItems().size(); j++) {
                ItemShop item = materialShop.getItems().get(j);
                this.addOption(slot(i++, 3), new BetterIntegerOption(BWHostUtils.optionDisplay("shop-item-" + materialShop.getName() + "-" + j + "-price", item.getItem()), item.getPrice().getAmount().getDefaultValue(), 0, 1000, 10));
            }
        } else {
            this.addOption(slot(5, 4), new BetterIntegerOption(BWHostUtils.optionDisplay("shop-item-" + materialShop.getName() + "-price", Material.GOLD_INGOT), materialShop.getFirstItem().getPrice().getAmount().getDefaultValue(), 0, 1000, 10));
        }
        this.addOption(slotEnable, new BooleanOption(BWHostUtils.optionDisplay("shop-item-" + materialShop.getName() + "-enabled", Material.LEVER), materialShop.isEnable().getDefaultValue()));
    }
}
