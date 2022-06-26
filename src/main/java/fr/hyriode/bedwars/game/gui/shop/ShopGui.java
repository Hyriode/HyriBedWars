package fr.hyriode.bedwars.game.gui.shop;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.api.player.HyriBWPlayer;
import fr.hyriode.bedwars.api.player.style.HyriGameStyle;
import fr.hyriode.bedwars.game.gui.BWGui;
import fr.hyriode.bedwars.game.gui.manager.GuiManager;
import fr.hyriode.bedwars.game.gui.pattern.DefaultGuiPattern;
import fr.hyriode.bedwars.game.gui.pattern.GuiPattern;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.shop.*;
import fr.hyriode.bedwars.game.waiting.BWGamePlayItem;
import fr.hyriode.bedwars.utils.InventoryUtils;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ShopGui extends BWGui {

    private final ShopCategory category;

    public ShopGui(Player owner, HyriBedWars plugin, ShopCategory category) {
        super(owner, plugin, category.getTitleLanguage(owner), TypeSize.LINE_6);
        this.category = category;
        this.initGui();
    }

    @Override
    protected void initGui() {
        final BWGamePlayer player = this.getPlayer();
        final HyriBWPlayer account = player.getAccount();

        final GuiPattern pattern = DefaultGuiPattern.valueOf(account.getGameStyle().name()).getGuiPattern(this);
        final GuiPattern.Size size = pattern.getSize();

        pattern.initGui();

        final Map<Integer, MaterialShop> materials = this.category != ShopCategory.QUICK_BUY
                ? HyriBedWars.getShopManager().getItemShopByCategoryToMap(0, this.category)
                : account.getQuickBuyShop();

        if(this.category == ShopCategory.MELEE && account.getGameStyle() == HyriGameStyle.HYRIODE){
            materials.put(materials.size() + 1, new FakeMaterialShop());
            materials.put(materials.size() + 1, new FakeMaterialShop());
            materials.put(materials.size() + 1, new FakeMaterialShop());
            materials.putAll(HyriBedWars.getShopManager().getItemShopByCategoryToMap(materials.size(), ShopCategory.RANGED));
        }

        int i = 0;
        for(int y = 0; y < size.getHeight(); ++y){
            for(int x = 0; x < size.getWidth(); ++x){
                if(materials.size() >= i) {
                    final MaterialShop material = materials.get(i++);
                    if(material == null) continue;
                    final ItemShop itemShop = this.getItemShop(material);
                    this.setItem(size.getStartX() + x, size.getStartY() + y, itemShop.getForShop(player), event -> {
                        if (material.isFake()) {
                            return;
                        }

                        if(event.getClick().isShiftClick() && event.getClick().isRightClick()){
                            System.out.println("AMAZING");
                            return;
                        }

                        if (material.isArmor() && player.getArmor() != null
                                && player.getArmor().getLevel() >= material.getAsArmor().getLevel()) {
                            this.owner.sendMessage("LSQKMJD");
                            return;
                        }
                        if (player.getItemsPermanent().contains(material)) {
                            this.owner.sendMessage("Vous avez déjà débloqué cet item");
                            return;
                        }
                        if (player.containsMaterialUpgrade(material) && player.getMaterialUpgrade(material).isMaxed()) {
                            this.owner.sendMessage("Vous avez déjà votre item au max");
                            return;
                        }
                        if (itemShop.hasPrice(this.owner)) {
                            InventoryUtils.removeMoney(this.owner, itemShop.getPrice());
                            material.buy(player);
                            this.owner.playSound(owner.getLocation(), Sound.NOTE_PLING, 0.8F, 2.0F);
                            this.owner.sendMessage(ChatColor.GREEN + String.format(HyriLanguageMessage.get("shop.purchased").getForPlayer(this.owner), ChatColor.GOLD + itemShop.getDisplayName().getForPlayer(this.owner)));
                            this.refresh();
                            return;
                        }
                        owner.playSound(owner.getLocation(), Sound.ENDERMAN_TELEPORT, 0.8F, 0.1F);
                        this.owner.sendMessage(ChatColor.RED + String.format(HyriLanguageMessage.get("shop.missing").getForPlayer(this.owner), itemShop.getPrice().getName(this.owner), InventoryUtils.getHasPrice(this.owner, itemShop.getPrice())));
                    });
                    continue;
                }
                if(this.category == ShopCategory.QUICK_BUY && this.getPlayer().getAccount().getGameStyle() == HyriGameStyle.HYPIXEL){
                    this.setItem(size.getStartX() + x, size.getStartY() + y, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 14).build());
                }
            }
        }

        this.setItem(9, 5, BWGamePlayItem.getItemStack(player), event -> {
            player.changeGamePlayStyle();
            this.refresh();
        });
    }

    private ItemShop getItemShop(MaterialShop material){
        if (material.isUpgradable() && this.getPlayer().containsMaterialUpgrade(material)) {
            UpgradeMaterial m = this.getPlayer().getMaterialUpgrade(material);
            return m.getItemShopByNextTier();
        }
        return material.getFirstItem();
    }

    @Override
    protected void refresh() {
        GuiManager.openShopGui(this, this.category);
    }

    public ShopCategory getCategory() {
        return category;
    }
}
