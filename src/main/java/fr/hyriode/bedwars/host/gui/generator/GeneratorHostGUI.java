package fr.hyriode.bedwars.host.gui.generator;

import fr.hyriode.bedwars.utils.BWHostUtils;
import fr.hyriode.hyrame.host.HostCategory;
import fr.hyriode.hyrame.host.gui.HostGUI;
import org.bukkit.entity.Player;

public class GeneratorHostGUI extends HostGUI {

    public GeneratorHostGUI(Player owner, HostCategory category) {
        super(owner, category.getDisplayName().getValue(owner), category);

        this.setItem(4, BWHostUtils.createItem(owner, category));
    }

}
