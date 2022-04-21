package fr.hyriode.bedwars.game.material.utility;

import fr.hyriode.bedwars.game.material.BWMaterial;
import fr.hyriode.bedwars.game.material.BWMaterials;
import fr.hyriode.bedwars.game.material.HyriShopItem;
import fr.hyriode.bedwars.game.material.ItemHyriShop;
import fr.hyriode.bedwars.game.material.utility.entity.Despawnable;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.material.utility.entity.DreamDefenderEntity;
import fr.hyriode.bedwars.utils.InventoryBWUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class DreamDefenderEgg extends HyriShopItem<HyriBedWars> {
    public DreamDefenderEgg(HyriBedWars plugin) {
        super(plugin, BWMaterial.DREAM_DEFENDER);
    }

    @Override
    public void onRightClick(IHyrame hyrame, PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(event.getPlayer().getGameMode() != GameMode.CREATIVE)
                InventoryBWUtils.removeItem(event.getPlayer(), event.getItem());
            DreamDefenderEntity.spawn(event.getClickedBlock().getLocation().add(0, 1, 0), this.plugin, this.plugin.getGame().getPlayer(player).getHyriTeam());
        }
    }
}
