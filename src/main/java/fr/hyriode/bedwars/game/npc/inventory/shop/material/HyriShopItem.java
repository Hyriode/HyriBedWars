package fr.hyriode.bedwars.game.npc.inventory.shop.material;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopCategory;
import fr.hyriode.hyrame.item.HyriItem;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HyriShopItem<T extends JavaPlugin> extends HyriItem<T> {

    private BWShopCategory category;
    private List<OreStack> price;

    public HyriShopItem(T plugin, String name, Material material, BWShopCategory category, OreStack... price) {
        super(plugin, name, () -> HyriBedWars.getLanguageManager().getMessage("shop.item."+name+".name"), () -> Collections.singletonList(HyriBedWars.getLanguageManager().getMessage("shop.item."+name+".lore")), material);
        this.category = category;
        this.price = Arrays.asList(price);
    }

    public BWShopCategory getCategory() {
        return category;
    }

    public List<OreStack> getPrice() {
        return price;
    }
}
