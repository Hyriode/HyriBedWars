package fr.hyriode.bedwars.config;

import fr.hyriode.api.config.IHyriConfig;
import fr.hyriode.hyrame.game.waitingroom.HyriWaitingRoom;
import fr.hyriode.hyrame.utils.Area;
import fr.hyriode.hyrame.utils.AreaWrapper;
import fr.hyriode.hyrame.utils.LocationWrapper;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BWConfiguration implements IHyriConfig {

    private final List<Team> teams;

    private final HyriWaitingRoom.Config waitingRoom;
    private final AreaWrapper gameArea;
    private final List<AreaWrapper> protectionArea;

    private final List<Generator> generatorsBase;
    private final List<Generator> generatorsDiamond;
    private final List<Generator> generatorsEmerald;

    private final List<LocationWrapper> diamondGeneratorLocations;
    private final List<LocationWrapper> emeraldGeneratorLocations;

    public BWConfiguration(HyriWaitingRoom.Config waitingRoom, AreaWrapper gameArea, List<AreaWrapper> protectionArea,
                           List<Generator> generatorsDiamond, List<Generator> generatorsEmerald, List<LocationWrapper> diamondGeneratorLocations, List<LocationWrapper> emeraldGeneratorLocations,
                           List<Generator> generatorsBase, List<Team> teams, double cancelInventoryY) {
        this.teams = teams;

        this.waitingRoom = waitingRoom;
        this.gameArea = gameArea;
        this.protectionArea = protectionArea;

        this.diamondGeneratorLocations = diamondGeneratorLocations;
        this.emeraldGeneratorLocations = emeraldGeneratorLocations;
        this.generatorsDiamond = generatorsDiamond;
        this.generatorsEmerald = generatorsEmerald;
        this.generatorsBase = generatorsBase;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public Team getTeam(String teamName){
        return this.teams.stream().filter(team -> team.getName().equals(teamName)).findFirst().orElse(null);
    }

    public HyriWaitingRoom.Config getWaitingRoom() {
        return this.waitingRoom;
    }

    public AreaWrapper getGameArea(){
        return this.gameArea;
    }

    public List<Area> getProtectionArea() {
        return this.protectionArea.stream().map(AreaWrapper::asArea).collect(Collectors.toList());
    }

    public List<Location> getDiamondGeneratorLocations() {
        return this.diamondGeneratorLocations.stream().map(LocationWrapper::asBukkit).collect(Collectors.toList());
    }

    public List<Location> getEmeraldGeneratorLocations() {
        return this.emeraldGeneratorLocations.stream().map(LocationWrapper::asBukkit).collect(Collectors.toList());
    }

    public List<Generator> getGeneratorsBase() {
        return generatorsBase;
    }

    public List<Generator> getGeneratorsDiamond() {
        return generatorsDiamond;
    }

    public List<Generator> getGeneratorsEmerald() {
        return generatorsEmerald;
    }

    @Override
    public String toString() {
        return "HyriBWConfiguration{" +
                "teams=" + teams +
                ", waitingRoom=pos1(" + "TODO" + ")" +
                ", gameArea=" + gameArea +
                ", protectArea=" + protectionArea +
                ", diamondGeneratorLocations=" + diamondGeneratorLocations +
                ", emeraldGeneratorLocations=" + emeraldGeneratorLocations +
                '}';
    }

    public static class Generator {

        private final int tier;
        private final List<Drop> drops;

        public Generator(int tier, List<Drop> drops) {
            this.tier = tier;
            this.drops = drops;
        }

        public Generator(int tier, Drop... drops) {
            this.tier = tier;
            this.drops = Arrays.asList(drops);
        }

        public int getTier() {
            return tier;
        }

        public List<Drop> getDrops() {
            return drops;
        }

        public static class Drop {

            private final String itemName;
            private final int spawnBetween;

            public Drop(String itemName, int spawnBetween) {
                this.itemName = itemName;
                this.spawnBetween = spawnBetween;
            }

            public String getItemName() {
                return itemName;
            }

            public int getSpawnBetween() {
                return spawnBetween;
            }
        }

    }

    public static class Team {

        private final String name;

        private final AreaWrapper baseArea;

        private final AreaWrapper baseAreaProtection;

        private final LocationWrapper generatorLocation;

        private final LocationWrapper shopNPCLocation;
        private final LocationWrapper upgradeNPCLocation;

        private final LocationWrapper respawnLocation;

        public Team(String name, AreaWrapper baseArea, AreaWrapper baseAreaProtection,
                    LocationWrapper generatorLocation, LocationWrapper shopNPCLocation,
                    LocationWrapper upgradeNPCLocation, LocationWrapper respawnLocation) {

            this.name = name;

            this.baseArea = baseArea;

            this.baseAreaProtection = baseAreaProtection;

            this.generatorLocation = generatorLocation;

            this.respawnLocation = respawnLocation;

            this.shopNPCLocation = shopNPCLocation;

            this.upgradeNPCLocation = upgradeNPCLocation;
        }

        public String getName() {
            return name;
        }

        public Area getBaseArea() {
            return baseArea.asArea();
        }

        public Area getBaseAreaProtection() {
            return this.baseAreaProtection.asArea();
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

        @Override
        public String toString() {
            return "Team{" +
                    "name='" + name + '\'' +
                    ", baseArea=" + baseArea +
                    ", baseAreaProtection=" + baseAreaProtection +
                    ", generatorLocation=" + generatorLocation +
                    ", shopNPCLocation=" + shopNPCLocation +
                    ", upgradeNPCLocation=" + upgradeNPCLocation +
                    ", respawnLocation=" + respawnLocation +
                    '}';
        }
    }
}
