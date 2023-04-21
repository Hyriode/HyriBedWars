package fr.hyriode.bedwars.game.listener.world.team.trap;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGame;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.hyrame.game.HyriGameState;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class TrapListener extends HyriListener<HyriBedWars> {

    public TrapListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler
    public void onTriggered(PlayerMoveEvent event){
        BWGame game = this.plugin.getGame();
        if(game == null || game.getState() != HyriGameState.PLAYING) return;
        BWGamePlayer player = game.getPlayer(event.getPlayer());
        if(player == null) return;
        BWGameTeam teamPlayer = player.getBWTeam();

        for (BWGameTeam team : game.getBWTeams()) {
            Player p = player.getPlayer();
            if(p != null && teamPlayer != null && !teamPlayer.equals(team)
                    && team.getConfig().getBaseArea().isInArea(p.getLocation())
                    && !player.hasCountdown(BWGamePlayer.TRAP_COUNTDOWN)){
                team.getTrapTeam().trap(player);
                break;
            }
        }
    }
}
