package fr.hyriode.bedwars.game.npc.inventory.shop.pages;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.material.BWMaterial;
import fr.hyriode.bedwars.game.material.ItemEmptySlot;
import fr.hyriode.bedwars.game.material.ItemShop;
import fr.hyriode.bedwars.game.material.tracker.BWTrackerItem;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopCategory;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopInventory;
import fr.hyriode.bedwars.utils.StringBWUtils;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class BWShopQuickBuy extends BWShopInventory {

    public BWShopQuickBuy(HyriBedWars plugin, Player owner) {
        super(plugin, owner, BWShopCategory.QUICK_BUY);
    }

    @Override
    protected void initGui() {
        super.initNavBar();

        final BWGamePlayer player = this.getPlayer();

        //Fill Quick buy zone
        if(!this.isHyriode()) {
            int pointStart = 19;
            int i = pointStart;
            for (;;++i) {
                if (i >= pointStart + 25) {
                    break;
                }
                if (i <= pointStart + 6 || i >= pointStart + 9 && i <= pointStart + 15 || i >= pointStart + 18) {
                    this.setItem(i, this.getEmptySlot());
                }
            }
        }

        final Map<String, Integer> quickBuy = this.getPlayer().getAccount().getQuickBuy();

        for(String material : quickBuy.keySet()){
            try {
                int slot = quickBuy.get(material);
                final ItemShop item = BWMaterial.valueOf(material).isItemUpgradable() ? BWMaterial.valueOf(material).getItemUpgradable().getTierItem(0) : BWMaterial.valueOf(material).getItemShop();
                this.setItem(slot, item.getItemForShop(this, player), item.getClick(this.plugin, this));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        this.addQuickBuyButtons();
    }

    private void addQuickBuyButtons(){
        this.setItem(45, BWTrackerItem.getItemGUI(this.owner),
                event -> new BWTrackerGUI(this.plugin, this.owner, this).open());

        this.setItem(53, new ItemBuilder(this.isHyriode() ? Material.SUGAR : Material.BLAZE_POWDER)
                .withName(ChatColor.GREEN + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.quick_buy.hotbar.name"))
                .withLore(StringBWUtils.loreToList(HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.quick_buy.hotbar.lore"), " ", ChatColor.YELLOW + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.click.edit")))
                .build(),
                event -> BWHotbarManagerGUI.open(this.plugin, this.owner));
    }

    private ItemStack getEmptySlot(){
        return new ItemEmptySlot().getItemForShop(this, this.getPlayer());
    }

    public static void open(HyriBedWars plugin, Player owner){
        new BWShopQuickBuy(plugin, owner).open();
    }
}
