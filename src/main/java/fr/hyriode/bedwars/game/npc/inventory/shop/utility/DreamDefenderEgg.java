package fr.hyriode.bedwars.game.npc.inventory.shop.utility;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.HyriItem;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.npc.inventory.shop.utility.entity.DreamDefenderEntity;
import fr.hyriode.bedwars.utils.InventoryBWUtils;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collections;

public class DreamDefenderEgg extends HyriItem<HyriBedWars> {
    public DreamDefenderEgg(HyriBedWars plugin) {
        super(plugin, "dream_defender", () -> HyriBedWars.getLanguageManager().getMessage("shop.item.dream_defender.name"), () -> Collections.singletonList(HyriBedWars.getLanguageManager().getMessage("shop.item.dream_defender.lore")), Material.MONSTER_EGG, (byte) 0);
    }

    @Override
    public void onRightClick(IHyrame hyrame, PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            InventoryBWUtils.removeItem(event.getPlayer(), event.getItem());
            DreamDefenderEntity.spawn(this.plugin, event.getClickedBlock().getLocation(), this.plugin.getGame().getPlayer(event.getPlayer().getUniqueId()).getTeam(), 1, 10, 120);
        }
    }
}
