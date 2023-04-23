package fr.hyriode.bedwars.game.team;

import fr.hyriode.api.language.HyriLanguage;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.config.BWConfiguration;
import fr.hyriode.bedwars.game.generator.BWGenerator;
import fr.hyriode.bedwars.game.generator.GeneratorManager;
import fr.hyriode.bedwars.game.gui.manager.GuiManager;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.player.scoreboard.BWPlayerScoreboard;
import fr.hyriode.bedwars.game.shop.ShopCategory;
import fr.hyriode.bedwars.game.team.trap.TrapTeam;
import fr.hyriode.bedwars.game.team.upgrade.UpgradeTeam;
import fr.hyriode.bedwars.game.upgrade.UpgradeManager;
import fr.hyriode.bedwars.manager.pnj.BWNPCType;
import fr.hyriode.bedwars.manager.pnj.PNJ;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.game.HyriGamePlayer;
import fr.hyriode.hyrame.game.team.HyriGameTeam;
import fr.hyriode.hyrame.generator.HyriGenerator;
import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.hyrame.npc.NPC;
import fr.hyriode.hyrame.npc.NPCManager;
import fr.hyriode.hyrame.utils.Area;
import fr.hyriode.hyrame.utils.BroadcastUtil;
import fr.hyriode.hyrame.utils.block.Cuboid;
import org.bukkit.*;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Criterias;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BWGameTeam extends HyriGameTeam {

    private final HyriBedWars plugin;
    private final Supplier<BWConfiguration.Team> config;

    private boolean hasBed;

    private final Map<String, HyriGenerator> forgeGenerator = new HashMap<>();
    private final UpgradeTeam upgradeTeam;
    private final TrapTeam trapTeam;

    public BWGameTeam(BWGameTeamColor team, int teamSize, HyriBedWars plugin) {
        super(team.getName(), team.getDisplayName(), team.getColor(), teamSize);
        this.plugin = plugin;
        this.hasBed = true;
        this.config = () -> this.plugin.getConfiguration().getTeam(this.getName());

        this.upgradeTeam = new UpgradeTeam();
        this.trapTeam = new TrapTeam(this);
    }

    public Map<String, HyriGenerator> getForgeGenerator() {
        return this.forgeGenerator;
    }

    public void start() {
        this.createGenerator();
        if(this.isEliminated()) {
            this.breakBedWithBlock(false);
        }
        this.spawnNPC();

        this.teleportPlayers();
        this.getBWPlayers().forEach(player -> {
            this.createScoreboard(player);
            player.respawn(false);
        });
    }

    public void createGenerator() {
        BWGenerator.Tier tier = HyriBedWars.getGeneratorManager().getGeneratorByName(GeneratorManager.FORGE).getTier(0);
        tier.getDrops().forEach(drop -> {
            String name = drop.get().getDropName();
            this.forgeGenerator.put(name, tier.getGenerators(plugin, this.getConfig().getGeneratorLocation()).get(name));
        });
        this.createForgeGenerator();
    }

    private void createScoreboard(BWGamePlayer player){
        Player p = player.getPlayer();
        BWPlayerScoreboard scoreboard = new BWPlayerScoreboard(this.plugin, p);

        player.setScoreboard(scoreboard);
        scoreboard.show();

        this.showHeart(p);
    }

    private void showHeart(Player player){
        Scoreboard s = player.getScoreboard();
        Objective h = s.getObjective("showheatlth") != null
                ? s.getObjective("showheatlth")
                : s.registerNewObjective("showheatlth", Criterias.HEALTH);
        h.getScore(player.getName()).setScore(20);
        h.setDisplaySlot(DisplaySlot.BELOW_NAME);
        h.setDisplayName(ChatColor.RED + "❤");
    }

    private void teleportPlayers() {
        this.teleport(this.getConfig().getRespawnLocation());
    }

    private void spawnNPC(){
        for (BWGamePlayer player : this.plugin.getGame().getPlayers()) {
            Player pl = player.getPlayer();
            NPC npcShop = this.plugin.getGame().getNPCShop(this.getConfig().getShopNPCLocation(), pl);
            NPC npcUpgrade = this.plugin.getGame().getNPCUpgrade(this.getConfig().getUpgradeNPCLocation(), pl);

            NPCManager.sendNPC(npcShop);
            NPCManager.sendNPC(npcUpgrade);
            this.plugin.getGame().addNPC(pl, npcShop, npcUpgrade);
        }
    }

    private void createForgeGenerator(){
        this.getForgeGenerator().forEach((__, generator) -> {
            generator.create();
        });
    }

    public BWConfiguration.Team getConfig() {
        return config.get();
    }

    public List<BWGamePlayer> getBWPlayers(){
        return this.players.values().stream().map(player -> (BWGamePlayer) player).collect(Collectors.toList());
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
        Area area = this.getConfig().getBaseArea();
        return new Cuboid(area.getMin(), area.getMax());
    }

    public void breakBedWithBlock(boolean withMessage){
        this.getBase().forEach(block -> {
            if(block.getType() == Material.BED_BLOCK){
                block.setType(Material.AIR);
            }
        });
        if(!hasBed || !withMessage) return;
        this.breakBed();
    }

    public void breakBed() {
        this.breakBed(null);
    }

    public void breakBed(BWGamePlayer breaker){
        Function<Player, String> enemy = __ -> breaker.getTeam().getColor().getChatColor() + breaker.getPlayer().getName() + ChatColor.GRAY;
        Function<Player, String> team = p -> this.getColor().getChatColor() + this.getDisplayName().getValue(p) + ChatColor.GRAY;

        if(this.hasBed) {
            if(breaker != null){
                breaker.addBedsBroken(1);
            }
        }
        this.setHasBed(false);

        this.plugin.getGame().getOnlinePlayers().stream().map(HyriGamePlayer::getPlayer).forEach(p -> {
            p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0F, 1.0F);
        });

        this.sendTitle(p -> ChatColor.RED + "" + ChatColor.BOLD + HyriLanguageMessage.get("bed.broken.title").getValue(p),
                p -> ChatColor.GRAY + (breaker != null
                        ? HyriLanguageMessage.get("bed.broken.subtitle.player").getValue(p)
                        .replace("%enemy%", enemy.apply(p))
                        : HyriLanguageMessage.get("bed.broken.subtitle").getValue(p)),
                10, 40, 10);
        BroadcastUtil.broadcast(p -> breaker != null
                ? HyriLanguageMessage.get("bed.broken.message.player").getValue(p)
                .replace("%team%", team.apply(p))
                .replace("%enemy%", enemy.apply(p))
                : HyriLanguageMessage.get("bed.broken.message").getValue(p).replace("%team%", team.apply(p)));
        this.plugin.getGame().updateScoreboards();
    }

    public boolean hasDragonBuff(){
        return this.upgradeTeam.hasUpgrade(UpgradeManager.DRAGON_BUFF);
    }

    public void upgradeGenerator(int tier) {
        GeneratorManager gm = HyriBedWars.getGeneratorManager();

        BWGenerator bwGenerator = gm.getGeneratorByName(GeneratorManager.FORGE);

        if (tier + 1 == 3) {
            HyriGenerator newGenerator = bwGenerator.getTier(3)
                    .getGenerators(this.plugin, this.getConfig().getGeneratorLocation()).get("emerald");
            this.forgeGenerator.put("emerald", newGenerator);
            newGenerator.create();
            return;
        }

        this.forgeGenerator.forEach((name, generator) -> {
            generator.upgrade(bwGenerator.getTier(tier + 1).getDrop(name).get().getTier());
        });
    }

    public void spawnEnderDragon() {
        EnderDragon enderDragon = IHyrame.WORLD.get().spawn(this.getConfig().getRespawnLocation(), EnderDragon.class);
        enderDragon.setCustomName(this.getColor().getChatColor() + this.getDisplayName().getValue(HyriLanguage.EN));
    }

    public int getTotalKills() {
        int totalKills = 0;
        for (BWGamePlayer team : this.getBWPlayers()) {
            totalKills += team.getKills();
        }
        return totalKills;
    }
}
