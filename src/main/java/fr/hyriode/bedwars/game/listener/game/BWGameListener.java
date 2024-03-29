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
        final BWGameTeam team = player.getBWTeam();

        if (team != null && team.isEliminated()) {
            event.disallow();
        }
    }

    @HyriEventHandler
    public void onReconnected(HyriGameReconnectedEvent event) {
        final BWGamePlayer player = (BWGamePlayer) event.getGamePlayer();
        player.getPlayer().getInventory().clear();

        this.plugin.getGame().getProtocolManager().getProtocol(HyriDeathProtocol.class)
                .runDeath(HyriGameDeathEvent.Reason.VOID, player.getPlayer());

        BWPlayerScoreboard scoreboard = new BWPlayerScoreboard(this.plugin, player.getPlayer());
        player.setScoreboard(scoreboard);
        scoreboard.show();
    }

    @HyriEventHandler
    public void onSpectator(HyriGameSpectatorEvent event) {
        final HyriGameSpectator spectator = event.getSpectator();
        final Player player = spectator.getPlayer();

        if (!(spectator instanceof HyriGamePlayer)) { // Player is an outside spectator
            player.teleport(this.plugin.getConfiguration().getWaitingRoom().getSpawn().asBukkit());

            BWGame game = this.plugin.getGame();
            for (BWGameTeam bwTeam : game.getBWTeams()) {
                game.getNPCShop(bwTeam.getConfig().getShopNPCLocation(), player);
                game.getNPCUpgrade(bwTeam.getConfig().getUpgradeNPCLocation(), player);
            }

            new BWPlayerScoreboard(this.plugin, player).show();
        }
    }
}
