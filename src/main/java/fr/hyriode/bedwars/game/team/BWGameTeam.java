package fr.hyriode.bedwars.game.team;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.config.BWConfiguration;
import fr.hyriode.bedwars.game.BWGame;
import fr.hyriode.bedwars.game.generator.BWBaseGenerator;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.shop.ItemMoney;
import fr.hyriode.bedwars.game.team.trap.TrapTeam;
import fr.hyriode.bedwars.game.team.upgrade.UpgradeTeam;
import fr.hyriode.hyrame.game.team.HyriGameTeam;
import fr.hyriode.hyrame.generator.HyriGenerator;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.hyrame.title.Title;
import fr.hyriode.hyrame.utils.block.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class BWGameTeam extends HyriGameTeam {

    private final HyriBedWars plugin;
    private final BWConfiguration.Team config;

    private boolean hasBed;

    private final HyriGenerator ironGenerator;
    private final HyriGenerator goldGenerator;
    private final UpgradeTeam upgradeTeam;
    private final TrapTeam trapTeam;

    public BWGameTeam(BWGameTeamColor team, int teamSize, HyriBedWars plugin, BWGame game) {
        super(game, team.getName(), team.getDisplayName(), team.getColor(), teamSize);
        this.plugin = plugin;
        this.hasBed = true;
        this.config = this.plugin.getConfiguration().getTeam(this.getName());

        this.ironGenerator = new HyriGenerator.Builder(plugin, this.config.getGeneratorLocation(), BWBaseGenerator.get(game.getType(), BWBaseGenerator.Type.IRON, 1)).withItem(new ItemStack(ItemMoney.IRON.getMaterial())).build();
        this.goldGenerator = new HyriGenerator.Builder(plugin, this.config.getGeneratorLocation(), BWBaseGenerator.get(game.getType(), BWBaseGenerator.Type.GOLD, 1)).withItem(new ItemStack(ItemMoney.GOLD.getMaterial())).build();
        this.upgradeTeam = new UpgradeTeam();
        this.trapTeam = new TrapTeam();

        this.setSpawnLocation(this.config.getRespawnLocation());
    }

    public HyriGenerator getIronGenerator() {
        return ironGenerator;
    }

    public HyriGenerator getGoldGenerator() {
        return goldGenerator;
    }

    public BWConfiguration.Team getConfig() {
        return config;
    }

    public List<BWGamePlayer> getBWPlayers(){
        return this.players.stream().map(player -> (BWGamePlayer) player).collect(Collectors.toList());
    }

    public UpgradeTeam getUpgradeTeam() {
        return upgradeTeam;
    }

    public TrapTeam getTrapTeam() {
        return this.trapTeam;
    }

    public boolean hasBed(){
        return this.hasBed;
    }

    public void setHasBed(boolean hasBed) {
        this.hasBed = hasBed;
    }

    public boolean isEliminated() {
        return this.getPlayersPlaying().isEmpty();
    }

    public void startHealPool() {
        int originNext = 10;
        int[] next = {originNext};
        Bukkit.getScheduler().runTaskTimer(this.plugin, () -> {
            new Cuboid(this.config.getBaseAreaPos1(), this.config.getBaseAreaPos2())
                    .iterator().forEachRemaining(block -> {
                        --next[0];
                        if(next[0] <= 0){
                            if(ThreadLocalRandom.current().nextBoolean()) {
                                block.getLocation().getWorld().playEffect(block.getLocation(), Effect.HAPPY_VILLAGER, 0);
                            }
                            next[0] = originNext;
                        }
                    });
        }, 10L, 10L);
    }

    public void breakBed() {
        this.breakBed(null);
    }

    public void breakBed(BWGamePlayer player){
        this.setHasBed(false);
        this.sendTitle(p -> HyriLanguageMessage.get("bed.broken.title").getForPlayer(p),
                p -> player != null
                        ? HyriLanguageMessage.get("bed.broken.subtitle.player").getForPlayer(p)
                        .replace("%enemy%", player.getTeam().getColor().getChatColor() + player.getPlayer().getName())
                        : HyriLanguageMessage.get("bed.broken.subtitle").getForPlayer(p),
                10, 40, 10);
    }
}
