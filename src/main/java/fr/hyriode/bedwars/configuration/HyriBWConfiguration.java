package fr.hyriode.bedwars.configuration;

import fr.hyriode.bedwars.game.BWGameType;
import fr.hyriode.bedwars.game.material.BWMaterials;
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
import java.util.stream.Collectors;

public class HyriBWConfiguration implements IHyriConfiguration {

    private static final Supplier<Location> DEFAULT_LOCATION = () -> new Location(IHyrame.WORLD.get(), 0, 0, 0, 0, 0);

    private final Ores ores;

    private final List<Team> teams;

    private BWGameType gameType;
    private final StringEntry gameTypeEntry;

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

    private Location npcCoalLoc;
    private final LocationEntry npcCoalLocEntry;

    private List<Location> diamondLocs;
    private final ListEntry diamondLocsEntry;

    private List<Location> emeraldLocs;
    private final ListEntry emeraldLocsEntry;

    private double cancelOpenInventoryY;
    private final DoubleEntry cancelOpenInventoryYEntry;

    private final FileConfiguration config;
    private final HyriBedWars plugin;

    public HyriBWConfiguration(HyriBedWars plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        this.teams = new ArrayList<>();

        this.gameType = BWGameType.SOLO;
        this.gameTypeEntry = new StringEntry("game-type", this.config);

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

        this.npcCoalLoc = DEFAULT_LOCATION.get();
        this.npcCoalLocEntry = new LocationEntry("npc-coal", this.config);

        this.diamondLocs = new ArrayList<>();
        this.diamondLocsEntry = new ListEntry("diamond-locations", this.config);

        this.emeraldLocs = new ArrayList<>();
        this.emeraldLocsEntry = new ListEntry("emerald-locations", this.config);

        this.cancelOpenInventoryY = 50.0D;
        this.cancelOpenInventoryYEntry = new DoubleEntry("fall.cancelOpenInventory-y", this.config);

        this.registerTeamsConfig();
        this.ores = new Ores("ores");
    }

    private void registerTeamsConfig() {
        for (EBWGameTeam team : EBWGameTeam.values()) {
            teams.add(new Team("team." + team.getName(), team.getName()));
        }
    }

    @Override
    public void create() {

        this.gameTypeEntry.setDefault(this.gameType.name());

        this.waitingSpawnEntry.setDefault(this.waitingSpawn);
        this.waitingSpawnPos1Entry.setDefault(this.waitingSpawnPos1);
        this.waitingSpawnPos2Entry.setDefault(this.waitingSpawnPos2);

        this.gameAreaPos1Entry.setDefault(this.gameAreaPos1);
        this.gameAreaPos2Entry.setDefault(this.gameAreaPos2);

        this.killLocEntry.setDefault(this.killLoc);

        this.npcCoalLocEntry.setDefault(this.npcCoalLoc);

        this.diamondLocsEntry.setDefault(this.diamondLocs);
        this.emeraldLocsEntry.setDefault(this.emeraldLocs);

        this.cancelOpenInventoryYEntry.setDefault(this.cancelOpenInventoryY);

        this.createTeamsConfig();
        this.ores.create();

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

        this.gameType = BWGameType.valueOf(this.gameTypeEntry.get());

        this.waitingSpawn = this.waitingSpawnEntry.get();
        this.waitingSpawnPos1 = this.waitingSpawnPos1Entry.get();
        this.waitingSpawnPos2 = this.waitingSpawnPos2Entry.get();

        this.gameAreaPos1 = this.gameAreaPos1Entry.get();
        this.gameAreaPos2 = this.gameAreaPos2Entry.get();

        this.killLoc = this.killLocEntry.get();
        this.npcCoalLoc = this.npcCoalLocEntry.get();

        this.diamondLocs = this.diamondLocsEntry.get().stream().map(o -> (Location)o).collect(Collectors.toList());
        this.emeraldLocs = this.emeraldLocsEntry.get().stream().map(o -> (Location)o).collect(Collectors.toList());

        this.cancelOpenInventoryY = this.cancelOpenInventoryYEntry.get();

        this.loadTeamConfig();
        this.ores.load();
    }

