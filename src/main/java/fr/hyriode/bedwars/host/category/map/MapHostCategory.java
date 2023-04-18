package fr.hyriode.bedwars.host.category.map;

import fr.hyriode.bedwars.host.category.BWHostCategory;
import fr.hyriode.bedwars.utils.BWHostUtils;
import fr.hyriode.hyrame.host.HostDisplay;
import fr.hyriode.hyrame.host.option.BooleanOption;
import fr.hyriode.hyrame.host.option.PreciseIntegerOption;
import org.bukkit.Material;

public class MapHostCategory extends BWHostCategory {

    public MapHostCategory() {
        super(BWHostUtils.categoryDisplay("map-manager", Material.GRASS));

        this.addOption(slot(4, 4), new BooleanOption(BWHostUtils.optionDisplay("break-world", Material.STONE), false));
        this.addSubCategory(slot(5, 4), new EventTimeHostCategory());
        this.addOption(slot(6, 4), new PreciseIntegerOption(BWHostUtils.optionDisplay("cooldown-fireball", Material.FIREBALL), 10, 0, 100, new int[] {1, 10, 20}));
    }
}
