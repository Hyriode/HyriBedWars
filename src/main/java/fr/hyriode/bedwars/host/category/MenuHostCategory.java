package fr.hyriode.bedwars.host.category;

import fr.hyriode.bedwars.game.shop.ShopCategory;
import fr.hyriode.bedwars.host.category.base.ShopHostCategory;
import fr.hyriode.bedwars.host.category.base.TrapHostCategory;
import fr.hyriode.bedwars.host.category.base.UpgradeHostCategory;
import fr.hyriode.bedwars.host.category.base.BaseHostCategory;
import fr.hyriode.bedwars.host.category.map.GeneratorManagerHostCategory;
import fr.hyriode.bedwars.host.category.map.MapHostCategory;
import fr.hyriode.bedwars.host.gui.BWHostGUI;
import fr.hyriode.bedwars.utils.BWHostUtils;
import fr.hyriode.bedwars.utils.InventoryUtils;
import fr.hyriode.hyrame.host.HostCategory;
import org.bukkit.Material;

public class MenuHostCategory extends HostCategory {

    public MenuHostCategory() {
        super(BWHostUtils.categoryDisplay("bedwars", Material.BED));
        this.guiProvider = player -> new BWHostGUI(player, this);

        this.addSubCategory(slot(3, 3), new MapHostCategory());
        this.addSubCategory(slot(3, 4), new GeneratorManagerHostCategory());

        this.addSubCategory(slot(5, 3), new ShopHostCategory(ShopCategory.BLOCKS));
        this.addSubCategory(slot(6, 3), new UpgradeHostCategory());
//        this.addSubCategory(slot(7, 3), new TrapHostCategory());
        this.addSubCategory(slot(7, 3), new BaseHostCategory());
    }

    private int slot(int x, int y) {
        return InventoryUtils.getSlotByXY(x, y);
    }

}
