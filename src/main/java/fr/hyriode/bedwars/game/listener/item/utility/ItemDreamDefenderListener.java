package fr.hyriode.bedwars.game.listener.item.utility;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.entity.models.DreamDefenderEntity;
import fr.hyriode.bedwars.utils.InventoryUtils;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ItemDreamDefenderListener extends HyriListener<HyriBedWars> {

    public ItemDreamDefenderListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler
    public void onSpawn(PlayerInteractEvent event){
        ItemStack itemStack = event.getItem();
        Player player = event.getPlayer();
        if(!MetadataReferences.isMetaItem(MetadataReferences.DREAM_DEFENDER, itemStack)) return;
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            DreamDefenderEntity.spawn(event.getClickedBlock().getLocation().add(0.5, 1, 0.5), this.plugin,
                    this.plugin.getGame().getPlayer(player).getBWTeam());
            InventoryUtils.remove(player, itemStack);
        }
    }
}
