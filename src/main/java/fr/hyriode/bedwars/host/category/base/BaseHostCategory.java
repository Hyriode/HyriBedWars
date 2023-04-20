package fr.hyriode.bedwars.host.category.base;

import fr.hyriode.bedwars.game.entity.models.BedBugEntity;
import fr.hyriode.bedwars.game.entity.models.DreamDefenderEntity;
import fr.hyriode.bedwars.host.category.BWHostCategory;
import fr.hyriode.bedwars.host.category.base.effect.EffectPermanentHostCategory;
import fr.hyriode.bedwars.utils.BWHostUtils;
import fr.hyriode.hyrame.host.option.PreciseIntegerOption;
import org.bukkit.Material;

public class BaseHostCategory extends BWHostCategory {
    public BaseHostCategory() {
        super(BWHostUtils.categoryDisplay("base", Material.WOOL));

//        this.addSubCategory(slot(4, 3), new EffectPermanentHostCategory());
    }
}
