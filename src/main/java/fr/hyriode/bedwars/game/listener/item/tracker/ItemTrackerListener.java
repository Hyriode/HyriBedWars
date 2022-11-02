package fr.hyriode.bedwars.game.listener.item.tracker;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.gui.shop.tracker.TrackerGui;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.hyrame.item.ItemNBT;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ItemTrackerListener extends HyriListener<HyriBedWars> {

    public ItemTrackerListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();

        if(MetadataReferences.isMetaItem(MetadataReferences.COMPASS, itemStack)) {
            if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                new TrackerGui(player, this.plugin, null).open();
            }
        }
    }
}
