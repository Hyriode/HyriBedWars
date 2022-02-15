package fr.hyriode.hyribedwars.game.team;

import fr.hyriode.hyrame.game.team.HyriGameTeam;
import fr.hyriode.hyrame.game.team.HyriGameTeamColor;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.hyrame.utils.Area;
import fr.hyriode.hyribedwars.HyriBedWars;
import fr.hyriode.hyribedwars.configuration.HyriBedWarsConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

public class HyriBedWarsGameTeam extends HyriGameTeam {

    private boolean hasBed;
    private boolean eliminated;

    private final HyriBedWars plugin;

    private Location bedLocation;
    private Area baseArea;
    private Area protectArea;

    public HyriBedWarsGameTeam(EHyriBedWarsGameTeam team, int teamSize, HyriBedWars plugin) {
        super(team.getName(), team.getDisplayName(), team.getColor(), teamSize);
        this.plugin = plugin;
    }

    public void init() {
        this.hasBed = true;
        this.eliminated = false;

        HyriBedWarsConfiguration.Team teamConfig = plugin.getConfiguration().getTeams().stream().filter(team -> team.getName().equalsIgnoreCase(this.name)).findFirst().get();

        teamConfig.getBedLocation().getWorld().getBlockAt(teamConfig.getBedLocation()).setType(Material.BED);
        this.bedLocation = teamConfig.getBedLocation().clone();

        this.setSpawnLocation(teamConfig.getRespawnLocation());

        this.baseArea = new Area(teamConfig.getBaseAreaPos1(), teamConfig.getBaseAreaPos2());
        this.protectArea = new Area(teamConfig.getBaseAreaProtectionPos1(), teamConfig.getBaseAreaProtectionPos2());

    }

    public boolean hasBed() {
        return hasBed;
    }

    public void setHasBed(boolean hasBed) {
        this.hasBed = hasBed;
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }

    public String getStateAsSymbol() {
        if(hasBed) {
            return ChatColor.GREEN + "✓";
        } else if(eliminated) {
            return ChatColor.RED + "✘";
        } else {
            return ChatColor.GREEN + String.valueOf(this.getPlayersPlaying().size());
        }
    }
}
