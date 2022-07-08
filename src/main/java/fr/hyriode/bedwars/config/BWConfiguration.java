package fr.hyriode.bedwars.config;

import fr.hyriode.hyrame.game.waitingroom.HyriWaitingRoom;
import fr.hyriode.hyrame.utils.Area;
import fr.hyriode.hyrame.utils.LocationWrapper;
import fr.hyriode.hystia.api.config.IConfig;
import org.bukkit.Location;

import java.util.List;
import java.util.stream.Collectors;

public class BWConfiguration implements IConfig {

    private final List<Team> teams;

    private final WaitingRoom waitingRoom;
    private final GameArea gameArea;
    private final List<AreaWrapper> protectionArea;

    private final List<LocationWrapper> diamondGeneratorLocations;
    private final List<LocationWrapper> emeraldGeneratorLocations;

    private final double cancelInventoryY;

    public BWConfiguration(WaitingRoom waitingRoom, GameArea gameArea, List<AreaWrapper> protectionArea, List<LocationWrapper> diamondGeneratorLocations,
                               List<LocationWrapper> emeraldGeneratorLocations, List<Team> teams, double cancelInventoryY) {
        this.teams = teams;

        this.waitingRoom = waitingRoom;
        this.gameArea = gameArea;
        this.protectionArea = protectionArea;

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

    public HyriWaitingRoom.Config getWaitingRoom() {
        return new HyriWaitingRoom.Config(this.waitingRoom.getWaitingSpawn(), this.waitingRoom.getWaitingSpawnPos1(), this.waitingRoom.getWaitingSpawnPos2(), this.waitingRoom.getNPCLocation());
    }

    public GameArea getGameArea(){
        return this.gameArea;
    }

    public List<Area> getProtectionArea() {
        return protectionArea.stream().map(AreaWrapper::getArea).collect(Collectors.toList());
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

    @Override
    public String toString() {
        return "HyriBWConfiguration{" +
                "teams=" + teams +
                ", waitingRoom=pos1(" + waitingRoom.getWaitingSpawnPos1().getX() + ", " + waitingRoom.getWaitingSpawnPos1().getY() + ", " + waitingRoom.getWaitingSpawnPos1().getZ() + ") pos2(" + waitingRoom.getWaitingSpawnPos2().getX() + ", " + waitingRoom.getWaitingSpawnPos2().getY() + ", " + waitingRoom.getWaitingSpawnPos2().getZ() + ")" +
                ", gameArea=" + gameArea +
                ", protectArea=" + protectionArea +
                ", diamondGeneratorLocations=" + diamondGeneratorLocations +
                ", emeraldGeneratorLocations=" + emeraldGeneratorLocations +
                ", cancelInventoryY=" + cancelInventoryY +
                '}';
    }

    public static class WaitingRoom{

        private final LocationWrapper spawn;
        /** First waiting room area position */
        private final LocationWrapper firstPos;
        /** Second waiting room area position */
        private final LocationWrapper secondPos;
        /** The location of the npc */
        private final LocationWrapper npcLocation;

        public WaitingRoom(LocationWrapper spawn, LocationWrapper firstPos, LocationWrapper secondPos, LocationWrapper npcLocation) {
            this.spawn = spawn;
            this.firstPos = firstPos;
            this.secondPos = secondPos;
            this.npcLocation = npcLocation;
        }

        public LocationWrapper getNPCLocation() {
            return this.npcLocation;
        }

        public LocationWrapper getWaitingSpawn() {
            return spawn;
        }

        public LocationWrapper getWaitingSpawnPos1() {
            return firstPos;
        }

        public LocationWrapper getWaitingSpawnPos2() {
            return secondPos;
        }

        public Area getArea() {
            return new Area(this.firstPos.asBukkit(), this.secondPos.asBukkit());
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

        public Area getBaseProtectArea(){
            return new Area(this.getBaseAreaProtectionPos1(), this.getBaseAreaProtectionPos2());
        }

        @Override
        public String toString() {
            return "Team{" +
                    "name='" + name + '\'' +
                    ", baseAreaPos1=" + baseAreaPos1 +
                    ", baseAreaPos2=" + baseAreaPos2 +
                    ", baseAreaProtectionPos1=" + baseAreaProtectionPos1 +
                    ", baseAreaProtectionPos2=" + baseAreaProtectionPos2 +
                    ", generatorLocation=" + generatorLocation +
                    ", shopNPCLocation=" + shopNPCLocation +
                    ", upgradeNPCLocation=" + upgradeNPCLocation +
                    ", respawnLocation=" + respawnLocation +
                    '}';
        }
    }

    public static class AreaWrapper{
        private final LocationWrapper pos1;
        private final LocationWrapper pos2;

        public AreaWrapper(LocationWrapper pos1, LocationWrapper pos2){
            this.pos1 = pos1;
            this.pos2 = pos2;
        }

        public Location getPos1() {
            return pos1.asBukkit();
        }

        public Location getPos2() {
            return pos2.asBukkit();
        }

        public Area getArea(){
            return new Area(this.pos1.asBukkit(), this.pos2.asBukkit());
        }

        @Override
        public String toString() {
            return "AreaWrapper{" +
                    "pos1=" + pos1.getX() + ", " + pos1.getY() + ", " + pos1.getZ() +
                    ", pos2=" + pos2.getX() + ", " + pos2.getY() + ", " + pos2.getZ() +
                    '}';
        }
    }
}
