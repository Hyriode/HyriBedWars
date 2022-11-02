package fr.hyriode.bedwars.host.category;

import fr.hyriode.bedwars.host.gui.BWHostGUI;
import fr.hyriode.bedwars.utils.InventoryUtils;
import fr.hyriode.hyrame.host.HostCategory;
import fr.hyriode.hyrame.host.HostDisplay;

public class BWHostCategory extends HostCategory {

    public BWHostCategory(HostDisplay display) {
        super(display);

        this.guiProvider = player -> new BWHostGUI(player, this);
    }

    protected int slot(int x, int y) {
        return InventoryUtils.getSlotByXY(x, y);
    }
}
