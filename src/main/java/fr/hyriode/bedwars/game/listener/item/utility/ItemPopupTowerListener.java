package fr.hyriode.bedwars.game.listener.item.utility;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.shop.utility.tower.TowerManager;
import fr.hyriode.bedwars.utils.InventoryUtils;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

public class ItemPopupTowerListener extends HyriListener<HyriBedWars> {

    public ItemPopupTowerListener(HyriBedWars plugin) {
        super(plugin);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        if(MetadataReferences.isMetaItem(MetadataReferences.POPUP_TOWER, event.getItemInHand())){
            event.setBuild(false);
            TowerManager.placeTower(this.plugin, event.getBlock(), this.plugin.getGame().getPlayer(event.getPlayer()),
                    TowerManager.Direction.getDirection(event.getBlock().getState().getData().getData()));
            if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                InventoryUtils.removeInHand(event.getPlayer());
            }
        }
    }
}
