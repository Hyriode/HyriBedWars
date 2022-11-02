package fr.hyriode.bedwars.host.category.map.generator;

import fr.hyriode.bedwars.game.generator.BWGenerator;
import fr.hyriode.bedwars.host.BWForgeValues;
import fr.hyriode.bedwars.host.gui.generator.GeneratorHostGUI;
import fr.hyriode.bedwars.utils.BWHostUtils;
import fr.hyriode.bedwars.utils.InventoryUtils;
import fr.hyriode.hyrame.host.HostCategory;
import fr.hyriode.hyrame.host.HostDisplay;
import fr.hyriode.hyrame.host.option.BooleanOption;
import fr.hyriode.hyrame.host.option.PreciseIntegerOption;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class GeneratorHostCategory extends HostCategory {

    public GeneratorHostCategory(HostDisplay hostDisplay, BWGenerator generator) {
        super(hostDisplay);
        this.guiProvider = player -> new GeneratorHostGUI(player, this);

        int slot = InventoryUtils.getSlotByXY(3, 3);

        for (BWGenerator.Tier tier : generator.getTiers()) {
            this.addSubCategory(slot, new GUITier(BWHostUtils.categoryDisplay(tier.getKeyName(), new ItemBuilder(Material.PAPER).build()), tier));
            slot++;
        }
    }

    static class GUITier extends HostCategory {

        private final BWGenerator.Tier tier;

        public GUITier(HostDisplay hostDisplay, BWGenerator.Tier tier) {
            super(hostDisplay);
            this.tier = tier;
            this.guiProvider = player -> new GeneratorHostGUI(player, this);

            this.initGui();
        }

        private void initGui() {
            List<Supplier<BWGenerator.Tier.Drop>> drops = new ArrayList<>(this.tier.getDrops().values());
            int i = 0;
            for(int y = 0; y < 2; ++y) {
                for (int x = 0; x < 7; ++x) {
                    int slot = InventoryUtils.getSlotByXY(x + 2, y + 3);
                    if(drops.size() > i) {
                        BWGenerator.Tier.Drop drop = drops.get(i).get();
                        this.addSubCategory(slot, new GUIDrop(BWHostUtils.categoryDisplay(drop.getDrop().getDisplayName().getKey(), drop.getDrop().getAsItemStack()), tier, drop));
                    } else {
                        return;
                    }
                    i++;
                }
            }
        }
    }

    static class GUIDrop extends HostCategory {

        private final BWGenerator.Tier.Drop drop;
        private final BWGenerator.Tier tier;

        public GUIDrop(HostDisplay hostDisplay, BWGenerator.Tier tier, BWGenerator.Tier.Drop drop) {
            super(hostDisplay);
            this.drop = drop;
            this.tier = tier;
            this.guiProvider = player -> new GeneratorHostGUI(player, this);

            this.initGui();
        }

        private void initGui() {
            this.addOption(InventoryUtils.getSlotByXY(4, 4), new PreciseIntegerOption(BWHostUtils.optionDisplay(BWForgeValues.formatSpawnLimit(this.tier.getName(), this.tier.getTier(), this.drop.getName()), "spawn-limit-generator", Material.MONSTER_EGG), this.drop.getSpawnLimit(), 0, 64, new int[] {1, 5}));
            this.addOption(InventoryUtils.getSlotByXY(5, 4), new PreciseIntegerOption(BWHostUtils.optionDisplay(BWForgeValues.formatSpawnBetween(this.tier.getName(), this.tier.getTier(), this.drop.getName()), "spawn-between-generator", Material.GHAST_TEAR), (int) this.drop.getTimeBetweenSpawns(), 0, 36_000, new int[] {1, 5}));
            this.addOption(InventoryUtils.getSlotByXY(6, 4), new BooleanOption(BWHostUtils.optionDisplay(BWForgeValues.formatSplitting(this.tier.getName(), this.tier.getTier(), this.drop.getName()), "splitting-generator", Material.DROPPER), this.drop.isSplitting()));
        }
    }
}
