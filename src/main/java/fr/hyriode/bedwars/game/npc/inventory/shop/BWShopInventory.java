package fr.hyriode.bedwars.game.npc.inventory.shop;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.api.player.HyriBWPlayer;
import fr.hyriode.bedwars.api.player.HyriGameStyle;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.material.BWMaterial;
import fr.hyriode.bedwars.game.material.ItemShop;
import fr.hyriode.bedwars.game.npc.inventory.shop.pages.BWShopQuickBuy;
import fr.hyriode.bedwars.game.start.items.BWGamePlayItem;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BWShopInventory extends HyriInventory {

    protected final HyriBedWars plugin;
    private final int categoryId;
    private final BWShopCategory category;
    private final HyriGameStyle gameStyle;

    public BWShopInventory(HyriBedWars plugin, Player owner, BWShopCategory category) {
        super(owner, category.getTitleLanguage().getForPlayer(owner), 54);
        this.plugin = plugin;
        this.category = category;
        this.categoryId = category.getId();
        this.gameStyle = plugin.getGame().getPlayer(owner).getAccount().getGameStyle();
        this.initGui();
    }

    protected void initGui(){
        this.initNavBar();
        this.initPage();
    }

    @SuppressWarnings("ALL")
    private void initPage(){
        int pointStart = 19;
        int i = pointStart;
        List<BWMaterial> materials = this.getMaterialByCategory(this.getCategory());
        boolean isMelee = false;
        if(this.isHyriode() && this.category == BWShopCategory.MELEE) {
            materials.addAll(this.getMaterialByCategory(BWShopCategory.RANGED));
            isMelee = true;
        }
        for (BWMaterial material : materials) {
            if(i == pointStart + 7 || i == pointStart + 15) i += 2;
            ItemShop itemShop = material.isItemUpgradable() ? material.getItemUpgradable().getTierItem(0) : material.getItemShop();
            if(this.isHyriode() && isMelee && itemShop.getCategory() == BWShopCategory.RANGED){
                i += 5;
                isMelee = false;
            }
            this.setItem(i, itemShop.getItemForShop(this, this.getPlayer()),
                    itemShop.getClick(this.plugin, this));
            i++;
        }
    }

    protected void initNavBar(){
        HyriBWPlayer player = this.plugin.getGame().getPlayer(this.owner).getAccount();
        if(this.isHyriode()){
            ItemStack itemSeparator = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 9).withName(" ").build();
            for(int j = 9 ; j < 18 ; ++j){
                this.setItem(j, itemSeparator);
            }
            for(int j = 45 ; j < 54 ; ++j){
                this.setItem(j, itemSeparator);
            }
            this.setItem(4, BWShopCategory.QUICK_BUY.getItemStack(owner, BWShopCategory.QUICK_BUY.getId() == categoryId, this.isHyriode()), event -> this.openGui(BWShopCategory.QUICK_BUY));
            this.setItem(13, itemSeparator);
            this.setItem(18, itemSeparator);
            this.setItem(26, itemSeparator);
            this.setItem(27, itemSeparator);
            this.setItem(35, itemSeparator);
            this.setItem(36, itemSeparator);
            this.setItem(44, itemSeparator);
            this.setItem(45, itemSeparator);
            this.setItem(53, itemSeparator);

            this.setItem(0, BWShopCategory.ARMOR.getItemStack(owner, BWShopCategory.ARMOR == category, this.isHyriode()), event -> this.openGui(BWShopCategory.ARMOR));
            this.setItem(1, BWShopCategory.BLOCKS.getItemStack(owner, BWShopCategory.BLOCKS == category, this.isHyriode()), event -> this.openGui(BWShopCategory.BLOCKS));
            this.setItem(2, BWShopCategory.MELEE.getItemStack(owner, BWShopCategory.MELEE == category, this.isHyriode()), event -> this.openGui(BWShopCategory.MELEE));
            this.setItem(6, BWShopCategory.POTIONS.getItemStack(owner, BWShopCategory.POTIONS == category, this.isHyriode()), event -> this.openGui(BWShopCategory.POTIONS));
            this.setItem(7, BWShopCategory.UTILITY.getItemStack(owner, BWShopCategory.UTILITY == category, this.isHyriode()), event -> this.openGui(BWShopCategory.UTILITY));
            this.setItem(8, BWShopCategory.TOOLS.getItemStack(owner, BWShopCategory.TOOLS == category, this.isHyriode()), event -> this.openGui(BWShopCategory.TOOLS));

            this.setItem(44, BWGamePlayItem.getItemStack(this.owner, player),
                    event -> {
                        this.openGui(this.category, player.changeGamePlayStyle() == HyriGameStyle.HYRIODE);
                        player.update(this.owner.getUniqueId());
                    });
            return;
        }
        int i = 0;
        for(BWShopCategory item : BWShopCategory.values()){
            this.setItem(i, item.getItemStack(this.owner, item == category, this.isHyriode()), event -> this.openGui(item));
            ++i;
        }

        for(int j = 9 ; j < 18 ; ++j){
            byte colorSeperator = 7;
            if(j == 9 + categoryId)
                colorSeperator = 13;
            this.setItem(j, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, colorSeperator)
                    .withName(ChatColor.DARK_GRAY + "⬆ " + ChatColor.GRAY + getNavbarSeperator("title"))
                    .withLore(ChatColor.DARK_GRAY + "⬇ " + ChatColor.GRAY + getNavbarSeperator("lore"))
                    .build());
        }

        this.setItem(44, BWGamePlayItem.getItemStack(this.owner, player),
                event -> {
                    this.openGui(this.category, player.changeGamePlayStyle() == HyriGameStyle.HYRIODE);
                    player.update(this.owner.getUniqueId());
                });
    }

    private void openGui(BWShopCategory category){
        this.openGui(category, this.isHyriode());
    }

    private void openGui(BWShopCategory category, boolean isHyriode){
        try {
            if(category == BWShopCategory.QUICK_BUY){
                BWShopQuickBuy.open(this.plugin, this.owner);
                return;
            }
            if(isHyriode && category == BWShopCategory.RANGED){
                BWShopInventory.open(this.plugin, this.owner, BWShopCategory.MELEE);
                return;
            }
            BWShopInventory.open(this.plugin, this.owner, category);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected List<BWMaterial> getMaterialByCategory(BWShopCategory category){
        return Arrays.stream(BWMaterial.values()).filter(material -> (material.isItemUpgradable() ? material.getItemUpgradable().getTierItem(0).getCategory() : material.getItemShop().getCategory()) == category)
                .collect(Collectors.toList());
    }

    public BWShopCategory getCategory() {
        return category;
    }

    public BWGamePlayer getPlayer(){
        return this.plugin.getGame().getPlayer(this.owner.getUniqueId());
    }

    public void refreshGui(){
        this.openGui(this.category);
    }

    private String getNavbarSeperator(String key){
        return HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.navbar.separator." + key);
    }

    public boolean isHyriode(){
        return this.gameStyle == HyriGameStyle.HYRIODE;
    }

    public static void open(HyriBedWars plugin, Player owner, BWShopCategory category){
        new BWShopInventory(plugin, owner, category).open();
    }
}
