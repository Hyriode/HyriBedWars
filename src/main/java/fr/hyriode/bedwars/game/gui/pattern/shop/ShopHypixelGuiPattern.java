package fr.hyriode.bedwars.game.gui.pattern.shop;

import fr.hyriode.bedwars.game.gui.manager.GuiManager;
import fr.hyriode.bedwars.game.gui.pattern.GuiPattern;
import fr.hyriode.bedwars.game.gui.shop.ShopGui;
import fr.hyriode.bedwars.game.shop.ShopCategory;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.api.language.HyriLanguageMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ShopHypixelGuiPattern extends GuiPattern {

    public ShopHypixelGuiPattern(ShopGui gui){
        super(gui, new Size(2, 3, 7, 3));

        this.setLineVertical(1, 2, 9, this.getSeparator(7));
        int i = 1;
        for (ShopCategory category : ShopCategory.values()) {
            boolean currentCategory = gui.getCategory() == category;

            this.setSlot(i, 1, category.getItemStack(gui.getOwner(), currentCategory, false),
                    event -> GuiManager.openShopGui(gui, category));

            this.setSlot(i, 2, this.getSeparator(currentCategory ? 13 : 7),
                    currentCategory ? event -> GuiManager.openShopGui(gui, category) : null);
            ++i;
        }
    }

    private ItemStack getSeparator(int color){
        return new ItemBuilder(Material.STAINED_GLASS_PANE, 1, color)
                .withName(ChatColor.DARK_GRAY + "⬆ " + ChatColor.GRAY + HyriLanguageMessage.get("shop.inventory.separator.title").getValue(gui.getOwner()))
                .withLore(ChatColor.DARK_GRAY + "⬇ " + ChatColor.GRAY + HyriLanguageMessage.get("shop.inventory.separator.lore").getValue(gui.getOwner()))
                .build();
    }

}
