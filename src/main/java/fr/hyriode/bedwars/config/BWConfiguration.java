package fr.hyriode.bedwars.config;

import fr.hyriode.api.config.IHyriConfig;
import fr.hyriode.bedwars.game.generator.BWGenerator;
import fr.hyriode.bedwars.game.shop.ItemMoney;
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

    private final double cancelInventoryY;

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

        this.cancelInventoryY = cancelInventoryY;
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

    public double getCancelInventoryY() {
        return cancelInventoryY;
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
                ", cancelInventoryY=" + cancelInventoryY +
                '}';
    }

    public static class Generator {

        private final String generator;
        private final int tier;
        private final List<Drop> drops;

        public Generator(String generator, int tier, Drop... drops) {
            this.generator = generator;
            this.tier = tier;
            this.drops = Arrays.asList(drops);
        }

        public Generator(String generator, int tier, List<Drop> drops) {
            this.generator = generator;
            this.tier = tier;
            this.drops = drops;
        }

        public String getGenerator() {
            return generator;
        }

        public int getTier() {
            return tier;
        }

        public List<Drop> getDrops() {
            return drops;
        }

        public BWGenerator.Tier toTier(){
            return new BWGenerator.Tier(this.tier, this.generator, this.drops.stream().map(Drop::toDrop).collect(Collectors.toList()));
        }

        public static class Drop {

            private String title;
            private final int spawnLimit;
            private final int spawnBetween;
            private final boolean splitting;
            private final String drop;

            public Drop(int spawnLimit, int spawnBetween, boolean splitting, String drop) {
                this(null, spawnLimit, spawnBetween, splitting, drop);
            }
            public Drop(String title, int spawnLimit, int spawnBetween, boolean splitting, String drop) {
                this.title = title;
                this.spawnLimit = spawnLimit;
                this.spawnBetween = spawnBetween;
                this.splitting = splitting;
                this.drop = drop;
            }

            public String getTitle() {
                return title;
            }

            public int getSpawnLimit() {
                return spawnLimit;
            }

            public int getSpawnBetween() {
                return spawnBetween;
            }

            public boolean isSplitting() {
                return splitting;
            }

            public String getDrop() {
                return drop;
            }

            public BWGenerator.Tier.Drop toDrop() {
                return new BWGenerator.Tier.Drop(this.title != null ? (__) -> this.title : null, this.spawnLimit, this.spawnBetween, this.splitting, ItemMoney.valueOf(this.drop.toUpperCase()));
            }
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
}
