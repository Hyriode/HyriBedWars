package fr.hyriode.bedwars.game.npc.inventory;

import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.npc.inventory.pages.BWShopQuickBuy;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWMaterial;
import fr.hyriode.bedwars.game.npc.inventory.shop.ItemShopStack;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.upgradable.ArmorBW;
import fr.hyriode.bedwars.utils.InventoryBWUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BWShopInventory extends HyriInventory {

    private final HyriBedWars plugin;
    private final int categoryId;
    private final EHyriBWShopNavBar category;

    public BWShopInventory(HyriBedWars plugin, Player owner, EHyriBWShopNavBar category) {
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

    private void initPage(){
        int i = 19;
        for (BWMaterial material : getMaterialByCategory(this.getCategory())) {
            if(i == 26 || i == 34) i += 2;
            final ItemShopStack itemShop = material.isUpgradable() ? this.getPlayer().getItemUpgradable(material) != null ? new ItemShopStack(material.getItemUpgradable().getNextTierItem()) : new ItemShopStack(material.getItemUpgradable().getTierItem(0)) : material.getItemShop();
            final boolean isMaxed = itemShop.getItem().isUpgradable() &&
                    this.getPlayer().hasUpgradeMaterial(itemShop.getItem().getHyriMaterial()) &&
                    this.getPlayer().getItemUpgradable(itemShop.getItem().getHyriMaterial()).isMaxed();

            this.setItem(i, material.getItemShop().getItemShop(this.getPlayer()), event -> {
                if(InventoryBWUtils.hasItems(this.owner, itemShop.getItem().getPrice()) && !isMaxed){

                    if(material.getCategory() == EHyriBWShopNavBar.ARMOR){
                        if(this.getPlayer().getPermanentArmor() != null && ((ArmorBW) this.getPlayer().getPermanentArmor().getItemShop().getItem()).getLevel() >= ((ArmorBW)itemShop.getItem()).getLevel()) {
                            owner.sendMessage("Vous ne pouvez plus prendre d'armure inferieur à la votre !");
                        }else{
                            this.getPlayer().giveArmor(material);
                            InventoryBWUtils.removeItems(this.owner, itemShop.getItem().getPrice());
                        }
                    }else {
                        if(material.isUpgradable()){
                            this.getPlayer().nextUpgradeItem(material);
                        }else {
                            if(material == BWMaterial.SHEARS){
                                if(this.getPlayer().hasPermanentShears()) {
                                    this.owner.sendMessage("Vous avez déjà cette item !");
                                    return;
                                }else {
                                    this.getPlayer().setPermanentShears(true);
                                }
                            }
                            if(itemShop.getItem().getHyriItem() != null){
                                this.plugin.getHyrame().getItemManager().giveItem(this.owner, itemShop.getItem().getHyriItem());
                            }else {
                                this.owner.getInventory().addItem(new ItemStack(itemShop.getItemStack()));
                            }
                        }

                        InventoryBWUtils.removeItems(this.owner, itemShop.getItem().getPrice());
                    }


                }else if(isMaxed){
                    this.owner.sendMessage("Vous avez tout upgrade !");
                }else{
                    this.owner.sendMessage("Vous n'avez pas ce qui faut !");
                }
                this.refreshGui();
            });
            i++;
        }
    }

    private void initNavBar(){
        int i = 0;
        for(EHyriBWShopNavBar item : EHyriBWShopNavBar.values()){
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

    private void openGui(EHyriBWShopNavBar category){
        try {
            if(category == EHyriBWShopNavBar.QUICK_BUY){
                new BWShopQuickBuy(this.plugin, this.owner).open();
                return;
            }
            new BWShopInventory(this.plugin, this.owner, category).open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected List<BWMaterial> getMaterialByCategory(EHyriBWShopNavBar category){
        return Arrays.stream(BWMaterial.values()).filter(material -> material.getCategory() == category)
                .collect(Collectors.toList());
    }

    public EHyriBWShopNavBar getCategory() {
        return category;
    }

    protected BWGamePlayer getPlayer(){
        return this.plugin.getGame().getPlayer(this.owner.getUniqueId());
    }

    private void refreshGui(){
        this.openGui(this.category);
    }
}
