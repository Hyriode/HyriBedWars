package fr.hyriode.bedwars.host.option;

import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.hyrame.host.HostDisplay;
import fr.hyriode.hyrame.host.option.HostOption;
import fr.hyriode.hyrame.host.option.IntegerOption;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyrameMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BetterIntegerOption extends IntegerOption {

    private final int step;

    public BetterIntegerOption(HostDisplay hostDisplay, int defaultValue, int minimum, int maximum, int step) {
        super(hostDisplay, defaultValue, minimum, maximum);
        this.step = step;
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event) {
        if (event.isShiftClick() && event.isLeftClick()) {
            this.setValue(this.value + step);
            return;
        } else if (event.isShiftClick() && event.isRightClick()) {
            this.setValue(this.value - step);
            return;
        }
        super.onClick(player, event);
    }

    @Override
    public ItemStack createItem(Player player) {
        final ItemBuilder builder = new ItemBuilder(super.createItem(player));
        final List<String> lore = builder.getLore();

        if (this.getClass() == BetterIntegerOption.class) {
            lore.set(lore.size() - 1, HyrameMessage.HOST_CLICK_TO_INCREASE.asString(player));
            lore.add(HyrameMessage.HOST_CLICK_TO_DECREASE.asString(player));
        }
        return builder.withLore(lore).build();
    }
}
