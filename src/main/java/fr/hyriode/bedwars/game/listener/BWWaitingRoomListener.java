package fr.hyriode.bedwars.game.listener;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.hyrame.game.HyriGameState;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class BWWaitingRoomListener extends HyriListener<HyriBedWars> {

    public BWWaitingRoomListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler
    public void onWaiting(PlayerMoveEvent event){
        Player player = event.getPlayer();
        Location loc = this.plugin.getConfiguration().getWaitingRoom().getWaitingSpawn();
        if((this.plugin.getGame().getState() == HyriGameState.READY || this.plugin.getGame().getState() == HyriGameState.WAITING)
                && player.getLocation().getY() <= loc.getY() - 10){
            player.teleport(loc);
        }
    }

}