    private void loadTeamConfig() {
        for (Team team : this.teams) {
            team.load();
        }
    }

    @Override
    public void save() {
        HyriBedWars.log("Saving configuration...");

        this.gameTypeEntry.set(this.gameType.name());

        this.waitingSpawnEntry.set(this.waitingSpawn);
        this.waitingSpawnPos1Entry.set(this.waitingSpawnPos1);
        this.waitingSpawnPos2Entry.set(this.waitingSpawnPos2);

        this.gameAreaPos1Entry.set(this.gameAreaPos1);
        this.gameAreaPos2Entry.set(this.gameAreaPos2);

        this.killLocEntry.set(this.killLoc);

        this.npcCoalLocEntry.set(this.npcCoalLoc);

        this.diamondLocsEntry.set(this.diamondLocs);
        this.emeraldLocsEntry.set(this.emeraldLocs);

        this.cancelOpenInventoryYEntry.set(this.cancelOpenInventoryY);

        this.saveTeamConfig();
        this.ores.save();

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
        return this.teams.stream().filter(team1 -> team1.getName().equals(teamName)).findFirst().get();
    }

    public BWGameType getGameType() {
        return this.gameType;
    }

    public Location getWaitingSpawn() {
        return this.waitingSpawn;
    }

    public Location getWaitingSpawnPos1() {
        return this.waitingSpawnPos1;
    }

    public Location getWaitingSpawnPos2() {
        return this.waitingSpawnPos2;
    }

    public Location getGameAreaPos1() {
        return this.gameAreaPos1;
    }

    public Location getGameAreaPos2() {
        return this.gameAreaPos2;
    }

    public Area getGameArea(){
        return new Area(this.gameAreaPos1, this.gameAreaPos2);
    }

    public Location getKillLoc() {
        return this.killLoc;
    }

    public Location getNPCCoalLocation() {
        return npcCoalLoc;
    }

    public List<Location> getDiamondLocations() {
        return this.diamondLocs;
    }

    public List<Location> getEmeraldLocations() {
        return this.emeraldLocs;
    }

    public double getCancelOpenInventoryY() {
        return this.cancelOpenInventoryY;
    }

    public Ores getOres() {
        return ores;
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

        public Area getBaseArea(){
            return new Area(this.baseAreaPos1, this.baseAreaPos2);
        }
    }

    public class Ores implements IHyriConfiguration{

        //BLOCKS

        private int woolPrice;
        private final IntegerEntry woolPriceEntry;
        private int hardClayPrice;
        private final IntegerEntry hardClayPriceEntry;
        private int glassPrice;
        private final IntegerEntry glassPriceEntry;
        private int endStonePrice;
        private final IntegerEntry endStonePriceEntry;
        private int ladderPrice;
        private final IntegerEntry ladderPriceEntry;
        private int planksPrice;
        private final IntegerEntry planksPriceEntry;
        private int obsidianPrice;
        private final IntegerEntry obsidianPriceEntry;

        //MELEE

        private int stoneSwordPrice;
        private final IntegerEntry stoneSwordPriceEntry;
        private int ironSwordPrice;
        private final IntegerEntry ironSwordPriceEntry;
        private int diamondSwordPrice;
        private final IntegerEntry diamondSwordPriceEntry;
        private int stickPrice;
        private final IntegerEntry stickPriceEntry;

        //ARMOR

        private int chainmailArmorPrice;
        private final IntegerEntry chainmailArmorPriceEntry;
        private int ironArmorPrice;
        private final IntegerEntry ironArmorPriceEntry;
        private int diamondArmorPrice;
        private final IntegerEntry diamondArmorPriceEntry;

        // TOOLS

        private int shearsPrice;
        private final IntegerEntry shearsPriceEntry;

