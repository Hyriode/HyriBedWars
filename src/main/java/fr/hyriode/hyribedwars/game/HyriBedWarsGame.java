package fr.hyriode.hyribedwars.game;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.game.HyriGame;
import fr.hyriode.hyrame.game.team.HyriGameTeam;
import fr.hyriode.hyribedwars.HyriBedWars;
import fr.hyriode.hyribedwars.configuration.HyriBedWarsConfiguration;
import fr.hyriode.hyribedwars.game.event.EHyriBedWarsNextEvent;
import fr.hyriode.hyribedwars.game.tasks.HyriBedWarsMainTask;
import fr.hyriode.hyribedwars.game.team.EHyriBedWarsGameTeam;
import fr.hyriode.hyribedwars.game.team.HyriBedWarsGameTeam;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HyriBedWarsGame extends HyriGame<HyriBedWarsGamePlayer> {

    private final HyriBedWars plugin;
    private HyriBedWarsGameType gameType;
    private EHyriBedWarsNextEvent actualEvent;

    public HyriBedWarsGame(IHyrame hyrame, HyriBedWars plugin) {
        super(hyrame, plugin, "hyribedwars", HyriBedWars.NAME, HyriBedWarsGamePlayer.class);

        this.plugin = plugin;
        this.gameType = HyriBedWarsGameType.SOLO;

        this.minPlayers = 4;
        this.maxPlayers = 8;

        this.registerTeams();
    }

    private void registerTeams() {
        if (gameType.equals(HyriBedWarsGameType.SOLO) || gameType.equals(HyriBedWarsGameType.DOUBLES)) {

            for (EHyriBedWarsGameTeam gameTeam : EHyriBedWarsGameTeam.values()) {
                this.registerTeam(new HyriBedWarsGameTeam(gameTeam, this.gameType.getTeamSize(), this.plugin));
            }

        } else {

            this.registerTeam(new HyriBedWarsGameTeam(EHyriBedWarsGameTeam.RED, this.gameType.getTeamSize(), this.plugin));
            this.registerTeam(new HyriBedWarsGameTeam(EHyriBedWarsGameTeam.BLUE, this.gameType.getTeamSize(), this.plugin));
            this.registerTeam(new HyriBedWarsGameTeam(EHyriBedWarsGameTeam.YELLOW, this.gameType.getTeamSize(), this.plugin));
            this.registerTeam(new HyriBedWarsGameTeam(EHyriBedWarsGameTeam.GREEN, this.gameType.getTeamSize(), this.plugin));

        }
    }

    @Override
    public void handleLogin(Player p) {
        super.handleLogin(p);
    }

    @Override
    public void handleLogout(Player p) {
        super.handleLogout(p);
    }

    @Override
    public void start() {
        super.start();

        this.teleportTeams();

        new HyriBedWarsMainTask(this.plugin, this.gameTime).runTaskTimerAsynchronously(this.plugin, 0, 20);
    }

    private void teleportTeams() {


    }

    @Override
    public void win(HyriGameTeam winner) {
        super.win(winner);
    }

    public HyriGameTeam getWinner() {
        final List<HyriGameTeam> teamsAlive = new ArrayList<>(this.teams);
        HyriGameTeam winner = null;

        for (HyriGameTeam team : this.teams) {
            HyriBedWarsGameTeam gameTeam = (HyriBedWarsGameTeam) team;

            if (gameTeam.isEliminated()) {
                teamsAlive.remove(team);
            }

            if (teams.size() == 1) {
                winner = teamsAlive.get(0);
            }
        }
        return winner;
    }

    public EHyriBedWarsNextEvent getActualEvent() {
        return actualEvent;
    }

    public void setActualEvent(EHyriBedWarsNextEvent actualEvent) {
        this.actualEvent = actualEvent;
    }
}
