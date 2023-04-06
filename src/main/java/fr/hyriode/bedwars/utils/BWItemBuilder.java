package fr.hyriode.bedwars.utils;

import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;

public class BWItemBuilder extends ItemBuilder {
    public BWItemBuilder(ItemStack itemStack) {
        super(itemStack);
    }

    public BWItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public BWItemBuilder(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public BWItemBuilder(Material material, int amount, int data) {
        this(new ItemStack(material, amount, (byte) data));
    }

    public BWItemBuilder(Potion potion, int amount) {
        this(potion.toItemStack(amount));
    }

    public BWItemBuilder(Potion potion) {
        this(potion, 1);
    }


    public ItemBuilder withDescription(String lore) {
        return this.withLore(StringUtils.loreToList(lore));
    }
}