        private int pickaxeTier1Price;
        private final IntegerEntry pickaxeTier1PriceEntry;
        private int pickaxeTier2Price;
        private final IntegerEntry pickaxeTier2PriceEntry;
        private int pickaxeTier3Price;
        private final IntegerEntry pickaxeTier3PriceEntry;
        private int pickaxeTier4Price;
        private final IntegerEntry pickaxeTier4PriceEntry;

        private int axeTier1Price;
        private final IntegerEntry axeTier1PriceEntry;
        private int axeTier2Price;
        private final IntegerEntry axeTier2PriceEntry;
        private int axeTier3Price;
        private final IntegerEntry axeTier3PriceEntry;
        private int axeTier4Price;
        private final IntegerEntry axeTier4PriceEntry;

        //RANGED

        private int arrowPrice;
        private final IntegerEntry arrowPriceEntry;
        private int bowPrice;
        private final IntegerEntry bowPriceEntry;
        private int bowPowerPrice;
        private final IntegerEntry bowPowerPriceEntry;
        private int bowPunchPrice;
        private final IntegerEntry bowPunchPriceEntry;

        //POTIONS

        private int potionSpeedPrice;
        private final IntegerEntry potionSpeedPriceEntry;
        private int potionJumpPrice;
        private final IntegerEntry potionJumpPriceEntry;
        private int potionInvisibilityPrice;
        private final IntegerEntry potionInvisibilityPriceEntry;

        //UTILITY

        private int goldenApplePrice;
        private final IntegerEntry goldenApplePriceEntry;
        private int bedBugPrice;
        private final IntegerEntry bedBugPriceEntry;
        private int dreamDefenderPrice;
        private final IntegerEntry dreamDefenderPriceEntry;
        private int fireballPrice;
        private final IntegerEntry fireballPriceEntry;
        private int tntPrice;
        private final IntegerEntry tntPriceEntry;
        private int enderPearlPrice;
        private final IntegerEntry enderPearlPriceEntry;
        private int waterPrice;
        private final IntegerEntry waterPriceEntry;
        private int bridgeEggPrice;
        private final IntegerEntry bridgeEggPriceEntry;
        private int magicMilkPrice;
        private final IntegerEntry magicMilkPriceEntry;
        private int spongePrice;
        private final IntegerEntry spongePriceEntry;
        private int popupTowerPrice;
        private final IntegerEntry popupTowerPriceEntry;

