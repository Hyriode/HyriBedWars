package fr.hyriode.bedwars.host.category.map;

import fr.hyriode.bedwars.game.BWEvent;
import fr.hyriode.bedwars.host.category.BWHostCategory;
import fr.hyriode.bedwars.utils.BWHostUtils;
import fr.hyriode.hyrame.host.option.PreciseIntegerOption;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.item.ItemHead;
import org.bukkit.Material;

public class EventTimeHostCategory extends BWHostCategory {

    public EventTimeHostCategory() {
        super(BWHostUtils.categoryDisplay("event-time", ItemBuilder.asHead(Head.CLOCK).build()));

        int i = 2;
        for (BWEvent value : BWEvent.values()) {
            this.addOption(slot(i++, 4), new PreciseIntegerOption(BWHostUtils.optionDisplay("event-time-" + value.getKey(), value.getIcon()), value.getTime().getDefaultValue(), 0, 5000, new int[] {1, 20}));
        }
    }

    enum Head implements ItemHead {
        CLOCK("ewogICJ0aW1lc3RhbXAiIDogMTY2NTcxOTYzMDUyNiwKICAicHJvZmlsZUlkIiA6ICJiMDU4MTFjYTdmNDk0YTM5OTZiNDU4ZjcwMmQ2MzJiOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJVeWlsIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2FkNDUyYmZjZGMzZWExNGY5ZTYxMmIxYzk2YWJlZjk3YzEwZTk2YzcxMTZlYTJhNGIxYTVkZjhkNGFhMTcyZjkiCiAgICB9CiAgfQp9")
        ;

        private final String texture;

        Head(String texture) {
            this.texture = texture;
        }
        @Override
        public String getTexture() {
            return this.texture;
        }
    }

}
