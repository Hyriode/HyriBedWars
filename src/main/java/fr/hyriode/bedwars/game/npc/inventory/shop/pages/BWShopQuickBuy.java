package fr.hyriode.bedwars.game.npc.inventory.shop.pages;

import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.material.BWMaterial;
import fr.hyriode.bedwars.game.material.ItemEmptySlot;
import fr.hyriode.bedwars.game.material.ItemShop;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopCategory;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class BWShopQuickBuy extends BWShopInventory {

    public BWShopQuickBuy(HyriBedWars plugin, Player owner) {
        super(plugin, owner, BWShopCategory.QUICK_BUY);
    }

    @Override
    protected void initGui() {
        super.initNavBar();

        final BWGamePlayer player = this.getPlayer();

        //Fill Quick buy zone
        for(int i = 19 ; ;++i){
            if(i > 43) {
                break;
            }
            if(i <= 25 || i >= 28 && i <= 34 || i >= 37) {
                this.setItem(i, this.getEmptySlot());
            }
        }

        final HashMap<Integer, String> quickBuy = this.getPlayer().getAccount().getQuickBuy();

        for(Integer i : quickBuy.keySet()){
            final ItemShop item = BWMaterial.valueOf(quickBuy.get(i)).getItemShop();
            this.setItem(i, item.getItemForShop(player), item.getClick(this.plugin, this));
        }

        this.addQuickBuyButtons();
    }

    private void addQuickBuyButtons(){
        this.setItem(45, new ItemBuilder(Material.COMPASS)
                .withName(ChatColor.GREEN + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.quick_buy.tracker.name"))
                .withLore(ChatColor.GRAY + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.quick_buy.tracker.lore1"),
                        ChatColor.GRAY + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.quick_buy.tracker.lore2"),
                        ChatColor.GRAY + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.quick_buy.tracker.lore3"),
                        ChatColor.GRAY + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.quick_buy.tracker.lore4"))
                .build());

        this.setItem(53, new ItemBuilder(Material.BLAZE_POWDER)
                .withName(ChatColor.GREEN + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.quick_buy.hotbar.name"))
                .withLore(ChatColor.GRAY + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.quick_buy.hotbar.lore1"),
                        ChatColor.GRAY + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.quick_buy.hotbar.lore2"),
                        " ",
                        ChatColor.GRAY + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.click.edit"))
                .build());
    }

    private ItemStack getEmptySlot(){
        return new ItemEmptySlot().getItemForShop(this.getPlayer());
    }

    public static void open(HyriBedWars plugin, Player owner){
        new BWShopQuickBuy(plugin, owner).open();
    }
}