        public Ores(String key){
            key += ".";

            this.woolPrice = 4;
            this.woolPriceEntry = new IntegerEntry(key + "wool", config);
            this.hardClayPrice = 12;
            this.hardClayPriceEntry = new IntegerEntry(key + "hard_clay", config);
            this.glassPrice = 12;
            this.glassPriceEntry = new IntegerEntry(key + "glass", config);
            this.endStonePrice = 24;
            this.endStonePriceEntry = new IntegerEntry(key + "end_stone", config);
            this.ladderPrice = 4;
            this.ladderPriceEntry = new IntegerEntry(key + "ladder", config);
            this.planksPrice = 4;
            this.planksPriceEntry = new IntegerEntry(key + "planks", config);
            this.obsidianPrice = 4;
            this.obsidianPriceEntry = new IntegerEntry(key + "obsidian", config);

            this.stoneSwordPrice = 10;
            this.stoneSwordPriceEntry = new IntegerEntry(key + "stone_sword", config);
            this.ironSwordPrice = 7;
            this.ironSwordPriceEntry = new IntegerEntry(key + "iron_sword", config);
            this.diamondSwordPrice = 4;
            this.diamondSwordPriceEntry = new IntegerEntry(key + "diamond_sword", config);
            this.stickPrice = 5;
            this.stickPriceEntry = new IntegerEntry(key + "stick", config);

            this.chainmailArmorPrice = 30;
            this.chainmailArmorPriceEntry = new IntegerEntry(key + "chainmail_armor", config);
            this.ironArmorPrice = 12;
            this.ironArmorPriceEntry = new IntegerEntry(key + "iron_armor", config);
            this.diamondArmorPrice = 6;
            this.diamondArmorPriceEntry = new IntegerEntry(key + "diamond_armor", config);

            this.shearsPrice = 20;
            this.shearsPriceEntry = new IntegerEntry(key + "shears", config);

            this.pickaxeTier1Price = 10;
            this.pickaxeTier1PriceEntry = new IntegerEntry(key + "pickaxe_tier_1", config);
            this.pickaxeTier2Price = 10;
            this.pickaxeTier2PriceEntry = new IntegerEntry(key + "pickaxe_tier_2", config);
            this.pickaxeTier3Price = 3;
            this.pickaxeTier3PriceEntry = new IntegerEntry(key + "pickaxe_tier_3", config);
            this.pickaxeTier4Price = 6;
            this.pickaxeTier4PriceEntry = new IntegerEntry(key + "pickaxe_tier_4", config);

            this.axeTier1Price = 10;
            this.axeTier1PriceEntry = new IntegerEntry(key + "axe_tier_1", config);
            this.axeTier2Price = 10;
            this.axeTier2PriceEntry = new IntegerEntry(key + "axe_tier_2", config);
            this.axeTier3Price = 3;
            this.axeTier3PriceEntry = new IntegerEntry(key + "axe_tier_3", config);
            this.axeTier4Price = 6;
            this.axeTier4PriceEntry = new IntegerEntry(key + "axe_tier_4", config);

            this.arrowPrice = 2;
            this.arrowPriceEntry = new IntegerEntry(key + "arrow", config);
            this.bowPrice = 12;
            this.bowPriceEntry = new IntegerEntry(key + "bow", config);
            this.bowPowerPrice = 20;
            this.bowPowerPriceEntry = new IntegerEntry(key + "bow_power", config);
            this.bowPunchPrice = 6;
            this.bowPunchPriceEntry = new IntegerEntry(key + "bow_punch", config);

            this.potionSpeedPrice = 1;
            this.potionSpeedPriceEntry = new IntegerEntry(key + "potion_speed", config);
            this.potionJumpPrice = 1;
            this.potionJumpPriceEntry = new IntegerEntry(key + "potion_jump", config);
            this.potionInvisibilityPrice = 2;
            this.potionInvisibilityPriceEntry = new IntegerEntry(key + "potion_invisibility", config);

            this.goldenApplePrice = 3;
            this.goldenApplePriceEntry = new IntegerEntry(key + "golden_apple", config);
            this.bedBugPrice = 30;
            this.bedBugPriceEntry = new IntegerEntry(key + "bed_bug", config);
            this.dreamDefenderPrice = 120;
            this.dreamDefenderPriceEntry = new IntegerEntry(key + "dream_defender", config);
            this.fireballPrice = 40;
            this.fireballPriceEntry = new IntegerEntry(key + "fireball", config);
            this.tntPrice = 4;
            this.tntPriceEntry = new IntegerEntry(key + "tnt", config);
            this.enderPearlPrice = 4;
            this.enderPearlPriceEntry = new IntegerEntry(key + "ender_pearl", config);
            this.waterPrice = 3;
            this.waterPriceEntry = new IntegerEntry(key + "water", config);
            this.bridgeEggPrice = 1;
            this.bridgeEggPriceEntry = new IntegerEntry(key + "bridge_egg", config);
            this.magicMilkPrice = 4;
            this.magicMilkPriceEntry = new IntegerEntry(key + "magic_milk", config);
            this.spongePrice = 3;
            this.spongePriceEntry = new IntegerEntry(key + "sponge", config);
            this.popupTowerPrice = 24;
            this.popupTowerPriceEntry = new IntegerEntry(key + "popup_tower", config);
        }

