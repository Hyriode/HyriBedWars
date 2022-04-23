package fr.hyriode.bedwars.game.scoreboard;

import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.BWGameType;
import fr.hyriode.bedwars.game.event.BWNextEvent;
import fr.hyriode.hyrame.game.HyriGameState;
import fr.hyriode.hyrame.game.scoreboard.HyriGameScoreboard;
import fr.hyriode.hyrame.game.scoreboard.HyriScoreboardIpConsumer;
import fr.hyriode.hyrame.game.team.HyriGameTeam;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGame;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.utils.StringBWUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class BWGameScoreboard extends HyriGameScoreboard<BWGame> {

    private final HyriBedWars plugin;
    private final BWGame game;
    private final BWGameType gameType;

    public BWGameScoreboard(HyriBedWars plugin, Player player) {
        super(plugin, plugin.getGame(), player, "bedwars");
        this.plugin = plugin;
        this.gameType = (BWGameType) plugin.getGame().getType();
        this.game = this.plugin.getGame();

        this.setLine(0, this.getDateLine(), line -> line.setValue(this.getDateLine()), 20);
        this.setLine(1, "§1");
        this.setLine(2, this.getActualEvent(), line -> line.setValue(this.getActualEvent()), 10);
        this.setLine(3, "§2");

        int i = this.addTeamsLines();

        this.setLine(i, ChatColor.DARK_AQUA + "hyriode.fr", new HyriScoreboardIpConsumer("hyriode.fr"), 2);
    }

    private int addTeamsLines() {
        int i = 4;
        for (HyriGameTeam team : this.game.getTeams()) {
            this.setLine(i, this.getTeamLine((BWGameTeam) team));
            i += 1;
        }
        this.setLine(i, "§3");
        if(this.gameType.getMaxTeams() <= 4){
            this.setLine(++i, this.getLinePrefix("kills") + " " + ChatColor.AQUA + this.getGamePlayer().getKills());
            this.setLine(++i, this.getLinePrefix("finalkills") + " " + ChatColor.AQUA + this.getGamePlayer().getFinalKills());
            System.out.println(this.gameType);
            System.out.println(this.gameType.getMaxTeams());
            if(this.gameType.getMaxTeams() > 2) {
                this.setLine(++i, this.getLinePrefix("bedsbroken") + " " + ChatColor.AQUA + this.getGamePlayer().getBedsBroken());
            }
            this.setLine(++i, "§4");
        }
        return i + 1;
    }

    public void update(){
        this.addTeamsLines();
        this.updateLines();
    }

    private String getTeamLine(BWGameTeam team) {
        return team.getColor().getChatColor() + team.getDisplayName().getForPlayer(player) + ChatColor.RESET + " » " + team.getStateAsSymbol() + (this.game.getPlayer(player.getUniqueId()).isInTeam(team) ? " " + this.getLinePrefix("you") : "");
    }

    private String getActualEvent(){
        final BWNextEvent currentNextEvent = this.game.getActualEvent();
        long timeBeforeEvent = currentNextEvent.getNextEvent() != null ? currentNextEvent.getNextEvent().getTimeBeforeEvent() : 0;
        long timeSecond = timeBeforeEvent - this.game.getTask().getTime();

        if (currentNextEvent.getNextEvent() != null)
            return currentNextEvent.getNextEvent().get().getForPlayer(this.player)
                    + " in " + StringBWUtils.formatTime(timeSecond);
        else
            return "Game down";
    }

    private String getDateLine() {
        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        format.setTimeZone(TimeZone.getTimeZone("GMT+1"));

        return ChatColor.GRAY + format.format(new Date());
    }

    private String getLinePrefix(String prefix) {
        return HyriBedWars.getLanguageManager().getValue(this.player, "scoreboard." + prefix + ".display");
    }

    private BWGamePlayer getGamePlayer(){
        return this.plugin.getGame().getPlayer(this.player);
    }


}
