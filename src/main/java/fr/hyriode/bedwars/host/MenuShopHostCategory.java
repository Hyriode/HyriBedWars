package fr.hyriode.bedwars.host;

import fr.hyriode.bedwars.host.category.BWHostCategory;
import fr.hyriode.bedwars.host.category.base.TrapHostCategory;
import fr.hyriode.bedwars.host.category.base.UpgradeHostCategory;
import fr.hyriode.bedwars.host.category.shop.ShopHostCategory;
import fr.hyriode.bedwars.host.option.AnimationBooleanOption;
import fr.hyriode.bedwars.utils.BWHostUtils;
import fr.hyriode.bedwars.utils.ItemPotionBuilder;
import fr.hyriode.hyrame.host.option.BooleanOption;
import fr.hyriode.hyrame.host.option.PreciseIntegerOption;
import org.bukkit.Material;
import org.bukkit.potion.PotionType;

public class MenuShopHostCategory extends BWHostCategory {
    public MenuShopHostCategory() {
        super(BWHostUtils.categoryDisplay("shop-menu", Material.EMERALD));

        this.addSubCategory(slot(4, 3), new ShopHostCategory());
        this.addSubCategory(slot(5, 3), new UpgradeHostCategory());
        this.addSubCategory(slot(6, 3), new TrapHostCategory());
        this.addOption(slot(5, 4), new AnimationBooleanOption(BWHostUtils.optionDisplay("shop-potion-enabled", Material.POTION), true, Material.POTION, Material.GLASS_BOTTLE));
        this.addOption(slot(6, 4), new PreciseIntegerOption(BWHostUtils.optionDisplay("cooldown-fireball", Material.FIREBALL), 10, 0, 100, new int[] {1, 10, 20}));
    }
}
