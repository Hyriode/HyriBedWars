package fr.hyriode.bedwars.game.gui.pattern.shop;

import fr.hyriode.bedwars.game.gui.manager.GuiManager;
import fr.hyriode.bedwars.game.gui.pattern.GuiPattern;
import fr.hyriode.bedwars.game.gui.shop.ShopGui;
import fr.hyriode.bedwars.game.shop.ShopCategory;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ShopHypixelGuiPattern extends GuiPattern {

    public ShopHypixelGuiPattern(ShopGui gui){
        super(gui, new Size(2, 3, 7, 3));

        this.setLineVertical(1, 2, 9, this.getSeparator(7));
        int i = 1;
        for (ShopCategory category : ShopCategory.values()) {
            this.setSlot(i, 1, category.getItemStack(false),
                    event -> GuiManager.openShopGui(gui, category));
            if (gui.getCategory() == category) {
                this.setSlot(i, 2, this.getSeparator(13));
            } else {
                this.setSlot(i, 2, this.getSeparator(7),
                        event -> GuiManager.openShopGui(gui, category));
            }
            ++i;
        }
    }

    private ItemStack getSeparator(int color){
        return new ItemBuilder(Material.STAINED_GLASS_PANE, 1, color)
                .withName(ChatColor.DARK_GRAY + "⬆ " + ChatColor.GRAY + HyriLanguageMessage.get("shop.inventory.separator.title").getForPlayer(gui.getOwner()))
                .withLore(ChatColor.DARK_GRAY + "⬇ " + ChatColor.GRAY + HyriLanguageMessage.get("shop.inventory.separator.lore").getForPlayer(gui.getOwner()))
                .build();
    }

}