        @Override
        public void create() {
            this.woolPriceEntry.setDefault(this.woolPrice);
            this.hardClayPriceEntry.setDefault(this.hardClayPrice);
            this.glassPriceEntry.setDefault(this.glassPrice);
            this.endStonePriceEntry.setDefault(this.endStonePrice);
            this.ladderPriceEntry.setDefault(this.ladderPrice);
            this.planksPriceEntry.setDefault(this.planksPrice);
            this.obsidianPriceEntry.setDefault(this.obsidianPrice);

            this.stoneSwordPriceEntry.setDefault(this.stoneSwordPrice);
            this.ironSwordPriceEntry.setDefault(this.ironSwordPrice);
            this.diamondSwordPriceEntry.setDefault(this.diamondSwordPrice);
            this.stickPriceEntry.setDefault(this.stickPrice);

            this.chainmailArmorPriceEntry.setDefault(this.chainmailArmorPrice);
            this.ironArmorPriceEntry.setDefault(this.ironArmorPrice);
            this.diamondArmorPriceEntry.setDefault(this.diamondArmorPrice);

            this.shearsPriceEntry.setDefault(this.shearsPrice);

            this.pickaxeTier1PriceEntry.setDefault(this.pickaxeTier1Price);
            this.pickaxeTier2PriceEntry.setDefault(this.pickaxeTier2Price);
            this.pickaxeTier3PriceEntry.setDefault(this.pickaxeTier3Price);
            this.pickaxeTier4PriceEntry.setDefault(this.pickaxeTier4Price);

            this.axeTier1PriceEntry.setDefault(this.axeTier1Price);
            this.axeTier2PriceEntry.setDefault(this.axeTier2Price);
            this.axeTier3PriceEntry.setDefault(this.axeTier3Price);
            this.axeTier4PriceEntry.setDefault(this.axeTier4Price);

            this.arrowPriceEntry.setDefault(this.arrowPrice);
            this.bowPriceEntry.setDefault(this.bowPrice);
            this.bowPowerPriceEntry.setDefault(this.bowPowerPrice);
            this.bowPunchPriceEntry.setDefault(this.bowPunchPrice);

            this.potionSpeedPriceEntry.setDefault(this.potionSpeedPrice);
            this.potionJumpPriceEntry.setDefault(this.potionJumpPrice);
            this.potionInvisibilityPriceEntry.setDefault(this.potionInvisibilityPrice);

            this.goldenApplePriceEntry.setDefault(this.goldenApplePrice);
            this.bedBugPriceEntry.setDefault(this.bedBugPrice);
            this.dreamDefenderPriceEntry.setDefault(this.dreamDefenderPrice);
            this.fireballPriceEntry.setDefault(this.fireballPrice);
            this.tntPriceEntry.setDefault(this.tntPrice);
            this.enderPearlPriceEntry.setDefault(this.enderPearlPrice);
            this.waterPriceEntry.setDefault(this.waterPrice);
            this.bridgeEggPriceEntry.setDefault(this.bridgeEggPrice);
            this.magicMilkPriceEntry.setDefault(this.magicMilkPrice);
            this.spongePriceEntry.setDefault(this.spongePrice);
            this.popupTowerPriceEntry.setDefault(this.popupTowerPrice);
        }

