package fr.hyriode.bedwars.game.gui.shop;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.api.player.BWPlayerData;
import fr.hyriode.bedwars.api.player.style.HyriGameStyle;
import fr.hyriode.bedwars.game.gui.BWGui;
import fr.hyriode.bedwars.game.gui.manager.GuiManager;
import fr.hyriode.bedwars.game.gui.pattern.DefaultGuiPattern;
import fr.hyriode.bedwars.game.gui.pattern.GuiPattern;
import fr.hyriode.bedwars.game.gui.shop.hotbar.HotBarManagerGui;
import fr.hyriode.bedwars.game.gui.shop.quickbuy.ChoiceSlotGui;
import fr.hyriode.bedwars.game.gui.shop.tracker.TrackerGui;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.shop.*;
import fr.hyriode.bedwars.game.shop.material.MaterialShop;
import fr.hyriode.bedwars.game.waiting.BWGamePlayItem;
import fr.hyriode.bedwars.host.BWShopValues;
import fr.hyriode.bedwars.utils.InventoryUtils;
import fr.hyriode.bedwars.utils.SoundUtils;
import fr.hyriode.bedwars.utils.StringUtils;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.api.language.HyriLanguageMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ShopGui extends BWGui {

    private final ShopCategory category;

    public ShopGui(Player owner, HyriBedWars plugin, ShopCategory category, BWGui backGui) {
        super(owner, plugin, category.getTitleLanguage(owner), TypeSize.LINE_6, backGui, false);
        this.category = category;
        this.initGui();
    }

    @Override
    protected void initGui() {
        final BWGamePlayer player = this.getPlayer();
        final BWPlayerData account = player.getAccount();

        final GuiPattern pattern = DefaultGuiPattern.valueOf(account.getGameStyle().name()).getGuiPattern(this);
        final GuiPattern.Size size = pattern.getSize();

        pattern.initGui();

        final Map<Integer, MaterialShop> materials = this.category != ShopCategory.QUICK_BUY
                ? HyriBedWars.getShopManager().getItemShopByCategoryToMap(true, 0, this.category)
                : account.getQuickBuyShop();

        if(this.category == ShopCategory.MELEE && account.getGameStyle() == HyriGameStyle.HYRIODE){
            materials.putAll(HyriBedWars.getShopManager().getItemShopByCategoryToMap(true, materials.size() + 3, ShopCategory.RANGED));
        }

        int i = 0;
        for(int y = 0; y < size.getHeight(); ++y){
            for(int x = 0; x < size.getWidth(); ++x){
                int slot = i;
                i++;
                if(materials.containsKey(slot)) {
                    final MaterialShop material = materials.get(slot);
                    if(material == null) continue;
                    final ItemShop itemShop = material.getItemShopForPlayer(player);
                    //TODO fix en host les items disable en verre rouge pour le theme hypixel
                    this.setItem(size.getStartX() + x, size.getStartY() + y, itemShop.getForShop(player, this.category), event -> {
                        if(event.getClick().isShiftClick() && event.getClick().isRightClick()){
                            if(this.category != ShopCategory.QUICK_BUY){
                                new ChoiceSlotGui(this.owner, this.plugin, material, this).open();
                                return;
                            }
                            account.removeSlotQuickBuy(slot);
                            this.refresh();
                            return;
                        }
                        if(itemShop.getItem().getType() == Material.POTION && !BWShopValues.POTIONS_ENABLE.get()) {
                            this.owner.sendMessage(ChatColor.RED + HyriLanguageMessage.get("potion.disabled").getValue(this.owner));
                            SoundUtils.playCantBuy(player.getPlayer());
                            return;
                        }
                        if(player.isFullInventory()){
                            player.getPlayer().sendMessage(ChatColor.RED + HyriLanguageMessage.get("shop.inventory.full").getValue(this.owner));
                        } else if (material.isArmor() && player.getArmor() != null
                                && player.getArmor().getLevel() >= material.getAsArmor().getLevel()) {
                            this.owner.sendMessage(ChatColor.RED + HyriLanguageMessage.get("shop.armor.lower").getValue(this.owner));
                        }else if (player.getItemsPermanent().contains(material)) {
                            this.owner.sendMessage(ChatColor.RED + HyriLanguageMessage.get("shop.item.unlocked").getValue(this.owner));
                        } else if (player.containsMaterialUpgrade(material) && player.getMaterialUpgrade(material).isMaxed()) {
                            this.owner.sendMessage(ChatColor.RED + HyriLanguageMessage.get("shop.item.maxed").getValue(this.owner));
                        }else if (itemShop.hasPrice(this.owner)) {
                            InventoryUtils.removeMoney(this.owner, itemShop.getPrice(), itemShop.getPriceAmount());
                            material.buy(event, player);
                            SoundUtils.playBuy(this.owner);
                            this.owner.sendMessage(ChatColor.GREEN + HyriLanguageMessage.get("shop.purchased").getValue(this.owner).replace("%item%", ChatColor.GOLD + itemShop.getDisplayName().getValue(this.owner)));
                            this.refresh();
                            return;
                        } else {
                            this.owner.sendMessage(ChatColor.RED + HyriLanguageMessage.get("shop.missing").getValue(this.owner)
                                    .replace("%name%", itemShop.getPrice().getName(this.owner)).replace("%amount%", InventoryUtils.getHasPrice(this.owner, itemShop.getPrice(), itemShop.getPriceAmount()) + ""));
                        }
                        SoundUtils.playCantBuy(owner);
                    });
                    continue;
                }
                if(this.category == ShopCategory.QUICK_BUY && this.getPlayer().getAccount().getGameStyle() == HyriGameStyle.HYPIXEL){
                    this.setItem(size.getStartX() + x, size.getStartY() + y, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 14).build());
                }
            }
        }

        this.setItem(9, 5, BWGamePlayItem.getItemStack(player), event -> {
            player.getAccount().changeGamePlayStyle();
            this.refresh();
        });

        this.setItem(9, 6, this.getItemHotbar(), event -> {
            new HotBarManagerGui(this.owner, this.plugin, null, this).open();
        });

        this.setItem(1, 6, this.getItemTraker(), event -> {
            new TrackerGui(this.owner, this.plugin, this).open();
        });
    }

    private ItemStack getItemHotbar(){
        return new ItemBuilder(Material.BLAZE_POWDER)
                .withName(ChatColor.GOLD + HyriLanguageMessage.get("shop.hotbar-manager.name").getValue(this.owner))
                .withLore(StringUtils.loreToList(HyriLanguageMessage.get("shop.hotbar-manager.lore").getValue(this.owner)))
                .build();
    }

    private ItemStack getItemTraker(){
        return new ItemBuilder(Material.COMPASS)
                .withName(ChatColor.GREEN + HyriLanguageMessage.get("shop.tracker.name").getValue(this.owner))
                .withLore(StringUtils.loreToList(HyriLanguageMessage.get("shop.tracker.lore").getValue(this.owner)))
                .build();
    }

    @Override
    protected void refresh() {
        GuiManager.openShopGui(this, this.category);
    }

    public ShopCategory getCategory() {
        return category;
    }
}
