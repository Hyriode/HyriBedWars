package fr.hyriode.bedwars.game.scoreboard;

import fr.hyriode.hyrame.game.scoreboard.HyriScoreboardIpConsumer;
import fr.hyriode.hyrame.game.team.HyriGameTeam;
import fr.hyriode.hyrame.scoreboard.Scoreboard;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGame;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.utils.StringBWUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class BWGameScoreboard extends Scoreboard {

    private final HyriBedWars plugin;
    private final BWGame game;

    public BWGameScoreboard(HyriBedWars plugin, Player player) {
        super(plugin, player, "bedwarsSB", ChatColor.DARK_AQUA + "     " + ChatColor.BOLD + plugin.getGame().getDisplayName() + "     ");
        this.plugin = plugin;
        this.game = this.plugin.getGame();

        this.addTeamsLines();

        this.setLine(0, this.getDateLine(), line -> line.setValue(this.getDateLine()), 20);
        this.setLine(1, "§1");
        this.setLine(2, this.getActualEvent(), line -> line.setValue(this.getActualEvent()), 20);
        this.setLine(3, "§2");
    }

    private void addTeamsLines() {
        int i = 4;
        for (HyriGameTeam team : this.game.getTeams()) {
            this.setLine(i, this.getTeamLine((BWGameTeam) team));
            i += 1;
        }
        this.setLine(i, "§3");
        this.setLine(i+1, ChatColor.DARK_AQUA + "hyriode.fr", new HyriScoreboardIpConsumer("hyriode.fr"), 2);
    }

    private String getTeamLine(BWGameTeam team) {
        if(this.game.getPlayer(player.getUniqueId()).isInTeam(team)) {
            return team.getDisplayName().getForPlayer(player) + ChatColor.RESET + " » " +team.getStateAsSymbol() + " " + this.getLinePrefix("you");
        }
        return team.getDisplayName().getForPlayer(player) + ChatColor.RESET + " » " +team.getStateAsSymbol();
    }

    private String getActualEvent(){
        long timeBeforeEvent = this.game.getActualEvent().getNextEvent() != null ?
                this.game.getActualEvent().getNextEvent().getTimeBeforeEvent() : 0;
        long timeSecond = timeBeforeEvent - this.game.getTask().getIndex();
        if(this.game.getActualEvent().getNextEvent() != null)
        return this.game.getActualEvent().getNextEvent().get().getForPlayer(this.player)
                + " in " + StringBWUtils.formatTime(timeSecond);
        else
            return "Game down";
    }

    private String getDateLine() {
        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        format.setTimeZone(TimeZone.getTimeZone("GMT+1"));

        return ChatColor.GRAY + format.format(new Date());
    }

    private String getTimeLine() {
        final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        format.setTimeZone(TimeZone.getTimeZone("GMT"));

        final String line = format.format(this.game.getGameTime() * 1000);

        return this.getLinePrefix("time") + ChatColor.AQUA + (line.startsWith("00:") ? line.substring(3) : line);
    }

    private String getLinePrefix(String prefix) {
        return HyriBedWars.getLanguageManager().getValue(this.player, "scoreboard." + prefix + ".display");
    }


}
