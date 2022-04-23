package fr.hyriode.bedwars.configuration;

import fr.hyriode.hyrame.utils.Area;
import fr.hyriode.hyrame.utils.LocationWrapper;
import fr.hyriode.hystia.api.config.IConfig;
import org.bukkit.Location;

import java.util.List;
import java.util.stream.Collectors;

public class HyriBWConfiguration implements IConfig {

    private final List<Team> teams;

    private final WaitingRoom waitingRoom;

    private final GameArea gameArea;

    private final List<LocationWrapper> diamondGeneratorLocations;

    private final List<LocationWrapper> emeraldGeneratorLocations;

    private final double cancelInventoryY;

    public HyriBWConfiguration(WaitingRoom waitingRoom, GameArea gameArea, List<LocationWrapper> diamondGeneratorLocations,
                               List<LocationWrapper> emeraldGeneratorLocations, List<Team> teams, double cancelInventoryY) {
        this.teams = teams;

        this.waitingRoom = waitingRoom;
        this.gameArea = gameArea;

        this.diamondGeneratorLocations = diamondGeneratorLocations;

        this.emeraldGeneratorLocations = emeraldGeneratorLocations;

        this.cancelInventoryY = cancelInventoryY;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public Team getTeam(String teamName){
        return this.teams.stream().filter(team -> team.getName().equals(teamName)).findFirst().orElse(null);
    }

    public WaitingRoom getWaitingRoom() {
        return this.waitingRoom;
    }

    public GameArea getGameArea(){
        return this.gameArea;
    }

    public List<Location> getDiamondGeneratorLocations() {
        return diamondGeneratorLocations.stream().map(LocationWrapper::asBukkit).collect(Collectors.toList());
    }

    public List<Location> getEmeraldGeneratorLocations() {
        return emeraldGeneratorLocations.stream().map(LocationWrapper::asBukkit).collect(Collectors.toList());
    }

    public double getCancelInventoryY() {
        return cancelInventoryY;
    }

    public static class WaitingRoom{

        private final LocationWrapper waitingSpawn;

        private final LocationWrapper waitingSpawnPos1;
        private final LocationWrapper waitingSpawnPos2;

        public WaitingRoom(LocationWrapper waitingSpawn, LocationWrapper waitingSpawnPos1, LocationWrapper waitingSpawnPos2){
            this.waitingSpawn = waitingSpawn;
            this.waitingSpawnPos1 = waitingSpawnPos1;
            this.waitingSpawnPos2 = waitingSpawnPos2;
        }

        public Location getWaitingSpawn() {
            return waitingSpawn.asBukkit();
        }

        public Location getWaitingSpawnPos1() {
            return waitingSpawnPos1.asBukkit();
        }

        public Location getWaitingSpawnPos2() {
            return waitingSpawnPos2.asBukkit();
        }

        public Area getArea() {
            return new Area(this.waitingSpawnPos1.asBukkit(), this.waitingSpawnPos2.asBukkit());
        }
    }

    public static class GameArea{

        private final LocationWrapper gameAreaPos1;
        private final LocationWrapper gameAreaPos2;

        public GameArea(LocationWrapper gameAreaPos1, LocationWrapper gameAreaPos2){
            this.gameAreaPos1 = gameAreaPos1;
            this.gameAreaPos2 = gameAreaPos2;
        }

        public Location getGameAreaPos1() {
            return gameAreaPos1.asBukkit();
        }

        public Location getGameAreaPos2() {
            return gameAreaPos2.asBukkit();
        }

        public Area getArea(){
            return new Area(this.gameAreaPos1.asBukkit(), this.gameAreaPos2.asBukkit());
        }
    }

    public static class Team {

        private final String name;

        private final LocationWrapper baseAreaPos1;
        private final LocationWrapper baseAreaPos2;

        private final LocationWrapper baseAreaProtectionPos1;
        private final LocationWrapper baseAreaProtectionPos2;

        private final LocationWrapper generatorLocation;

        private final LocationWrapper shopNPCLocation;
        private final LocationWrapper upgradeNPCLocation;

        private final LocationWrapper respawnLocation;

        public Team(String name, LocationWrapper baseAreaPos1, LocationWrapper baseAreaPos2, LocationWrapper baseAreaProtectionPos1, LocationWrapper baseAreaProtectionPos2,
        LocationWrapper generatorLocation, LocationWrapper shopNPCLocation, LocationWrapper upgradeNPCLocation, LocationWrapper respawnLocation) {

            this.name = name;

            this.baseAreaPos1 = baseAreaPos1;
            this.baseAreaPos2 = baseAreaPos2;

            this.baseAreaProtectionPos1 = baseAreaProtectionPos1;
            this.baseAreaProtectionPos2 = baseAreaProtectionPos2;

            this.generatorLocation = generatorLocation;

            this.respawnLocation = respawnLocation;

            this.shopNPCLocation = shopNPCLocation;

            this.upgradeNPCLocation = upgradeNPCLocation;
        }

        public String getName() {
            return name;
        }

        public Location getBaseAreaPos1() {
            return baseAreaPos1.asBukkit();
        }

        public Location getBaseAreaPos2() {
            return baseAreaPos2.asBukkit();
        }

        public Location getBaseAreaProtectionPos1() {
            return baseAreaProtectionPos1.asBukkit();
        }

        public Location getBaseAreaProtectionPos2() {
            return baseAreaProtectionPos2.asBukkit();
        }

        public Location getGeneratorLocation() {
            return generatorLocation.asBukkit();
        }

        public Location getShopNPCLocation() {
            return shopNPCLocation.asBukkit();
        }

        public Location getUpgradeNPCLocation() {
            return upgradeNPCLocation.asBukkit();
        }

        public Location getRespawnLocation() {
            return respawnLocation.asBukkit();
        }

        public Area getBaseArea(){
            return new Area(this.baseAreaPos1.asBukkit(), this.baseAreaPos2.asBukkit());
        }
    }
}
