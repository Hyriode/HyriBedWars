package fr.hyriode.bedwars.game.npc.inventory.shop.utility;

import fr.hyriode.bedwars.game.BWGameOre;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopCategory;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.HyriShopItem;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.OreStack;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.npc.inventory.shop.utility.entity.DreamDefenderEntity;
import fr.hyriode.bedwars.utils.InventoryBWUtils;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class DreamDefenderEgg extends HyriShopItem<HyriBedWars> {
    public DreamDefenderEgg(HyriBedWars plugin) {
        super(plugin, "dream_defender", Material.MONSTER_EGG, BWShopCategory.UTILITY, new OreStack(BWGameOre.IRON, 120));
    }

    @Override
    public void onRightClick(IHyrame hyrame, PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            InventoryBWUtils.removeItem(event.getPlayer(), event.getItem());
            DreamDefenderEntity.spawn(this.plugin, event.getClickedBlock().getLocation(), this.plugin.getGame().getPlayer(event.getPlayer().getUniqueId()).getTeam(), 1, 10, 120);
        }
    }
}
