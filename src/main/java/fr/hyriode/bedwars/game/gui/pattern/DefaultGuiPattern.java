package fr.hyriode.bedwars.game.gui.pattern;

import fr.hyriode.bedwars.game.gui.pattern.shop.ShopHypixelGuiPattern;
import fr.hyriode.bedwars.game.gui.pattern.shop.ShopHyriodeGuiPattern;
import fr.hyriode.bedwars.game.gui.shop.ShopGui;

public enum DefaultGuiPattern {

    HYPIXEL(ShopHypixelGuiPattern.class),
    HYRIODE(ShopHyriodeGuiPattern.class),

    ;

    private final Class<? extends GuiPattern> guiPattern;

    DefaultGuiPattern(Class<? extends GuiPattern> guiPattern){
        this.guiPattern = guiPattern;
    }

    public GuiPattern getGuiPattern(ShopGui gui) {
        try {
            return guiPattern.getConstructor(ShopGui.class).newInstance(gui);
        } catch (Exception e) {
            return new EmptyGuiPattern(gui);
        }
    }

}
