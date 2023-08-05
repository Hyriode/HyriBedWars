package fr.hyriode.bedwars.game.gui.shop.quickbuy;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.api.player.BWPlayerData;
import fr.hyriode.bedwars.game.gui.BWGui;
import fr.hyriode.bedwars.game.gui.manager.GuiManager;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.shop.ItemShop;
import fr.hyriode.bedwars.game.shop.material.MaterialShop;
import fr.hyriode.bedwars.game.shop.ShopCategory;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.api.language.HyriLanguageMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ChoiceSlotGui extends BWGui {

    private final MaterialShop materialShop;

    public ChoiceSlotGui(Player owner, HyriBedWars plugin, MaterialShop materialShop, BWGui backGui) {
        super(owner, plugin, HyriLanguageMessage.get("gui.choice-slot.title"), TypeSize.LINE_6, backGui, false);
        this.materialShop = materialShop;
        this.initGui();
    }

    @Override
    protected void initGui() {
        BWGamePlayer player = this.getPlayer();
        BWPlayerData account = player.getAccount();
        Map<Integer, MaterialShop> quickBuy = account.getQuickBuyShop();

        this.setItem(5, 1, this.getItemSlot());

        int i = 0;
        for(int y = 0; y < 3; ++y) {
            for (int x = 0; x < 7; ++x) {
                ItemStack itemStack = null;
                if (quickBuy.containsKey(i)) {
                    MaterialShop material = quickBuy.get(i);
                    if (material != null) {
                        ItemShop itemShop = material.getItemShopForPlayer(player);
                        itemStack = new ItemBuilder(itemShop.getItemStack(player))
                                .withName(ChatColor.AQUA + itemShop.getDisplayName().getValue(player))
                                .withLore(ChatColor.YELLOW + HyriLanguageMessage.get("gui.choice-slot.items.lore").getValue(player))
                                .build();
                    }
                }
                int slot = i;
                this.setItem(2 + x, 3 + y, itemStack != null ? itemStack : new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 14).withName(" ").build(),
                        event -> {
                            account.putMaterialQuickBuy(slot, materialShop.getName());
                            GuiManager.openShopGui(this.plugin, this.owner, ShopCategory.QUICK_BUY);
                        });
                i++;
            }
        }

    }

    private ItemStack getItemSlot(){
        BWGamePlayer player = this.getPlayer();
        ItemShop itemShop = this.materialShop.getItemShopForPlayer(player);

        return new ItemBuilder(itemShop.getItemStack(player))
                .withName(ChatColor.RESET + itemShop.getDisplayName().getValue(player))
                .withLore(ChatColor.YELLOW + HyriLanguageMessage.get("gui.choice-slot.lore").getValue(player))
                .build();
    }

    @Override
    protected void refresh() {

    }
}
