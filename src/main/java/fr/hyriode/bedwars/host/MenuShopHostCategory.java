package fr.hyriode.bedwars.host;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.host.category.BWHostCategory;
import fr.hyriode.bedwars.host.category.base.TrapHostCategory;
import fr.hyriode.bedwars.host.category.base.UpgradeHostCategory;
import fr.hyriode.bedwars.host.category.shop.ShopHostCategory;
import fr.hyriode.bedwars.host.option.AnimationBooleanOption;
import fr.hyriode.bedwars.utils.BWHostUtils;
import fr.hyriode.hyrame.host.option.PreciseIntegerOption;
import org.bukkit.Material;

public class MenuShopHostCategory extends BWHostCategory {
    public MenuShopHostCategory(HyriBedWars plugin) {
        super(BWHostUtils.categoryDisplay("shop-menu", Material.EMERALD));

        this.addSubCategory(slot(4, 3), new ShopHostCategory(plugin));
        this.addSubCategory(slot(5, 3), new UpgradeHostCategory(plugin));
        this.addSubCategory(slot(6, 3), new TrapHostCategory(plugin));
        this.addOption(slot(5, 4), new AnimationBooleanOption(BWHostUtils.optionDisplay("shop-potion-enabled", Material.POTION), true, Material.POTION, Material.GLASS_BOTTLE));
        this.addOption(slot(6, 4), new PreciseIntegerOption(BWHostUtils.optionDisplay("cooldown-fireball", Material.FIREBALL), 10, 0, 100, new int[] {1, 10, 20}));
    }
}
