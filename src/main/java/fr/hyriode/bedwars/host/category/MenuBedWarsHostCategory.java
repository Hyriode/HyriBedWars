package fr.hyriode.bedwars.host.category;

import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.bedwars.host.BWGameValues;
import fr.hyriode.bedwars.host.category.map.EventTimeHostCategory;
import fr.hyriode.bedwars.host.gui.BWHostGUI;
import fr.hyriode.bedwars.host.option.BetterIntegerOption;
import fr.hyriode.bedwars.option.PercentageOption;
import fr.hyriode.bedwars.utils.BWHostUtils;
import fr.hyriode.bedwars.utils.InventoryUtils;
import fr.hyriode.hyrame.host.HostCategory;
import org.bukkit.Material;

public class MenuBedWarsHostCategory extends HostCategory {

    public MenuBedWarsHostCategory() {
        super(BWHostUtils.categoryDisplay("bedwars-menu", Material.BED));
        this.guiProvider = player -> new BWHostGUI(player, this);

        this.addSubCategory(slot(3, 3), new EventTimeHostCategory());
        this.addOption(slot(4, 3), new BetterIntegerOption(BWHostUtils.optionDisplay("time-before-spawn", Material.MONSTER_EGG), BWGameValues.RESPAWNING_DELAY.getDefaultValue(), 0, 60, 10, player -> HyriLanguageMessage.get("host.option.bedwars.time-before-spawn.instant").getValue(player), BetterIntegerOption.Type.SECONDS)); //secondes
        this.addOption(slot(4, 4), new BetterIntegerOption(BWHostUtils.optionDisplay("limit-pos-y", Material.WOOL), BWGameValues.LIMIT_POS_Y.getDefaultValue(), 0, 256, 10, player -> HyriLanguageMessage.get("host.option.bedwars.limit-pos-y.no-limit").getValue(player)));
        this.addOption(slot(3, 4), new BetterIntegerOption(BWHostUtils.optionDisplay("time-before-destroy-bed", Material.BED), BWGameValues.BED_BREAKING_DELAY.getDefaultValue(), 0, 60, 10, player -> HyriLanguageMessage.get("host.option.bedwars.time-before-destroy-bed.no-limit").getValue(player), BetterIntegerOption.Type.MINUTES)); //mins

        this.addOption(slot(6, 3), new BetterIntegerOption(BWHostUtils.optionDisplay("drop-number-diamond", Material.DIAMOND), BWGameValues.DIAMOND_GENERATOR_RATE.getDefaultValue(), 1, 10, 5, null, BetterIntegerOption.Type.MULTIPLE));
        this.addOption(slot(7, 3), new BetterIntegerOption(BWHostUtils.optionDisplay("drop-number-emerald", Material.EMERALD), BWGameValues.EMERALD_GENERATOR_RATE.getDefaultValue(), 1, 10, 5, null, BetterIntegerOption.Type.MULTIPLE));
        this.addOption(slot(7, 4), new PercentageOption(BWHostUtils.optionDisplay("drop-number-forge", Material.FURNACE), BWGameValues.FORGE_GENERATOR_RATE.getDefaultValue(), 25, 500, 25));
    }

    private int slot(int x, int y) {
        return InventoryUtils.getSlotByXY(x, y);
    }

}
