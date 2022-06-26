package fr.hyriode.bedwars.game.gui.pattern.shop;

import fr.hyriode.bedwars.game.gui.manager.GuiManager;
import fr.hyriode.bedwars.game.gui.pattern.GuiPattern;
import fr.hyriode.bedwars.game.gui.shop.ShopGui;
import fr.hyriode.bedwars.game.shop.ShopCategory;
import static fr.hyriode.bedwars.game.shop.ShopCategory.*;

import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ShopHyriodeGuiPattern extends GuiPattern {

    public ShopHyriodeGuiPattern(ShopGui gui) {
        super(gui, new Size(2, 3, 7, 3));

        this.setNavbar();

        this.setLineVertical(1, 2, 9, this.getItemDeco());
        this.setLineHorizontal(1, 2, 5, this.getItemDeco());
        this.setLineVertical(1, 6, 9, this.getItemDeco());
        this.setLineHorizontal(9, 2, 5, this.getItemDeco());

    }

    private void setNavbar(){
        this.setSlotNavItem(0, ARMOR);
        this.setSlotNavItem(1, BLOCKS);
        this.setSlotNavItem(2, MELEE);

        this.setSlotNavItem(4, QUICK_BUY);

        this.setSlotNavItem(6, POTIONS);
        this.setSlotNavItem(7, UTILITY);
        this.setSlotNavItem(8, TOOLS);
    }

    private void setSlotNavItem(int slot, ShopCategory category){
        this.setSlot(slot, category.getItemStack(this.gui.getOwner(), this.gui.getCategory() == category, true), this.getClick(category));
    }

    private Consumer<InventoryClickEvent> getClick(ShopCategory category){
        return event -> GuiManager.openShopGui(this.gui, category);
    }

    private ItemStack getItemDeco(){
        return new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 9).withName(" ").build();
    }
}
