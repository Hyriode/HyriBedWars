package fr.hyriode.bedwars.host.gui;

import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.bedwars.utils.BWHostUtils;
import fr.hyriode.hyrame.host.HostCategory;
import fr.hyriode.hyrame.host.gui.HostGUI;
import org.bukkit.entity.Player;

public class BWHostGUI extends HostGUI {

    public BWHostGUI(Player owner, HostCategory category) {
        super(owner, HyriLanguageMessage.get("host.gui." + category.getName() + ".title").getValue(owner), category);

        this.setItem(4, BWHostUtils.createItem(owner, category));
    }

}