        @Override
        public void load() {
            this.woolPrice = this.woolPriceEntry.get();
            this.hardClayPrice = this.hardClayPriceEntry.get();
            this.glassPrice = this.glassPriceEntry.get();
            this.endStonePrice = this.endStonePriceEntry.get();
            this.ladderPrice = this.ladderPriceEntry.get();
            this.planksPrice = this.planksPriceEntry.get();
            this.obsidianPrice = this.obsidianPriceEntry.get();

            this.stoneSwordPrice = this.stoneSwordPriceEntry.get();
            this.ironSwordPrice = this.ironSwordPriceEntry.get();
            this.diamondSwordPrice = this.diamondSwordPriceEntry.get();
            this.stickPrice = this.stickPriceEntry.get();

            this.chainmailArmorPrice = this.chainmailArmorPriceEntry.get();
            this.ironArmorPrice = this.ironArmorPriceEntry.get();
            this.diamondArmorPrice = this.diamondArmorPriceEntry.get();

            this.shearsPrice = this.shearsPriceEntry.get();

            this.pickaxeTier1Price = this.pickaxeTier1PriceEntry.get();
            this.pickaxeTier2Price = this.pickaxeTier2PriceEntry.get();
            this.pickaxeTier3Price = this.pickaxeTier3PriceEntry.get();
            this.pickaxeTier4Price = this.pickaxeTier4PriceEntry.get();

            this.axeTier1Price = this.axeTier1PriceEntry.get();
            this.axeTier2Price = this.axeTier2PriceEntry.get();
            this.axeTier3Price = this.axeTier3PriceEntry.get();
            this.axeTier4Price = this.axeTier4PriceEntry.get();

            this.arrowPrice = this.arrowPriceEntry.get();
            this.bowPrice = this.bowPriceEntry.get();
            this.bowPowerPrice = this.bowPowerPriceEntry.get();
            this.bowPunchPrice = this.bowPunchPriceEntry.get();

            this.potionSpeedPrice = this.potionSpeedPriceEntry.get();
            this.potionJumpPrice = this.potionJumpPriceEntry.get();
            this.potionInvisibilityPrice = this.potionInvisibilityPriceEntry.get();

            this.goldenApplePrice = this.goldenApplePriceEntry.get();
            this.bedBugPrice = this.bedBugPriceEntry.get();
            this.dreamDefenderPrice = this.dreamDefenderPriceEntry.get();
            this.fireballPrice = this.fireballPriceEntry.get();
            this.tntPrice = this.tntPriceEntry.get();
            this.enderPearlPrice = this.enderPearlPriceEntry.get();
            this.waterPrice = this.waterPriceEntry.get();
            this.bridgeEggPrice = this.bridgeEggPriceEntry.get();
            this.magicMilkPrice = this.magicMilkPriceEntry.get();
            this.spongePrice = this.spongePriceEntry.get();
            this.popupTowerPrice = this.popupTowerPriceEntry.get();
        }

        @Override
        public void save() {
            this.woolPriceEntry.set(this.woolPrice);
            this.hardClayPriceEntry.set(this.hardClayPrice);
            this.glassPriceEntry.set(this.glassPrice);
            this.endStonePriceEntry.set(this.endStonePrice);
            this.ladderPriceEntry.set(this.ladderPrice);
            this.planksPriceEntry.set(this.planksPrice);
            this.obsidianPriceEntry.set(this.obsidianPrice);

            this.stoneSwordPriceEntry.set(this.stoneSwordPrice);
            this.ironSwordPriceEntry.set(this.ironSwordPrice);
            this.diamondSwordPriceEntry.set(this.diamondSwordPrice);
            this.stickPriceEntry.set(this.stickPrice);

            this.chainmailArmorPriceEntry.set(this.chainmailArmorPrice);
            this.ironArmorPriceEntry.set(this.ironArmorPrice);
            this.diamondArmorPriceEntry.set(this.diamondArmorPrice);

            this.shearsPriceEntry.set(this.shearsPrice);

            this.pickaxeTier1PriceEntry.set(this.pickaxeTier1Price);
            this.pickaxeTier2PriceEntry.set(this.pickaxeTier2Price);
            this.pickaxeTier3PriceEntry.set(this.pickaxeTier3Price);
            this.pickaxeTier4PriceEntry.set(this.pickaxeTier4Price);

            this.axeTier1PriceEntry.set(this.axeTier1Price);
            this.axeTier2PriceEntry.set(this.axeTier2Price);
            this.axeTier3PriceEntry.set(this.axeTier3Price);
            this.axeTier4PriceEntry.set(this.axeTier4Price);

            this.arrowPriceEntry.set(this.arrowPrice);
            this.bowPriceEntry.set(this.bowPrice);
            this.bowPowerPriceEntry.set(this.bowPowerPrice);
            this.bowPunchPriceEntry.set(this.bowPunchPrice);

            this.potionSpeedPriceEntry.set(this.potionSpeedPrice);
            this.potionJumpPriceEntry.set(this.potionJumpPrice);
            this.potionInvisibilityPriceEntry.set(this.potionInvisibilityPrice);

            this.goldenApplePriceEntry.set(this.goldenApplePrice);
            this.bedBugPriceEntry.set(this.bedBugPrice);
            this.dreamDefenderPriceEntry.set(this.dreamDefenderPrice);
            this.fireballPriceEntry.set(this.fireballPrice);
            this.tntPriceEntry.set(this.tntPrice);
            this.enderPearlPriceEntry.set(this.enderPearlPrice);
            this.waterPriceEntry.set(this.waterPrice);
            this.bridgeEggPriceEntry.set(this.bridgeEggPrice);
            this.magicMilkPriceEntry.set(this.magicMilkPrice);
            this.spongePriceEntry.set(this.spongePrice);
            this.popupTowerPriceEntry.set(this.popupTowerPrice);
        }

