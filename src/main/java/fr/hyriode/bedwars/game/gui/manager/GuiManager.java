package fr.hyriode.bedwars.game.gui.manager;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.api.player.style.HyriGameStyle;
import fr.hyriode.bedwars.game.gui.upgrade.UpgradeGui;
import fr.hyriode.bedwars.game.gui.shop.ShopGui;
import fr.hyriode.bedwars.game.shop.ShopCategory;
import org.bukkit.entity.Player;

public class GuiManager {

    public static void openShopGui(HyriBedWars plugin, Player player, ShopCategory category){
        new ShopGui(player, plugin, category, null).open();
    }

    public static void openShopGui(ShopGui gui, ShopCategory category){
        if(gui.getCategory() == ShopCategory.RANGED && gui.getPlayer().getAccount().getGameStyle() == HyriGameStyle.HYRIODE){
            category = ShopCategory.MELEE;
        }
        new ShopGui(gui.getOwner(), gui.getPlugin(), category, null).open();
    }

    public static void openUpgradeGui(HyriBedWars plugin, Player p) {
        new UpgradeGui(p, plugin, null).open();
    }
}
