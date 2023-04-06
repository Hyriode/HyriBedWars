package fr.hyriode.bedwars.host.category.base;

import fr.hyriode.bedwars.host.category.BWHostCategory;
import fr.hyriode.bedwars.utils.BWHostUtils;
import org.bukkit.Material;

public class TrapHostCategory extends BWHostCategory {
    public TrapHostCategory() {
        super(BWHostUtils.categoryDisplay("trap", Material.TRIPWIRE_HOOK));
    }
}
