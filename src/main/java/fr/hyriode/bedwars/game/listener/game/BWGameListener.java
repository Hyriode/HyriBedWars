package fr.hyriode.bedwars.game.listener.game;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.HyriEventHandler;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGame;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.player.scoreboard.BWPlayerScoreboard;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.hyrame.game.HyriGamePlayer;
import fr.hyriode.hyrame.game.HyriGameSpectator;
import fr.hyriode.hyrame.game.event.player.HyriGameDeathEvent;
import fr.hyriode.hyrame.game.event.player.HyriGameReconnectEvent;
import fr.hyriode.hyrame.game.event.player.HyriGameReconnectedEvent;
import fr.hyriode.hyrame.game.event.player.HyriGameSpectatorEvent;
import fr.hyriode.hyrame.game.protocol.HyriDeathProtocol;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.entity.Player;

public class BWGameListener extends HyriListener<HyriBedWars> {

    public BWGameListener(HyriBedWars plugin) {
        super(plugin);

        HyriAPI.get().getEventBus().register(this);
    }

    @HyriEventHandler
    public void onReconnect(HyriGameReconnectEvent event) {
        final BWGamePlayer player = (BWGamePlayer) event.getGamePlayer();
        final BWGameTeam team = (BWGameTeam) player.getTeam();

        if (team.hasBed()) {
            event.allow();
        } else {
            event.disallow();
        }
    }

    @HyriEventHandler
    public void onReconnected(HyriGameReconnectedEvent event) {
        final BWGamePlayer player = (BWGamePlayer) event.getGamePlayer();

        this.plugin.getGame().getProtocolManager().getProtocol(HyriDeathProtocol.class)
                .runDeath(HyriGameDeathEvent.Reason.VOID, player.getPlayer());
    }

    @HyriEventHandler
    public void onSpectator(HyriGameSpectatorEvent event) {
        final BWGame game = (BWGame) event.getGame();
        final HyriGameSpectator spectator = event.getSpectator();
        final Player player = spectator.getPlayer();

        if (!(spectator instanceof HyriGamePlayer)) { // Player is an outside spectator
            player.teleport(game.getWaitingRoom().getConfig().getSpawn().asBukkit());

            new BWPlayerScoreboard(this.plugin, player).show();
        }
    }
}
