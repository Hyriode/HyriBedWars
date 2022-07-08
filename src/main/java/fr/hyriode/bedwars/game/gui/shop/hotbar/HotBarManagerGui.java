package fr.hyriode.bedwars.game.gui.shop.hotbar;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.api.player.HyriBWPlayer;
import fr.hyriode.bedwars.game.gui.BWGui;
import fr.hyriode.bedwars.game.gui.manager.GuiManager;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.player.hotbar.HotbarCategory;
import fr.hyriode.bedwars.game.shop.ShopCategory;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

public class HotBarManagerGui extends BWGui {

    private final HotbarCategory category;

    public HotBarManagerGui(Player owner, HyriBedWars plugin, HotbarCategory category, BWGui backGui) {
        super(owner, plugin, HyriLanguageMessage.get("inventory.hotbar_manager.title"), TypeSize.LINE_5, backGui, false);
        this.category = category;

        this.initGui();
    }

    @Override
    protected void initGui() {
        BWGamePlayer player = this.getPlayer();
        IHyriPlayer account = player.asHyriPlayer();
        HyriBWPlayer bwAccount = player.getAccount();

        this.setItem(0, new ItemBuilder(Material.ARROW)
                .withName(ChatColor.RED + HyriLanguageMessage.get("gui.back").getForPlayer(this.owner))
                .withLore(ChatColor.GRAY + HyriLanguageMessage.get("gui.back.lore").getForPlayer(this.owner))
                .build(), event -> GuiManager.openShopGui(this.plugin, this.owner, ShopCategory.QUICK_BUY));

        this.setItem(8, new ItemBuilder(Material.BARRIER)
                .withName(ChatColor.RED + HyriLanguageMessage.get("gui.reset").getForPlayer(this.owner))
                .withLore(ChatColor.GRAY + HyriLanguageMessage.get("gui.reset.lore").getForPlayer(this.owner))
                .build(), event -> {
            bwAccount.resetHotbar();
            player.update();
            this.refresh();
        });

        if(this.category != null) {
            this.setItem(5, 2, this.category.getItemStack(account));
        }else {
            int i = 1;
            for (HotbarCategory category : HotbarCategory.values()) {
                this.setItem(++i, 2, category.getItemStack(account),
                        event -> new HotBarManagerGui(this.owner, this.plugin, category, this).open());
            }
        }
        Map<Integer, HotbarCategory> hotbar = bwAccount.getHotBar();
        for (int j = 0; j < 9; j++) {
            int slot = j;
            this.setItem(j + 1, 4, hotbar.containsKey(j) ? hotbar.get(j).getItemStack(account) : this.getItemDeco(),
                    event -> {
                if(this.category != null) {
                    bwAccount.putMaterialHotBar(slot, this.category);
                } else {
                    bwAccount.removeMaterialHotBar(slot);
                }
                player.update();
                this.refresh();
            });
        }
    }

    @Override
    protected void refresh() {
        new HotBarManagerGui(this.getOwner(), this.getPlugin(), null, this).open();
    }
}
