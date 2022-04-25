package fr.hyriode.bedwars.game.team;

import fr.hyriode.api.settings.HyriLanguage;
import fr.hyriode.bedwars.game.BWGameOre;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.generator.BWBaseGoldGenerator;
import fr.hyriode.bedwars.game.generator.BWBaseIronGenerator;
import fr.hyriode.bedwars.game.generator.BWEmeraldGenerator;
import fr.hyriode.bedwars.game.team.upgrade.BWTeamUpgrades;
import fr.hyriode.bedwars.game.team.upgrade.BWUpgradeTier;
import fr.hyriode.bedwars.game.team.upgrade.EBWUpgrades;
import fr.hyriode.bedwars.game.team.upgrade.traps.BWTeamTraps;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.game.team.HyriGameTeam;
import fr.hyriode.hyrame.generator.HyriGenerator;
import fr.hyriode.hyrame.generator.IHyriGeneratorTier;
import fr.hyriode.hyrame.utils.Area;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.configuration.HyriBWConfiguration;
import fr.hyriode.hyrame.utils.BroadcastUtil;
import org.bukkit.*;
import org.bukkit.entity.EnderDragon;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;
import java.util.function.Consumer;

public class BWGameTeam extends HyriGameTeam {

    private boolean hasBed;

    private final HyriBedWars plugin;

    private final BWTeamUpgrades upgrades;
    private final BWTeamTraps traps;

    private HyriGenerator ironGenerator;
    private HyriGenerator goldGenerator;

    private Location npcShopLocation;
    private Location npcUpgradeLocation;
    private Location generatorLocation;
    private Area baseArea;
    private Area protectArea;

    public BWGameTeam(EBWGameTeam team, int teamSize, HyriBedWars plugin) {
        super(plugin.getGame(), team.getName(), team.getDisplayName(), team.getColor(), teamSize);
        this.plugin = plugin;
        this.upgrades = new BWTeamUpgrades(plugin, this);
        this.traps = new BWTeamTraps(this);
        this.init();
    }

    public void init() {
        this.hasBed = true;

        System.out.println(this.getName());

        HyriBWConfiguration.Team teamConfig = this.plugin.getConfiguration().getTeam(this.getName());

        this.setSpawnLocation(teamConfig.getRespawnLocation());

        this.npcShopLocation = teamConfig.getShopNPCLocation();
        this.npcUpgradeLocation = teamConfig.getUpgradeNPCLocation();

        this.generatorLocation = teamConfig.getGeneratorLocation();

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
        return this.getPlayers().size() < 1;
    }

    public void baseArea(Consumer<Location> execute){
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
                    final Location loc = new Location(IHyrame.WORLD.get(), x, y, z);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            execute.accept(loc);
                        }
                    }.runTask(this.plugin);
                }
            }
        }
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

    public BWTeamTraps getTraps() {
        return traps;
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

    public HyriGenerator getIronGenerator() {
        return ironGenerator;
    }

    public HyriGenerator getGoldGenerator() {
        return goldGenerator;
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

    public void destroyBed(BWGamePlayer breaker) {
        this.setHasBed(false);
        if(breaker != null) {
            breaker.addBedsBroken();
        }
        BroadcastUtil.broadcast(player -> "  ");
        BroadcastUtil.broadcast(player -> ChatColor.BOLD + "BED DESTRUCTION > " +
                ChatColor.RESET + this.getColor().getChatColor() + this.getDisplayName().getForPlayer(player) + " Bed" +
                ChatColor.GRAY + " was destroyed by " + breaker.getTeam().getColor().getChatColor() + breaker.getPlayer().getName());
        BroadcastUtil.broadcast(player -> "  ");
        this.sendTitle(player -> ChatColor.RED + "BED DESTROYED!", player -> "You will no longer respawn!", 10, 3 * 20, 10);
    }

    public void spawnEnderDragon(){
        EnderDragon enderDragon = IHyrame.WORLD.get().spawn(this.getSpawnLocation(), EnderDragon.class);
        enderDragon.setCustomName(this.getColor().getChatColor() + HyriBedWars.getLanguageManager().getValue(HyriLanguage.EN, "team.name") + " " + this.getDisplayName().getValue(HyriLanguage.EN));
    }

    public void removeBed() {
        this.baseArea(loc -> {
            if(loc.getBlock().getType() == Material.BED_BLOCK){
                loc.getBlock().setType(Material.AIR);
            }
        });
    }
}
