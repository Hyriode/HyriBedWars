package fr.hyriode.bedwars.game.npc.inventory.shop;

import fr.hyriode.bedwars.game.material.BWMaterial;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.npc.inventory.shop.pages.BWShopQuickBuy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BWShopInventory extends HyriInventory {

    protected final HyriBedWars plugin;
    private final int categoryId;
    private final BWShopCategory category;

    public BWShopInventory(HyriBedWars plugin, Player owner, BWShopCategory category) {
        super(owner, category.getTitleLanguage().getForPlayer(owner), 54);
        this.plugin = plugin;
        this.category = category;
        this.categoryId = category.getId();
        this.initGui();
    }

    protected void initGui(){
        this.initNavBar();
        this.initPage();
    }

    @SuppressWarnings("ALL")
    private void initPage(){
        int i = 19;
        for (BWMaterial material : getMaterialByCategory(this.getCategory())) {
            if(i == 26 || i == 34) i += 2;
            try {
                this.setItem(i, material.getItemShop().getItemForShop(this.getPlayer()), material.getItemShop().getClick(this.plugin, this));
            }catch (Exception e){
                e.printStackTrace();
            }
            i++;
        }
    }

    protected void initNavBar(){
        int i = 0;
        for(BWShopCategory item : BWShopCategory.values()){
            this.setItem(i, item.getItemStack(this.owner, item.getId() == categoryId), event -> this.openGui(item));
            ++i;
        }

        for(int j = 9 ; j < 18 ; ++j){
            byte data = 7;
            if(j == 9 + categoryId)
                data = 13;
            this.setItem(j, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, data)
                    .withName(ChatColor.DARK_GRAY + "⬆" + ChatColor.GRAY + " " +
                            HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.navbar.separator.title"))
                    .withLore(ChatColor.DARK_GRAY + "⬇" + ChatColor.GRAY + " " +
                            HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.navbar.separator.lore"))
                    .build());
        }
    }

    private void openGui(BWShopCategory category){
        try {
            if(category == BWShopCategory.QUICK_BUY){
                new BWShopQuickBuy(this.plugin, this.owner).open();
                return;
            }
            new BWShopInventory(this.plugin, this.owner, category).open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected List<BWMaterial> getMaterialByCategory(BWShopCategory category){
        return Arrays.stream(BWMaterial.values()).filter(material -> material.getItemShop().getCategory() == category)
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
}
