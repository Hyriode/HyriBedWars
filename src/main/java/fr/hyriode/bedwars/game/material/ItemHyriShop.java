package fr.hyriode.bedwars.game.material;

import fr.hyriode.bedwars.game.material.utility.BedBugBall;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopCategory;
import fr.hyriode.hyrame.item.HyriItem;
import org.bukkit.inventory.ItemStack;

public class ItemHyriShop extends ItemShop {

    private Class<? extends HyriItem<?>> hyriItem;

    public ItemHyriShop(BWShopCategory category, String name, ItemStack itemStack, OreStack price, Class<? extends HyriItem<?>> hyriItem) {
        super(name, itemStack, category, false, price);
        this.hyriItem = hyriItem;
    }

    public Class<? extends HyriItem<?>> getHyriItem() {
        return hyriItem;
    }
}
