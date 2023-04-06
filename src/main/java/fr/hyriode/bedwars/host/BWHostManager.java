package fr.hyriode.bedwars.host;

import fr.hyriode.bedwars.host.category.MenuHostCategory;
import fr.hyriode.hyrame.HyrameLoader;

public class BWHostManager {
    public void attach() {
        HyrameLoader.getHyrame().getHostController().addCategory(25, new MenuHostCategory());

    }
}
