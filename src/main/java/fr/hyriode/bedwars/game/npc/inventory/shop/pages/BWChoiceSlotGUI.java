package fr.hyriode.bedwars.game.npc.inventory.shop.pages;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.api.player.HyriBWPlayer;
import fr.hyriode.bedwars.api.player.HyriGameStyle;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.material.BWMaterial;
import fr.hyriode.bedwars.game.material.ItemEmptySlot;
import fr.hyriode.bedwars.game.material.ItemShop;
import fr.hyriode.hyrame.inventory.HyriInventory;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

//For Quick Buy
public class BWChoiceSlotGUI extends HyriInventory {

    private final HyriBedWars plugin;
    private final BWMaterial material;
    private final HyriGameStyle gameStyle;

    public BWChoiceSlotGUI(HyriBedWars plugin, Player owner, BWMaterial material, boolean isHyriode) {
        this(plugin, owner, material, isHyriode ? HyriGameStyle.HYRIODE : HyriGameStyle.HYPIXEL);
    }

    public BWChoiceSlotGUI(HyriBedWars plugin, Player owner, BWMaterial material, HyriGameStyle gameStyle) {
        super(owner, HyriBedWars.getLanguageManager().getValue(owner, "inv.choice_slot.title"), 54);
        this.plugin = plugin;
        this.material = material;
        this.gameStyle = gameStyle;
        this.initGui();
    }

    protected void initGui() {
        ItemShop itemShop = this.material.isItemUpgradable() ? this.material.getItemUpgradable().getTierItem(0) : this.material.getItemShop();

        this.setItem(4, itemShop
                .getItemToPlace(this.getPlayer()));

        final BWGamePlayer player = this.getPlayer();

        for(int slot = 19 ; ;++slot){
            if(slot > 43)
                break;
            if(slot <= 25 || slot >= 28 && slot <= 34 || slot >= 37) {
                final int finalSlot = slot;
                this.setItem(slot, new ItemEmptySlot().getItemToReplace(this.getPlayer()), event -> {
                    final HyriBWPlayer account = this.getPlayer().getAccount();

                    account.putMaterialQuickBuy(finalSlot, this.material.name());
                    account.update(this.getPlayer().getUUID());

                    new BWShopQuickBuy(this.plugin, this.owner).open();
                });
            }
        }
        Map<String, Integer> quickBuy = this.getPlayer().getAccount().getQuickBuy();
        for(String material : quickBuy.keySet()){
            ItemShop item = BWMaterial.valueOf(material).isItemUpgradable() ? BWMaterial.valueOf(material).getItemUpgradable().getTierItem(0) : BWMaterial.valueOf(material).getItemShop();

            int slot = quickBuy.get(material);
            this.setItem(slot, item.getItemToReplace(player),
                    event -> {
                        final HyriBWPlayer account = this.getPlayer().getAccount();

                        account.putMaterialQuickBuy(slot, this.material.name());
                        account.update(this.getPlayer().getUUID());

                        new BWShopQuickBuy(this.plugin, this.owner).open();
                    });
        }
    }

    protected boolean isHyriode(){
        return this.gameStyle == HyriGameStyle.HYRIODE;
    }

    private BWGamePlayer getPlayer(){
        return this.plugin.getGame().getPlayer(this.owner.getUniqueId());
    }
}
