package fr.hyriode.bedwars.game.player.scoreboard;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGame;
import fr.hyriode.bedwars.game.BWNextEvent;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.game.type.BWGameType;
import fr.hyriode.bedwars.utils.StringUtils;
import fr.hyriode.hyrame.game.scoreboard.HyriGameScoreboard;
import fr.hyriode.hyrame.game.scoreboard.HyriScoreboardIpConsumer;
import fr.hyriode.hyrame.game.team.HyriGameTeam;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.hyrame.scoreboard.HyriScoreboard;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class BWPlayerScoreboard extends HyriGameScoreboard<BWGame> {

    private final HyriBedWars plugin;
    private final BWGame game;
    private final BWGameType gameType;

    public BWPlayerScoreboard(HyriBedWars plugin, BWGame game, Player player) {
        super(plugin, game, player.getPlayer(), "sbbedwars");
        this.plugin = plugin;
        this.gameType = plugin.getGame().getType();
        this.game = this.plugin.getGame();

        int i = 0;
        this.setLine(i++, this.getDateLine(), line -> line.setValue(this.getDateLine()), 20);
        this.setLine(i++, "§1");
        this.setLine(i++, this.getCurrentEvent(), line -> line.setValue(this.getCurrentEvent()), 1);
        this.setLine(i++, "§2");
        i = this.addTeamsLines(i);

        this.setLine(i, ChatColor.DARK_AQUA + "hyriode.fr", new HyriScoreboardIpConsumer("hyriode.fr"), 2);
    }

    private int addTeamsLines(int i) {
        for (HyriGameTeam team : this.game.getTeams()) {
            if(team != null) {
                this.setLine(i++, this.getTeamLine((BWGameTeam) team), line -> line.setValue(this.getTeamLine((BWGameTeam) team)), 1);
            }
        }
        BWGamePlayer player = this.getGamePlayer();
        this.setLine(i++, "§3");
        if (this.gameType.getMaxTeams() <= 4) {
            this.setLine(i++, this.getLinePrefix("kills") + " " + ChatColor.AQUA + player.getKills(), line -> line.setValue(this.getLinePrefix("kills") + " " + ChatColor.AQUA + player.getKills()), 1);
            this.setLine(i++, this.getLinePrefix("finalkills") + " " + ChatColor.AQUA + player.getFinalKills(), line -> line.setValue(this.getLinePrefix("finalkills") + " " + ChatColor.AQUA + player.getFinalKills()), 1);
            if (this.gameType.getMaxTeams() > 2) {
                this.setLine(i++, this.getLinePrefix("bedsbroken") + " " + ChatColor.AQUA + player.getBedsBroken(), line -> line.setValue(this.getLinePrefix("bedsbroken") + " " + ChatColor.AQUA + player.getBedsBroken()), 1);
            }
            this.setLine(i++, "§4");
        }
        return i;
    }

    private String getCurrentEvent(){
        final BWNextEvent currentNextEvent = this.game.getCurrentEvent();
        long timeBeforeEvent = currentNextEvent.getNextEvent() != null ? currentNextEvent.getNextEvent().getTimeBeforeEvent() : 0;
        long timeSecond = timeBeforeEvent - this.game.getTask().getTime();

        if (currentNextEvent.getNextEvent() != null)
            return currentNextEvent.getNextEvent().get().getForPlayer(this.player)
                    + " " + this.getLinePrefix("in") + " " + StringUtils.formatTime(timeSecond);
        else
            return this.getLinePrefix("game_end");
    }

    private String getTeamLine(BWGameTeam team) {
        return team.getColor().getChatColor() + team.getDisplayName().getForPlayer(player) + ChatColor.RESET + " » " + this.getStateAsSymbol(team) + (this.game.getPlayer(player.getUniqueId()).isInTeam(team) ? " " + this.getLinePrefix("you") : "");
    }

    public String getStateAsSymbol(BWGameTeam team) {
        if(team.isEliminated()) {
            return ChatColor.RED + "✘";
        } else if(team.hasBed()) {
            return ChatColor.GREEN + "✔";
        } else {
            return ChatColor.GREEN + String.valueOf(team.getPlayersPlaying().size());
        }
    }

    private String getDateLine() {
        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        format.setTimeZone(TimeZone.getTimeZone("GMT+1"));

        return ChatColor.GRAY + format.format(new Date());
    }

    private String getLinePrefix(String prefix) {
        return HyriLanguageMessage.get("scoreboard." + prefix + ".display").getForPlayer(this.player);
    }

    private BWGamePlayer getGamePlayer(){
        return this.plugin.getGame().getPlayer(this.player);
    }
}
