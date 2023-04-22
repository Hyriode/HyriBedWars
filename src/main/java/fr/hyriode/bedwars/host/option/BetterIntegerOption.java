package fr.hyriode.bedwars.host.option;

import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.hyrame.host.HostDisplay;
import fr.hyriode.hyrame.host.option.IntegerOption;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyrameMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Function;

public class BetterIntegerOption extends IntegerOption {

    private final int step;
    private Type type;
    private Function<Player, String> defaultMessage;

    public BetterIntegerOption(HostDisplay hostDisplay, int defaultValue, int minimum, int maximum, int step, Function<Player, String> defaultMessage) {
        super(hostDisplay, defaultValue, minimum, maximum);
        this.step = step;
        this.defaultMessage = defaultMessage;
        this.valueFormatter = player -> HyrameMessage.HOST_OPTION_NUMBER_FORMATTER.asString(player)
                .replace("%value%", this.value == 0 && this.defaultMessage != null ? this.defaultMessage.apply(player) : this.value+"");
    }

    public BetterIntegerOption(HostDisplay hostDisplay, int defaultValue, int minimum, int maximum, int step, Function<Player, String> defaultMessage, Type type) {
        this(hostDisplay, defaultValue, minimum, maximum, step, defaultMessage);
        this.type = type;
        if(type != null && type != Type.INTEGER && type.getMessage() != null) {
            this.valueFormatter = player -> HyrameMessage.HOST_OPTION_NUMBER_FORMATTER.asString(player)
                    .replace("%value%", this.value == 0 && this.defaultMessage != null ? this.defaultMessage.apply(player) : this.value + "" + type.getMessage().getValue(player));
        }
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


    public enum Type {
        MINUTES("host.option.type.minutes"),
        SECONDS("host.option.type.seconds"),
        MULTIPLE("host.option.type.multiple"),
        INTEGER(null);

        private String key;

        Type(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public HyriLanguageMessage getMessage() {
            if(key == null) return null;
            return HyriLanguageMessage.get(key);
        }
    }
}
