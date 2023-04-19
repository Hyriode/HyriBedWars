package fr.hyriode.bedwars.host.category.base;

import fr.hyriode.bedwars.host.category.BWHostCategory;
import fr.hyriode.bedwars.host.category.base.effect.EffectPermanentHostCategory;
import fr.hyriode.bedwars.utils.BWHostUtils;
import fr.hyriode.hyrame.host.option.PreciseIntegerOption;
import org.bukkit.Material;

public class BaseHostCategory extends BWHostCategory {
    public BaseHostCategory() {
        super(BWHostUtils.categoryDisplay("base", Material.WOOL));

        this.addSubCategory(slot(4, 3), new EffectPermanentHostCategory());
        this.addOption(slot(5, 3), new PreciseIntegerOption(BWHostUtils.optionDisplay("dream-defender", Material.PUMPKIN), 10, 0, 5000, new int[] {1, 20}));
        this.addOption(slot(6, 3), new PreciseIntegerOption(BWHostUtils.optionDisplay("dream-defender", Material.SNOW_BALL), 10, 0, 5000, new int[] {1, 20}));
    }
}
