package fr.hyriode.bedwars.host.category.base;

import fr.hyriode.bedwars.host.category.BWHostCategory;
import fr.hyriode.bedwars.utils.BWHostUtils;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.Material;

public class UpgradeHostCategory extends BWHostCategory {
    public UpgradeHostCategory() {
        super(BWHostUtils.categoryDisplay("upgrade",
                ItemBuilder.asHead(() -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjQxZjFlZjQzOWY5MTA2OWE0MzY3OGQyMjdhZDQ1OGQ2NjNlYzA0MzYzYmNlOWM3YzAxOWM1Njc5ZThjZjAwNCJ9fX0=").build()));
        ;
    }
}
