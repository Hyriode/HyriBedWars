package fr.hyriode.bedwars.game.npc.inventory.shop.material.upgradable;

import fr.hyriode.bedwars.game.npc.inventory.EHyriBWShopNavBar;
import fr.hyriode.bedwars.game.npc.inventory.shop.ItemShop;
import fr.hyriode.bedwars.game.npc.inventory.shop.ItemShopStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ArmorBW extends ItemShop {

    private final int level;

    private ItemStack leggings;
    private ItemStack boots;

    public ArmorBW(int level, Material material, String keyName, boolean permanent, ItemShopStack... price) {
        super(new ItemStack(material), keyName, EHyriBWShopNavBar.ARMOR, permanent, null, price);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public ArmorBW setLeggings(ItemStack leggings) {
        this.leggings = leggings;
        return this;
    }

    public ArmorBW setBoots(ItemStack boots) {
        this.boots = boots;
        return this;
    }

    public ItemStack getLeggings() {
        return this.leggings;
    }

    public ItemStack getBoots() {
        return this.boots;
    }
}
