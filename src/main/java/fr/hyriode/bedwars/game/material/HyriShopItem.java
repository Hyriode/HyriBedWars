package fr.hyriode.bedwars.game.material;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopCategory;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.HyriItem;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HyriShopItem<T extends JavaPlugin> extends HyriItem<T> {

    private final BWShopCategory category;
    private final List<OreStack> price;

    public HyriShopItem(T plugin, String name, Material material, BWShopCategory category, OreStack... price) {
        super(plugin, name, () -> HyriBedWars.getLanguageManager().getMessage("shop.item."+name+".name"), material);
        this.category = category;
        this.price = Arrays.asList(price);
    }

    public BWShopCategory getCategory() {
        return category;
    }

    public List<OreStack> getPrice() {
        return price;
    }

    @Override
    public void onGive(IHyrame hyrame, Player player, int slot, ItemStack itemStack) {
        player.getInventory().setItem(slot, new ItemBuilder(itemStack).withName(ChatColor.WHITE + itemStack.getItemMeta().getDisplayName()).build());
    }
}
