package fr.hyriode.bedwars.game.team;

import fr.hyriode.bedwars.game.team.upgrade.BWUpgrades;
import fr.hyriode.hyrame.game.team.HyriGameTeam;
import fr.hyriode.hyrame.utils.Area;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.configuration.HyriBWConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;

public class BWGameTeam extends HyriGameTeam {

    private boolean hasBed;
    private boolean eliminated;

    private final HyriBedWars plugin;

    private final BWUpgrades upgrades;

    private Location bedLocation;
    private Area baseArea;
    private Area protectArea;

    public BWGameTeam(EBWGameTeam team, int teamSize, HyriBedWars plugin) {
        super(team.getName(), team.getDisplayName(), team.getColor(), teamSize);
        this.plugin = plugin;
        this.upgrades = new BWUpgrades(this);
        this.init();
    }

    public void init() {
        this.hasBed = true;
        this.eliminated = false;

        HyriBWConfiguration.Team teamConfig = plugin.getConfiguration().getTeams().stream().filter(team -> team.getName().equalsIgnoreCase(this.name)).findFirst().get();

        this.bedLocation = teamConfig.getBedLocation();

        this.setSpawnLocation(teamConfig.getRespawnLocation());

        this.baseArea = new Area(teamConfig.getBaseAreaPos1(), teamConfig.getBaseAreaPos2());
        this.protectArea = new Area(teamConfig.getBaseAreaProtectionPos1(), teamConfig.getBaseAreaProtectionPos2());

    }

    public void updateUpgrade(){
        this.upgrades.getUpgrades().forEach((upgrade, upgradeTier) -> {
            this.players.forEach(player -> {

            });
        });
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

    public Location getBedLocation() {
        return bedLocation;
    }

    public Area getBaseArea() {
        return baseArea;
    }

    public Area getProtectArea() {
        return protectArea;
    }

    public BWUpgrades getUpgrades() {
        return upgrades;
    }

    public String getStateAsSymbol() {
        if(this.hasBed) {
            return ChatColor.GREEN + "✔";
        } else if(this.eliminated) {
            return ChatColor.RED + "✘";
        } else {
            return ChatColor.GREEN + String.valueOf(this.getPlayersPlaying().size());
        }
    }
}
