package fr.hyriode.bedwars.host.option;

import fr.hyriode.hyrame.host.HostDisplay;
import fr.hyriode.hyrame.host.option.BooleanOption;
import fr.hyriode.hyrame.host.option.HostOption;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AnimationBooleanOption extends BooleanOption {

    private final Material firstItem;
    private final Material secondItem;

    public AnimationBooleanOption(HostDisplay hostDisplay, boolean defaultValue, Material firstItem, Material secondItem) {
        super(hostDisplay, defaultValue);
        this.firstItem = firstItem;
        this.secondItem = secondItem;
    }

    @Override
    public ItemStack createItem(Player player) {
        ItemStack itemStack = super.createItem(player);
        itemStack.setType(this.value ? this.firstItem : this.secondItem);
        return itemStack;
    }
}
