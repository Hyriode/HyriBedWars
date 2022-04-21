package fr.hyriode.bedwars.game.material;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.HyriItem;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class HyriShopItem<T extends JavaPlugin> extends HyriItem<T> {

    private final BWMaterial material;

    public HyriShopItem(T plugin, BWMaterial material) {
        super(plugin, material.getName(), () -> material.getItemShop().getName(), material.getItemShop().getItemStack().getType());
        this.material = material;
    }

    public BWMaterial getBWMaterial() {
        return material;
    }

    @Override
    public ItemStack onPreGive(IHyrame hyrame, Player player, int slot, ItemStack itemStack) {
        return new ItemBuilder(itemStack).withName(ChatColor.WHITE + itemStack.getItemMeta().getDisplayName()).build();
    }
}
