package fr.hyriode.hyribedwars.game.scoreboard;

import fr.hyriode.hyrame.game.team.HyriGameTeam;
import fr.hyriode.hyrame.scoreboard.Scoreboard;
import fr.hyriode.hyribedwars.HyriBedWars;
import fr.hyriode.hyribedwars.game.HyriBedWarsGame;
import fr.hyriode.hyribedwars.game.team.HyriBedWarsGameTeam;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

public class HyriBedWarsGameScoreboard extends Scoreboard {

    private final HyriBedWars plugin;
    private final HyriBedWarsGame game;

    public HyriBedWarsGameScoreboard(HyriBedWars plugin, Player player) {
        super(plugin, player, "hyribedwars", ChatColor.DARK_AQUA + "     " + ChatColor.BOLD + plugin.getGame().getDisplayName() + "     ");
        this.plugin = plugin;
        this.game = this.plugin.getGame();

        this.addTeamsLines();

        this.setLine(0, getDateLine(), scoreboardLine -> scoreboardLine.setValue(getDateLine()), 20);
        this.setLine(1, "§1");
        //TODO Line 2 -> Next Event
        this.setLine(3, "§2");

    }

    private void addTeamsLines() {
        int i = 4;
        for (HyriGameTeam team : this.game.getTeams()) {
            this.setLine(i, getTeamLine((HyriBedWarsGameTeam) team));
            i += 1;
        }
    }

    private String getTeamLine(HyriBedWarsGameTeam team) {
        if(this.game.getPlayer(player.getUniqueId()).isInTeam(team)) {
            return team.getDisplayName().getForPlayer(player) + ChatColor.RESET+ " » " +team.getStateAsSymbol() + " " + this.getLinePrefix("you");
        }
        return team.getDisplayName().getForPlayer(player) + ChatColor.RESET+ " » " +team.getStateAsSymbol();
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
        return HyriBedWars.getHyriLanguageManager().getValue(this.player, "scoreboard." + prefix + ".display");
    }


}
