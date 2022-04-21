package fr.hyriode.bedwars.game.listener;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class BWCreatureListener extends HyriListener<HyriBedWars> {

    public BWCreatureListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler()
    public void onSpawn(CreatureSpawnEvent event){
        event.setCancelled(false);
    }

}
