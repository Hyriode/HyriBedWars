package fr.hyriode.bedwars.option;

import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.hyrame.host.HostDisplay;
import fr.hyriode.hyrame.host.option.HostOption;
import fr.hyriode.hyrame.host.option.IntegerOption;
import fr.hyriode.hyrame.host.option.PreciseIntegerOption;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyrameMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PercentageOption extends IntegerOption {

    private final int step;

    public PercentageOption(HostDisplay hostDisplay, int defaultValue, int minimum, int maximum, int step) {
        super(hostDisplay, defaultValue, minimum, maximum);
        this.step = step;
        this.valueFormatter = player -> HyriLanguageMessage.get("host.option.percentage.formatter").getValue(player).replace("%value%", String.valueOf(this.value));
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event) {
        if (event.isLeftClick()) {
            this.setValue(this.value + step);
        } else if (event.isRightClick()) {
            this.setValue(this.value - step);
        }
    }

    @Override
    public ItemStack createItem(Player player) {
        final ItemBuilder builder = new ItemBuilder(super.createItem(player));
        final List<String> lore = builder.getLore();

        if (this.getClass() == PercentageOption.class) {
            lore.set(lore.size() - 1, HyrameMessage.HOST_CLICK_TO_INCREASE.asString(player));
            lore.add(HyrameMessage.HOST_CLICK_TO_DECREASE.asString(player));
        }
        return builder.withLore(lore).build();
    }
}
