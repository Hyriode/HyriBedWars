package fr.hyriode.bedwars.game.team;

import fr.hyriode.bedwars.game.BWGameOre;
import fr.hyriode.bedwars.game.generator.BWBaseGoldGenerator;
import fr.hyriode.bedwars.game.generator.BWBaseIronGenerator;
import fr.hyriode.bedwars.game.generator.BWEmeraldGenerator;
import fr.hyriode.bedwars.game.team.upgrade.BWTeamUpgrades;
import fr.hyriode.bedwars.game.team.upgrade.BWUpgradeTier;
import fr.hyriode.bedwars.game.team.upgrade.EBWUpgrades;
import fr.hyriode.hyrame.game.team.HyriGameTeam;
import fr.hyriode.hyrame.generator.HyriGenerator;
import fr.hyriode.hyrame.generator.IHyriGeneratorTier;
import fr.hyriode.hyrame.utils.Area;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.configuration.HyriBWConfiguration;
import org.bukkit.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class BWGameTeam extends HyriGameTeam {

    private boolean hasBed;

    private final HyriBedWars plugin;

    private final BWTeamUpgrades upgrades;

    private HyriGenerator ironGenerator;
    private HyriGenerator goldGenerator;

    private Location npcShopLocation;
    private Location npcUpgradeLocation;
    private Location generatorLocation;
    private Area baseArea;
    private Area protectArea;

    public BWGameTeam(EBWGameTeam team, int teamSize, HyriBedWars plugin) {
        super(team.getName(), team.getDisplayName(), team.getColor(), teamSize);
        this.plugin = plugin;
        this.upgrades = new BWTeamUpgrades(this);
        this.init();
    }

    public void init() {
        this.hasBed = true;

        HyriBWConfiguration.Team teamConfig = this.plugin.getConfiguration().getTeam(this.getName());

        this.setSpawnLocation(teamConfig.getRespawnLocation());

        this.npcShopLocation = teamConfig.getShopNPCLocation();
        this.npcUpgradeLocation = teamConfig.getUpgradeNPCLocation();

        this.generatorLocation = teamConfig.getGeneratorLocation();

        this.baseArea = new Area(teamConfig.getBaseAreaPos1(), teamConfig.getBaseAreaPos2());
        this.protectArea = new Area(teamConfig.getBaseAreaProtectionPos1(), teamConfig.getBaseAreaProtectionPos2());

    }

    public void startHealPoolUpgrade(){
        if(this.upgrades.containsUpgrade(EBWUpgrades.HEAL_POOL)){
            new BukkitRunnable(){
                @Override
                public void run() {
                    Location locMin = baseArea.getMin();
                    int miX = locMin.getBlockX();
                    int miY = locMin.getBlockY();
                    int miZ = locMin.getBlockZ();
                    Location locMax = baseArea.getMax();
                    int maX = locMax.getBlockX();
                    int maY = locMax.getBlockY();
                    int maZ = locMax.getBlockZ();
                    for(int x = miX ; x < maX ; ++x){
                        for(int y = miY ; y < maY ; ++y){
                            for(int z = miZ ; z < maZ ; ++z){
                                Random random = new Random();
                                int rand = random.nextInt(500);
                                if(rand == 1)
                                Bukkit.getWorld("world").playEffect(new Location(Bukkit.getWorld("world"), x, y, z), Effect.HEART, 10);
                            }
                        }
                    }
                    players.forEach(player -> {
                        if(baseArea.isInArea(player.getPlayer().getLocation())){
                            player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 2, 1));
                        }
                    });
                }
            }.runTaskTimer(this.plugin, 0L, 40L);
        }
    }

    public void updateGenerator(){
        if(this.upgrades.containsUpgrade(EBWUpgrades.FORGE)){
            BWUpgradeTier currentTier = this.upgrades.getCurrentUpgradeTier(EBWUpgrades.FORGE);
            switch (currentTier.getTier()){
                case 0:
                    this.ironGenerator.upgrade(BWBaseIronGenerator.BASE_II);
                    this.goldGenerator.upgrade(BWBaseGoldGenerator.BASE_II);
                    break;
                case 1:
                    this.ironGenerator.upgrade(BWBaseIronGenerator.BASE_III);
                    this.goldGenerator.upgrade(BWBaseGoldGenerator.BASE_III);
                    break;
                case 2:
                    HyriGenerator emeraldGenerator = new HyriGenerator.Builder(this.plugin, this.generatorLocation, BWEmeraldGenerator.EMERALD_TIER_I)
                            .withItem(BWGameOre.EMERALD.getItemStack()).build();
                    emeraldGenerator.create();
                    break;
                case 3:
                    this.ironGenerator.upgrade(BWBaseIronGenerator.BASE_IV);
                    this.goldGenerator.upgrade(BWBaseGoldGenerator.BASE_IV);
                    break;
            }
        }
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
        return this.getPlayersPlaying().size() == 0;
    }

    public Area getBaseArea() {
        return baseArea;
    }

    public Area getProtectArea() {
        return protectArea;
    }

    public BWTeamUpgrades getUpgrades() {
        return upgrades;
    }

    public String getStateAsSymbol() {
        if(this.hasBed) {
            return ChatColor.GREEN + "✔";
        } else if(this.isEliminated()) {
            return ChatColor.RED + "✘";
        } else {
            return ChatColor.GREEN + String.valueOf(this.getPlayersPlaying().size());
        }
    }

    public Location getNPCShopLocation() {
        return npcShopLocation;
    }

    public Location getNPCUpgradeLocation() {
        return npcUpgradeLocation;
    }

    public Location getGeneratorLocation() {
        return generatorLocation;
    }

    public void setIronGenerator(HyriGenerator ironGenerator) {
        this.ironGenerator = ironGenerator;
    }

    public void setGoldGenerator(HyriGenerator goldGenerator) {
        this.goldGenerator = goldGenerator;
    }
}
