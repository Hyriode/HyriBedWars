package fr.hyriode.bedwars.configuration;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.configuration.HyriConfigurationEntry.*;
import fr.hyriode.hyrame.configuration.IHyriConfiguration;
import fr.hyriode.hyrame.utils.Area;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.team.EBWGameTeam;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class HyriBWConfiguration implements IHyriConfiguration {

    private static final Supplier<Location> DEFAULT_LOCATION = () -> new Location(IHyrame.WORLD.get(), 0, 0, 0, 0, 0);

    private final List<Team> teams;

    private Location waitingSpawn;
    private final LocationEntry waitingSpawnEntry;

    private Location waitingSpawnPos1;
    private final LocationEntry waitingSpawnPos1Entry;
    private Location waitingSpawnPos2;
    private final LocationEntry waitingSpawnPos2Entry;

    private Location gameAreaPos1;
    private final LocationEntry gameAreaPos1Entry;

    private Location gameAreaPos2;
    private final LocationEntry gameAreaPos2Entry;

    private Location killLoc;
    private final LocationEntry killLocEntry;

    private final FileConfiguration config;
    private final HyriBedWars plugin;

    public HyriBWConfiguration(HyriBedWars plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.teams = new ArrayList<>();

        final String waitingSpawnKey = "waiting-spawn.";

        this.waitingSpawn = DEFAULT_LOCATION.get();
        this.waitingSpawnEntry = new LocationEntry(waitingSpawnKey + "spawn", this.config);

        this.waitingSpawnPos1 = DEFAULT_LOCATION.get();
        this.waitingSpawnPos1Entry = new LocationEntry(waitingSpawnKey + "first", this.config);
        this.waitingSpawnPos2 = DEFAULT_LOCATION.get();
        this.waitingSpawnPos2Entry = new LocationEntry(waitingSpawnKey + "second", this.config);

        final String gameAreaKey = "game-area.";

        this.gameAreaPos1 = DEFAULT_LOCATION.get();
        this.gameAreaPos1Entry = new LocationEntry(gameAreaKey + "first", this.config);
        this.gameAreaPos2 = DEFAULT_LOCATION.get();
        this.gameAreaPos2Entry = new LocationEntry(gameAreaKey + "second", this.config);

        this.killLoc = DEFAULT_LOCATION.get();
        this.killLocEntry = new LocationEntry("kill-location", this.config);

        this.registerTeamsConfig();
    }

    private void registerTeamsConfig() {
        for (EBWGameTeam team : EBWGameTeam.values()) {
            teams.add(new Team("team." + team.getName(), team.getName()));
        }
    }

    @Override
    public void create() {
        this.waitingSpawnEntry.setDefault(this.waitingSpawn);
        this.waitingSpawnPos1Entry.setDefault(this.waitingSpawnPos1);
        this.waitingSpawnPos2Entry.setDefault(this.waitingSpawnPos2);

        this.gameAreaPos1Entry.setDefault(this.gameAreaPos1);
        this.gameAreaPos2Entry.setDefault(this.gameAreaPos2);

        this.killLocEntry.setDefault(this.killLoc);

        this.createTeamsConfig();

        this.plugin.saveConfig();
    }

    private void createTeamsConfig() {
        for (Team team : this.teams) {
            team.create();
        }
    }

    @Override
    public void load() {
        HyriBedWars.log("Loading configuration...");

        this.waitingSpawn = this.waitingSpawnEntry.get();
        this.waitingSpawnPos1 = this.waitingSpawnPos1Entry.get();
        this.waitingSpawnPos2 = this.waitingSpawnPos2Entry.get();

        this.gameAreaPos1 = this.gameAreaPos1Entry.get();
        this.gameAreaPos2 = this.gameAreaPos2Entry.get();

        this.killLoc = this.killLocEntry.get();

        this.loadTeamConfig();
    }

    private void loadTeamConfig() {
        for (Team team : this.teams) {
            team.load();
        }
    }

    @Override
    public void save() {
        HyriBedWars.log("Saving configuration...");

        this.waitingSpawnEntry.set(this.waitingSpawn);
        this.waitingSpawnPos1Entry.set(this.waitingSpawnPos1);
        this.waitingSpawnPos2Entry.set(this.waitingSpawnPos2);

        this.gameAreaPos1Entry.set(this.gameAreaPos1);
        this.gameAreaPos2Entry.set(this.gameAreaPos2);

        this.killLocEntry.set(this.killLoc);

        this.saveTeamConfig();

        this.plugin.saveConfig();
    }

    private void saveTeamConfig() {
        for (Team team : this.teams) {
            team.save();
        }
    }

    @Override
    public FileConfiguration getConfig() {
        return this.config;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public Team getTeam(String teamName){
        return this.teams.stream().filter(team1 -> {
            System.out.println(teamName);
            System.out.println(team1);
            return team1.getName().equals(teamName);
        }).findFirst().get();
    }

    public Location getWaitingSpawn() {
        return waitingSpawn;
    }

    public Location getWaitingSpawnPos1() {
        return waitingSpawnPos1;
    }

    public Location getWaitingSpawnPos2() {
        return waitingSpawnPos2;
    }

    public Location getGameAreaPos1() {
        return gameAreaPos1;
    }

    public Location getGameAreaPos2() {
        return gameAreaPos2;
    }

    public Location getKillLoc() {
        return killLoc;
    }

    public class Team implements IHyriConfiguration {

        private final String name;

        private Location baseAreaPos1;
        private final LocationEntry baseAreaPos1Entry;
        private Location baseAreaPos2;
        private final LocationEntry baseAreaPos2Entry;

        private Location baseAreaProtectionPos1;
        private final LocationEntry baseAreaProtectionPos1Entry;
        private Location baseAreaProtectionPos2;
        private final LocationEntry baseAreaProtectionPos2Entry;

        private Location generatorLocation;
        private final LocationEntry generatorLocationEntry;

        private Location shopNPCLocation;
        private final LocationEntry shopNPCLocationEntry;
        private Location upgradeNPCLocation;
        private final LocationEntry upgradeNPCLocationEntry;

        private Location respawnLocation;
        private final LocationEntry respawnLocationEntry;

        private Location bedLocation;
        private final LocationEntry bedLocationEntry;

        public Team(String key, String name) {
            key += ".";
            this.name = name;

            final String baseKey = key + "base.";
            final String areaKey = baseKey + "area.";
            final String protectionKey = baseKey + "protection.";
            final String npcKey = baseKey + "npc.";
            final String spawnKey = baseKey + "spawn.";
            final String generatorKey = baseKey + "generator.";

            this.baseAreaPos1 = DEFAULT_LOCATION.get();
            this.baseAreaPos1Entry = new LocationEntry(areaKey + "first", this.getConfig());
            this.baseAreaPos2 = DEFAULT_LOCATION.get();
            this.baseAreaPos2Entry = new LocationEntry(areaKey + "second", this.getConfig());

            this.baseAreaProtectionPos1 = DEFAULT_LOCATION.get();
            this.baseAreaProtectionPos1Entry = new LocationEntry(protectionKey + "first", this.getConfig());
            this.baseAreaProtectionPos2 = DEFAULT_LOCATION.get();
            this.baseAreaProtectionPos2Entry = new LocationEntry(protectionKey + "second", this.getConfig());

            this.generatorLocation = DEFAULT_LOCATION.get();
            this.generatorLocationEntry = new LocationEntry(generatorKey + "location", this.getConfig());

            this.respawnLocation = DEFAULT_LOCATION.get();
            this.respawnLocationEntry = new LocationEntry(spawnKey + "location", this.getConfig());

            this.shopNPCLocation = DEFAULT_LOCATION.get();
            this.shopNPCLocationEntry = new LocationEntry(npcKey + "shop.location", this.getConfig());

            this.upgradeNPCLocation = DEFAULT_LOCATION.get();
            this.upgradeNPCLocationEntry = new LocationEntry(npcKey + "upgrade.location", this.getConfig());

            this.bedLocation = DEFAULT_LOCATION.get();
            this.bedLocationEntry = new LocationEntry(baseKey + "bed.location", this.getConfig());
        }

        @Override
        public void create() {
            this.baseAreaPos1Entry.setDefault(this.baseAreaPos1);
            this.baseAreaPos2Entry.setDefault(this.baseAreaPos2);

            this.baseAreaProtectionPos1Entry.setDefault(this.baseAreaProtectionPos1);
            this.baseAreaProtectionPos2Entry.setDefault(this.baseAreaProtectionPos2);

            this.generatorLocationEntry.setDefault(this.generatorLocation);
            this.respawnLocationEntry.setDefault(this.respawnLocation);
            this.shopNPCLocationEntry.setDefault(this.shopNPCLocation);
            this.upgradeNPCLocationEntry.setDefault(this.upgradeNPCLocation);
            this.bedLocationEntry.setDefault(this.bedLocation);
        }

        @Override
        public void load() {
            this.baseAreaPos1 = this.baseAreaPos1Entry.get();
            this.baseAreaPos2 = this.baseAreaPos2Entry.get();

            this.baseAreaProtectionPos1 = this.baseAreaProtectionPos1Entry.get();
            this.baseAreaProtectionPos2 = this.baseAreaProtectionPos2Entry.get();

            this.generatorLocation = this.generatorLocationEntry.get();
            this.respawnLocation = this.respawnLocationEntry.get();
            this.shopNPCLocation = this.shopNPCLocationEntry.get();
            this.upgradeNPCLocation = this.upgradeNPCLocationEntry.get();
            this.bedLocation = this.bedLocationEntry.get();
        }

        @Override
        public void save() {
            this.baseAreaPos1Entry.set(this.baseAreaPos1);
            this.baseAreaPos2Entry.set(this.baseAreaPos2);

            this.baseAreaProtectionPos1Entry.set(this.baseAreaProtectionPos1);
            this.baseAreaProtectionPos2Entry.set(this.baseAreaProtectionPos2);

            this.generatorLocationEntry.set(this.generatorLocation);
            this.respawnLocationEntry.set(this.respawnLocation);
            this.shopNPCLocationEntry.set(this.shopNPCLocation);
            this.upgradeNPCLocationEntry.set(this.upgradeNPCLocation);
            this.bedLocationEntry.set(this.bedLocation);
        }

        @Override
        public FileConfiguration getConfig() {
            return config;
        }

        public String getName() {
            return name;
        }

        public Location getBaseAreaPos1() {
            return baseAreaPos1;
        }

        public Location getBaseAreaPos2() {
            return baseAreaPos2;
        }

        public Location getBaseAreaProtectionPos1() {
            return baseAreaProtectionPos1;
        }

        public Location getBaseAreaProtectionPos2() {
            return baseAreaProtectionPos2;
        }

        public Location getGeneratorLocation() {
            return generatorLocation;
        }

        public Location getShopNPCLocation() {
            return shopNPCLocation;
        }

        public Location getUpgradeNPCLocation() {
            return upgradeNPCLocation;
        }

        public Location getRespawnLocation() {
            return respawnLocation;
        }

        public Location getBedLocation() {
            return bedLocation;
        }

        public Area getBaseArea(){
            return new Area(this.baseAreaPos1, this.baseAreaPos2);
        }
    }
}
