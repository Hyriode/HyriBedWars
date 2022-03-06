package fr.hyriode.bedwars.game.npc.inventory.shop;

import fr.hyriode.bedwars.game.npc.inventory.shop.material.BWMaterial;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.ItemShop;
import fr.hyriode.bedwars.game.team.upgrade.EBWUpgrades;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.npc.inventory.shop.pages.BWShopQuickBuy;
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
                final ItemShop itemShop = material.isItemUpgradable() ?
                        this.getPlayer().getItemUpgradable(material) != null ?
                                material.getItemUpgradable().getNextTierItem() :
                                material.getItemUpgradable().getTierItem(0) :
                        material.getItemShop();
                final boolean isMaxed = itemShop.isUpgradable() &&
                        this.getPlayer().hasUpgradeMaterial(itemShop.getHyriMaterial()) &&
                        this.getPlayer().getItemUpgradable(itemShop.getHyriMaterial()).isMaxed();

                this.setItem(i, material.getItemShop().getItemForShop(this.getPlayer()), event -> {
                    if (InventoryBWUtils.hasItems(this.owner, itemShop.getPrice()) && !isMaxed) {
                        if (material.isArmor()) {
                            if (this.getPlayer().getPermanentArmor() != null && this.getPlayer().getPermanentArmor().getArmor().getLevel() >= ((ArmorBW) itemShop).getLevel()) {
                                owner.sendMessage("Vous ne pouvez plus prendre d'armure inferieur à la votre !");
                            } else {
                                this.getPlayer().giveArmor(material);
                                this.getPlayer().setUpgradesTeam(EBWUpgrades.PROTECTION_ARMOR.getUpgrade());
                                InventoryBWUtils.removeItems(this.owner, itemShop.getPrice());
                            }
                        } else {
                            if (material.isItemUpgradable()) {
                                this.getPlayer().nextUpgradeItem(material);
                            } else {
                                if (material.getItemShop().isPermanent()) {
                                    if (this.getPlayer().hasPermanentItem(material)) {
                                        this.owner.sendMessage("Vous avez déjà cette item !");
                                        return;
                                    } else {
                                        this.getPlayer().addPermanentItem(material);
                                    }
                                }
                                if (material.isHyriItem()) {
                                    this.plugin.getHyrame().getItemManager().giveItem(this.owner, material.getHyriItem());
                                } else {
                                    if (material == BWMaterial.WOOL) {
                                        this.owner.getInventory().addItem(
                                                new ItemBuilder(itemShop.getItemStack().getType(),
                                                        itemShop.getItemStack().getAmount(),
                                                        this.getPlayer().getTeam().getColor().getDyeColor().getWoolData()).build());
                                    } else if ((material == BWMaterial.DIAMOND_SWORD || material == BWMaterial.IRON_SWORD || material == BWMaterial.STONE_SWORD) && InventoryBWUtils.hasItems(this.owner, new ItemStack(Material.WOOD_SWORD))) {
                                        if (InventoryBWUtils.hasItems(this.owner, new ItemStack(Material.WOOD_SWORD))) {
                                            InventoryBWUtils.setItemsSlot(this.owner, slot -> material.getItemShop().getItemStack(),
                                                    new ItemStack(Material.WOOD_SWORD));
                                        }
                                    } else {
                                        this.owner.getInventory().addItem(new ItemStack(itemShop.getItemStack()));
                                    }
                                }
                            }

                            InventoryBWUtils.removeItems(this.owner, itemShop.getPrice());
                        }


                    } else if (isMaxed) {
                        this.owner.sendMessage("Vous avez tout upgrade !");
                    } else {
                        this.owner.sendMessage("Vous n'avez pas ce qui faut !");
                    }
                    this.refreshGui();
                });
            }catch (Exception e){
                e.printStackTrace();
            }
            i++;
        }
    }

    private void initNavBar(){
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

    protected BWGamePlayer getPlayer(){
        return this.plugin.getGame().getPlayer(this.owner.getUniqueId());
    }

    private void refreshGui(){
        this.openGui(this.category);
    }
}
