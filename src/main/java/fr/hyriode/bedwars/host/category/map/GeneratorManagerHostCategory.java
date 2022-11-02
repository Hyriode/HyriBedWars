package fr.hyriode.bedwars.host.category.map;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.generator.GeneratorManager;
import fr.hyriode.bedwars.host.category.BWHostCategory;
import fr.hyriode.bedwars.host.category.map.generator.GeneratorHostCategory;
import fr.hyriode.bedwars.utils.BWHostUtils;
import org.bukkit.Material;

public class GeneratorManagerHostCategory extends BWHostCategory {

    public GeneratorManagerHostCategory() {
        super(BWHostUtils.categoryDisplay("generator", Material.DISPENSER));

        GeneratorManager gm = HyriBedWars.getGeneratorManager();

        this.addSubCategory(slot(5, 3), new GeneratorHostCategory(BWHostUtils.optionDisplay("forge-generator", Material.FURNACE), gm.getGeneratorByName(GeneratorManager.FORGE)));
        this.addSubCategory(slot(4, 4), new GeneratorHostCategory(BWHostUtils.optionDisplay("diamond-generator", Material.DIAMOND_BLOCK), gm.getGeneratorByName(GeneratorManager.DIAMOND)));
        this.addSubCategory(slot(6, 4), new GeneratorHostCategory(BWHostUtils.optionDisplay("emerald-generator", Material.EMERALD_BLOCK), gm.getGeneratorByName(GeneratorManager.EMERALD)));
    }

}
