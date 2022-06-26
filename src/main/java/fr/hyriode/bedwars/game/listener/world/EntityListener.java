package fr.hyriode.bedwars.game.listener.world;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class EntityListener extends HyriListener<HyriBedWars> {

    public EntityListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onSpawnCreature(CreatureSpawnEvent event){
        event.setCancelled(false);
    }
}
