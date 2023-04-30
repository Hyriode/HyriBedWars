package fr.hyriode.bedwars.host.category.base;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.trap.Trap;
import fr.hyriode.bedwars.game.upgrade.Upgrade;
import fr.hyriode.bedwars.host.BWTrapValues;
import fr.hyriode.bedwars.host.category.BWHostCategory;
import fr.hyriode.bedwars.option.PercentageOption;
import fr.hyriode.bedwars.utils.BWHostUtils;
import fr.hyriode.hyrame.host.option.BooleanOption;
import fr.hyriode.hyrame.host.option.PreciseIntegerOption;
import org.bukkit.Material;

import java.util.List;

public class TrapHostCategory extends BWHostCategory {
    public TrapHostCategory(HyriBedWars plugin) {
        super(BWHostUtils.categoryDisplay("trap", Material.TRIPWIRE_HOOK));

        List<Trap> traps = plugin.getTrapManager().getTraps();

        this.addOption(slot(2, 3), new BooleanOption(BWHostUtils.optionDisplay("traps-enabled", Material.LEVER), BWTrapValues.TRAP_ENABLED.getDefaultValue()));

        int i = 0;
        for(int y = 0; y < 2; ++y) {
            for (int x = 0; x < 3; ++x) {
                if (traps.size() > i) {
                    Trap trap = traps.get(i++);
                    this.addOption(slot(4 + x, 3 + y), new BooleanOption(BWHostUtils.optionDisplay("trap-" + trap.getName() + "-enabled", trap.getIcon()), true));
                }
            }
        }
    }
}
