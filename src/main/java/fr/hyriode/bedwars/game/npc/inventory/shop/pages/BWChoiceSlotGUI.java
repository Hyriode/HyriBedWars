package fr.hyriode.bedwars.game.npc.inventory.shop.pages;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.api.player.HyriBWPlayer;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.material.BWMaterial;
import fr.hyriode.bedwars.game.material.ItemEmptySlot;
import fr.hyriode.hyrame.inventory.HyriInventory;
import org.bukkit.entity.Player;

//For Quick Buy
public class BWChoiceSlotGUI extends HyriInventory {

    private final HyriBedWars plugin;
    private final BWMaterial material;

    public BWChoiceSlotGUI(HyriBedWars plugin, Player owner, BWMaterial material) {
        super(owner, HyriBedWars.getLanguageManager().getValue(owner, "inv.choice_slot.title"), 54);
        this.plugin = plugin;
        this.material = material;
        this.initGui();
    }

    protected void initGui() {
        this.setItem(4, this.material.getItemShop()
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
                    this.plugin.getAPI().getPlayerManager().sendPlayer(account);

                    new BWShopQuickBuy(this.plugin, this.owner).open();
                });
            }
        }

        for(Integer slot : this.getPlayer().getAccount().getQuickBuy().keySet()){
            this.setItem(slot, BWMaterial.valueOf(this.getPlayer().getAccount().getQuickBuy().get(slot)).getItemShop().getItemToReplace(player),
                    event -> {
                        final HyriBWPlayer account = this.getPlayer().getAccount();

                        account.putMaterialQuickBuy(slot, this.material.name());
                        this.plugin.getAPI().getPlayerManager().sendPlayer(account);

                        new BWShopQuickBuy(this.plugin, this.owner).open();
                    });
        }
    }

    private BWGamePlayer getPlayer(){
        return this.plugin.getGame().getPlayer(this.owner.getUniqueId());
    }
}
