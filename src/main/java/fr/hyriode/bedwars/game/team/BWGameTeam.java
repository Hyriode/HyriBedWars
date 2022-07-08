package fr.hyriode.bedwars.game.team;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.config.BWConfiguration;
import fr.hyriode.bedwars.game.BWGame;
import fr.hyriode.bedwars.game.generator.BWBaseGenerator;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.shop.ItemMoney;
import fr.hyriode.bedwars.game.team.trap.TrapTeam;
import fr.hyriode.bedwars.game.team.upgrade.UpgradeTeam;
import fr.hyriode.bedwars.game.upgrade.UpgradeManager;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.game.HyriGamePlayer;
import fr.hyriode.hyrame.game.team.HyriGameTeam;
import fr.hyriode.hyrame.generator.HyriGenerator;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.hyrame.title.Title;
import fr.hyriode.hyrame.utils.block.Cuboid;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
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
        this.trapTeam = new TrapTeam(this);

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
            this.getBase().iterator().forEachRemaining(block -> {
                --next[0];
                if(next[0] <= 0){
                    if(ThreadLocalRandom.current().nextInt(20) == 3) {
                        block.getLocation().getWorld().playEffect(block.getLocation(), Effect.HAPPY_VILLAGER, 0);
                    }
                    next[0] = originNext;
                }
            });
            this.getPlayersPlaying().stream().map(HyriGamePlayer::getPlayer).forEach(player -> {
                if(this.getConfig().getBaseArea().isInArea(player.getLocation())){
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40, 1));
                }
            });
        }, 20L, 20L);
    }

    public Cuboid getBase(){
        return new Cuboid(this.config.getBaseAreaPos1(), this.config.getBaseAreaPos2());
    }

    public void breakBedWithBlock(){
        this.getBase().forEach(block -> {
            if(block.getType() == Material.BED_BLOCK){
                block.setType(Material.AIR);
            }
        });
        if(!hasBed || this.isEliminated()) return;
        this.breakBed();
    }

    public void breakBed() {
        this.breakBed(null);
    }

    public void breakBed(BWGamePlayer player){
        Function<Player, String> enemy = p -> player.getTeam().getColor().getChatColor() + player.getPlayer().getName() + ChatColor.GRAY;
        Function<Player, String> team = p -> this.getColor().getChatColor() + this.getDisplayName().getForPlayer(p) + ChatColor.GRAY;

        this.setHasBed(false);
        this.plugin.getGame().getPlayers().forEach(bwPlayer -> {
            Player p = bwPlayer.getPlayer();
            p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.ENDERDRAGON_GROWL, 1.0F, 1.0F);
        });

        this.sendTitle(p -> ChatColor.RED + HyriLanguageMessage.get("bed.broken.title").getForPlayer(p),
                p -> ChatColor.GRAY + (player != null
                        ? HyriLanguageMessage.get("bed.broken.subtitle.player").getForPlayer(p)
                        .replace("%enemy%", enemy.apply(p))
                        : HyriLanguageMessage.get("bed.broken.subtitle").getForPlayer(p)),
                10, 40, 10);
        this.plugin.getGame().sendMessageToAll(p -> player != null
                ? HyriLanguageMessage.get("bed.broken.message.player").getForPlayer(p)
                .replace("%team%", team.apply(p))
                .replace("%enemy%", enemy.apply(p))
                : HyriLanguageMessage.get("bed.broken.message").getForPlayer(p).replace("%team%", team.apply(p)));
    }

    public boolean hasDragonBuff(){
        return this.upgradeTeam.hasUpgrade(UpgradeManager.DRAGON_BUFF);
    }

    public void upgradeGenerator(int tier) {
        if (tier == 2) {
            HyriGenerator generator = new HyriGenerator
                    .Builder(this.plugin, this.ironGenerator.getLocation(), BWBaseGenerator.TIER_EMERALD)
                    .withItem(new ItemStack(ItemMoney.EMERALD.getMaterial())).build();
            generator.create();
            return;
        }
        this.ironGenerator.upgrade(BWBaseGenerator.get(this.plugin.getGame().getType(), BWBaseGenerator.Type.IRON, tier + 1));
        this.goldGenerator.upgrade(BWBaseGenerator.get(this.plugin.getGame().getType(), BWBaseGenerator.Type.GOLD, tier + 1));

    }
}
