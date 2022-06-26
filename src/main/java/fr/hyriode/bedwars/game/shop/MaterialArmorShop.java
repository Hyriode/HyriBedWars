package fr.hyriode.bedwars.game.shop;

import fr.hyriode.bedwars.game.player.BWGamePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class MaterialArmorShop extends MaterialShop{

    private int level;
    private ItemStack leggings;
    private ItemStack boots;

    public MaterialArmorShop(String name, ShopCategory category, ItemShop icon, int level, boolean permanent,
                             ItemStack leggings, ItemStack boots) {
        super(name, category, permanent, Collections.singletonList(icon));
        this.level = level;
        this.leggings = leggings;
        this.boots = boots;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public ItemStack getBoots() {
        return boots;
    }

    @Override
    public void buy(BWGamePlayer player) {
        player.setArmor(this.getAsArmor());
        player.giveArmor();
    }

    public int getLevel() {
        return level;
    }
}