        @Override
        public FileConfiguration getConfig() {
            return config;
        }

        public int getArrowPrice() {
            return arrowPrice;
        }

        public int getDiamondSwordPrice() {
            return diamondSwordPrice;
        }

        public int getEndStonePrice() {
            return endStonePrice;
        }

        public int getGlassPrice() {
            return glassPrice;
        }

        public int getHardClayPrice() {
            return hardClayPrice;
        }

        public int getIronSwordPrice() {
            return ironSwordPrice;
        }

        public int getLadderPrice() {
            return ladderPrice;
        }

        public int getObsidianPrice() {
            return obsidianPrice;
        }

        public int getPlanksPrice() {
            return planksPrice;
        }

        public int getAxeTier1Price() {
            return axeTier1Price;
        }

        public int getChainmailArmorPrice() {
            return chainmailArmorPrice;
        }

        public int getStoneSwordPrice() {
            return stoneSwordPrice;
        }

        public int getIronArmorPrice() {
            return ironArmorPrice;
        }

        public int getAxeTier2Price() {
            return axeTier2Price;
        }

        public int getStickPrice() {
            return stickPrice;
        }

        public int getWoolPrice() {
            return woolPrice;
        }

        public int getAxeTier3Price() {
            return axeTier3Price;
        }

        public int getBowPrice() {
            return bowPrice;
        }

        public int getFireballPrice() {
            return fireballPrice;
        }

        public int getShearsPrice() {
            return shearsPrice;
        }

        public int getPotionSpeedPrice() {
            return potionSpeedPrice;
        }

        public int getGoldenApplePrice() {
            return goldenApplePrice;
        }

        public int getDreamDefenderPrice() {
            return dreamDefenderPrice;
        }

        public int getEnderPearlPrice() {
            return enderPearlPrice;
        }

        public int getBridgeEggPrice() {
            return bridgeEggPrice;
        }

        public int getAxeTier4Price() {
            return axeTier4Price;
        }

        public int getBedBugPrice() {
            return bedBugPrice;
        }

        public int getPickaxeTier2Price() {
            return pickaxeTier2Price;
        }

        public int getBowPowerPrice() {
            return bowPowerPrice;
        }

        public int getPickaxeTier1Price() {
            return pickaxeTier1Price;
        }

        public int getPickaxeTier4Price() {
            return pickaxeTier4Price;
        }

        public int getPopupTowerPrice() {
            return popupTowerPrice;
        }

        public int getPotionInvisibilityPrice() {
            return potionInvisibilityPrice;
        }

        public int getSpongePrice() {
            return spongePrice;
        }

        public int getMagicMilkPrice() {
            return magicMilkPrice;
        }

        public int getDiamondArmorPrice() {
            return diamondArmorPrice;
        }

        public int getTntPrice() {
            return tntPrice;
        }

        public int getPickaxeTier3Price() {
            return pickaxeTier3Price;
        }

        public int getWaterPrice() {
            return waterPrice;
        }

        public int getBowPunchPrice() {
            return bowPunchPrice;
        }

        public int getPotionJumpPrice() {
            return potionJumpPrice;
        }
    }
}
