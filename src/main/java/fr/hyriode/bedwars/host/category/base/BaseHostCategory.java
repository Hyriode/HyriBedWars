package fr.hyriode.bedwars.host.category.base;

import fr.hyriode.bedwars.host.category.BWHostCategory;
import fr.hyriode.bedwars.utils.BWHostUtils;
import org.bukkit.Material;

public class BaseHostCategory extends BWHostCategory {
    public BaseHostCategory() {
        super(BWHostUtils.categoryDisplay("base", Material.WOOL));

    }
}
