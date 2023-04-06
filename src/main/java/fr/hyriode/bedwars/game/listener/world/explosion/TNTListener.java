package fr.hyriode.bedwars.game.listener.world.explosion;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

public class TNTListener extends HyriListener<HyriBedWars> {

    public TNTListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlacedTNT(BlockPlaceEvent event){
        Block block = event.getBlock();
        if(block.getType() == Material.TNT){
            block.setType(Material.AIR);
            IHyrame.WORLD.get().spawnEntity(block.getLocation().add(0.5D, 0.5D, 0.5D), EntityType.PRIMED_TNT);
        }
    }
}
