package fr.hyriode.bedwars.game.listener.world.entity;

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
        if(event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM) {
            event.setCancelled(false);
        }
    }
}
