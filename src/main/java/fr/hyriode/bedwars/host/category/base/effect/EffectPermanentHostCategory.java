package fr.hyriode.bedwars.host.category.base.effect;

import fr.hyriode.bedwars.host.category.BWHostCategory;
import fr.hyriode.bedwars.utils.BWHostUtils;
import org.bukkit.Material;

public class EffectPermanentHostCategory extends BWHostCategory {

    public EffectPermanentHostCategory() {
        super(BWHostUtils.optionDisplay("effect-permanent", Material.POTION));


    }
}
