package fr.hyriode.bedwars.game;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.settings.HyriLanguage;
import fr.hyriode.bedwars.game.generator.BWBaseGoldGenerator;
import fr.hyriode.bedwars.game.generator.BWBaseIronGenerator;
import fr.hyriode.bedwars.game.generator.BWDiamondGenerator;
import fr.hyriode.bedwars.game.generator.BWEmeraldGenerator;
import fr.hyriode.bedwars.game.material.utility.entity.BedBugEntity;
import fr.hyriode.bedwars.game.material.utility.entity.DreamDefenderEntity;
import fr.hyriode.bedwars.game.npc.inventory.shop.pages.BWShopQuickBuy;
import fr.hyriode.bedwars.game.npc.inventory.upgrade.BWUpgradeGui;
import fr.hyriode.bedwars.game.start.items.BWGamePlayItem;
import fr.hyriode.bedwars.game.tasks.BWGameInventoryTask;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.game.HyriGame;
import fr.hyriode.hyrame.game.HyriGamePlayer;
import fr.hyriode.hyrame.game.protocol.HyriDeathProtocol;
import fr.hyriode.hyrame.game.protocol.HyriLastHitterProtocol;
import fr.hyriode.hyrame.game.protocol.HyriWaitingProtocol;
import fr.hyriode.hyrame.game.team.HyriGameTeam;
import fr.hyriode.hyrame.generator.HyriGenerator;
import fr.hyriode.hyrame.npc.NPC;
import fr.hyriode.hyrame.npc.NPCManager;
import fr.hyriode.hyrame.npc.NPCSkin;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.api.player.HyriBWPlayer;
import fr.hyriode.bedwars.game.event.BWNextEvent;
import fr.hyriode.bedwars.game.npc.BWNPCType;
import fr.hyriode.bedwars.game.scoreboard.BWGameScoreboard;
import fr.hyriode.bedwars.game.tasks.BWGameTask;
import fr.hyriode.bedwars.game.team.EBWGameTeam;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.*;
import java.util.stream.Collectors;

public class BWGame extends HyriGame<BWGamePlayer> {

    private final HyriBedWars plugin;
    private final BWGameType gameType;
    private BWNextEvent actualEvent;

    private final List<HyriGenerator> diamondGenerators = new ArrayList<>();
    private final List<HyriGenerator> emeraldGenerators = new ArrayList<>();

    private BWGameTask task;

    private final List<DreamDefenderEntity> dreamDefenders = new ArrayList<>();
    private final List<BedBugEntity> bedBugs = new ArrayList<>();

    public BWGame(IHyrame hyrame, HyriBedWars plugin) {
        super(hyrame, plugin, "bedwars", HyriBedWars.NAME, BWGamePlayer.class);

        this.plugin = plugin;
        this.gameType = this.plugin.getConfiguration().getGameType();

        this.minPlayers = this.gameType.getMinPlayers();
        this.maxPlayers = this.gameType.getMaxPlayers();

        this.registerTeams();
    }

    private void registerTeams() {
        Arrays.asList(EBWGameTeam.values()).forEach(
                gameTeam -> this.registerTeam(new BWGameTeam(gameTeam, this.gameType.getTeamsSize(), this.plugin)));
    }

    @Override
    public void handleLogin(Player p) {
        super.handleLogin(p);
        p.getInventory().setArmorContents(null);
        p.getInventory().clear();
        p.setGameMode(GameMode.ADVENTURE);
        p.setFoodLevel(20);
        p.setHealth(20);
        p.setLevel(0);
        p.setExp(0.0F);
        p.teleport(this.plugin.getConfiguration().getWaitingSpawn());
        p.getActivePotionEffects().forEach(potionEffect -> p.removePotionEffect(potionEffect.getType()));

        this.getPlayer(p.getUniqueId()).handleLogin(this.plugin);

        HyriBWPlayer account = this.plugin.getAPI().getPlayerManager().getPlayer(p.getUniqueId());

        if(account == null){
            account = new HyriBWPlayer(p.getUniqueId());
        }

        this.getPlayer(p.getUniqueId()).setAccount(account);

        Bukkit.getScheduler().runTaskLater(this.plugin , () -> this.hyrame.getItemManager().giveItem(p, 4, BWGamePlayItem.class), 1);
    }

    @Override
    public void handleLogout(Player p) {
        if(this.getPlayer(p) != null) {
            BWGameTeam team = this.getPlayer(p).getHyriTeam();

            super.handleLogout(p);

            if (team != null && team.getPlayers().isEmpty()) {
                team.setHasBed(false);
                this.win();
                return;
            }
        }
        if(this.players.isEmpty()){
            this.end();
        }
    }

    @Override
    public void start() {
        super.start();

        final HyriDeathProtocol.Options.YOptions yOptions = new HyriDeathProtocol.Options.YOptions(20);

        this.protocolManager.enableProtocol(new HyriLastHitterProtocol(this.hyrame, this.plugin, 8 * 20L));
        this.protocolManager.enableProtocol(new HyriDeathProtocol(this.hyrame, this.plugin, gamePlayer -> {
            final Player player = gamePlayer.getPlayer();

            if(MetadataReferences.isInvisible(player)){
                player.removeMetadata(MetadataReferences.ISINVISIBLE, this.plugin);
            }

            player.teleport(this.plugin.getConfiguration().getKillLoc());

            return this.getPlayer(player).kill();
        }, this.createDeathScreen(), HyriDeathProtocol.ScreenHandler.Default.class)
                .withOptions(new HyriDeathProtocol.Options().withYOptions(yOptions)));

        this.setActualEvent(BWNextEvent.START);
        this.updateBed();
        this.updateAPI();

        this.spawnNPCs();

        this.spawnGenerators();

        this.task = new BWGameTask(this.plugin);
        this.task.runTaskTimerAsynchronously(this.plugin, 0, 20);

        BWGameInventoryTask taskInventory = new BWGameInventoryTask(this.plugin);
        taskInventory.runTaskTimer(this.plugin, 0, 2);

        this.teleportTeams();

        this.setScoreboardForPlayers();
    }

    public List<BWGameTeam> getTeamsTracker(BWGameTeam ignored){
        return this.teams.stream().filter(team -> !((BWGameTeam) team).isEliminated() && !team.getName().equals(ignored.getName())).map(team -> (BWGameTeam) team).collect(Collectors.toList());
    }

    /**
     * Example to execute :
     *
     * @code Bukkit.getScheduler().runTaskLater(plugin, spawnFirework(player.getLocation().add(0, 1, 0))::detonate, 2);
     * */
    private static Firework spawnFirework(Location loc){
        final Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        final FireworkMeta meta = fw.getFireworkMeta();


        meta.setPower(2);
        meta.addEffect(FireworkEffect.builder().withColor(Color.AQUA, Color.YELLOW, Color.BLUE, Color.RED, Color.GREEN).build());

        fw.setFireworkMeta(meta);
        return fw;
    }

    public void win(){
        int eliminated = 0;
        for (HyriGameTeam team : this.teams) {
            if(((BWGameTeam) team).isEliminated())
                ++eliminated;
        }
        if(eliminated == this.gameType.getMaxTeams() - 1){
            this.teams.stream().filter(team -> !((BWGameTeam) team).isEliminated()).findFirst().ifPresent(this::win);
        }
        this.players.forEach(player -> {
            if(player != null)
                player.getScoreboard().update();
        });
    }

    @Override
    public void win(HyriGameTeam winner) {
        System.out.println("Game done");
        super.win(winner);
    }

    @Override
    public void postRegistration() {
        super.postRegistration();

        this.protocolManager.getProtocol(HyriWaitingProtocol.class).withTeamSelector(true);
    }

    private HyriDeathProtocol.Screen createDeathScreen() {
        return new HyriDeathProtocol.Screen(5, victim -> {
            final BWGamePlayer gamePlayer = this.getPlayer(victim.getUniqueId());

            gamePlayer.setCooldownRespawn(true);
            victim.setGameMode(GameMode.SURVIVAL);
            victim.teleport(this.plugin.getConfiguration().getTeam(gamePlayer.getTeam().getName()).getRespawnLocation());
            gamePlayer.respawn();
            gamePlayer.ignoreGenerators(false);
            victim.spigot().setCollidesWithEntities(true); //TODO à retirer
            victim.playSound(victim.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
            new BukkitRunnable(){
                int i = 0;
                @Override
                public void run() {
                    if(!gamePlayer.isCooldownRespawn() || i > 20*2){
                        gamePlayer.setCooldownRespawn(false);
                        this.cancel();
                    }
                    ++i;
                }
            }.runTaskTimer(this.plugin, 0, 1);
        });
    }

    private void updateBed(){
        this.teams.forEach(gameTeam -> {
            if(((BWGameTeam) gameTeam).isEliminated())
                ((BWGameTeam) gameTeam).setHasBed(false);
        });
    }

    private void updateAPI(){
        for (BWGamePlayer player : this.players) {
            if(this.plugin.getAPI().getPlayerManager().getPlayer(player.getUUID()) == null){
                HyriBWPlayer account = player.getAccount();
                account.resetQuickBuy();
                account.resetHotbar();
                account.update();
            }
        }
    }

    private void spawnNPCs(){
        for(HyriGameTeam team : this.plugin.getGame().getTeams()) {
            final Location locShop = ((BWGameTeam)team).getNPCShopLocation();
            final Location locUpgrade = ((BWGameTeam)team).getNPCUpgradeLocation();

            final NPCSkin npcShopSkin =
//                    this.getCosmeticByUpPlayersRank(team.getPlayers()) != null ?
//                    this.getCosmeticByUpPlayersRank(team.getPlayers()).getSkin() :
                    BWNPCType.SHOP.getDefaultSkin();
            final NPCSkin npcUpgradeSkin =
//                    this.getCosmeticByUpPlayersRank(team.getPlayers()) != null ?
//                    this.getCosmeticByUpPlayersRank(team.getPlayers()).getSkin() :
                    BWNPCType.UPGRADE.getDefaultSkin();

            for (HyriGamePlayer player : this.players) {
                final HyriLanguage language = HyriAPI.get().getPlayerSettingsManager()
                        .getPlayerSettings(player.getUUID()).getLanguage();
                final NPC shop = NPCManager.createNPC(locShop, npcShopSkin, BWNPCType.SHOP.getLore(language));
                final NPC upgrade = NPCManager.createNPC(locUpgrade, npcUpgradeSkin, BWNPCType.UPGRADE.getLore(language));

                shop.setShowingToAll(false);
                shop.addPlayer(player.getPlayer());
                shop.setInteractCallback((rightClick, clicker) -> {
                    BWGamePlayer bwPlayer = this.getPlayer(clicker);
                    if (rightClick && !bwPlayer.isDead() && !bwPlayer.isSpectator()) new BWShopQuickBuy(this.plugin, clicker).open();
                });

                upgrade.setShowingToAll(false);
                upgrade.addPlayer(player.getPlayer());
                upgrade.setInteractCallback((rightClick, clicker) -> {
                    BWGamePlayer bwPlayer = this.getPlayer(clicker);
                    if (rightClick && !bwPlayer.isDead() && !bwPlayer.isSpectator()) new BWUpgradeGui(this.plugin, clicker).open();
                });

                NPCManager.sendNPC(shop);
                NPCManager.sendNPC(upgrade);
            }
        }
    }

    private void spawnGenerators() {
        //Spawn Generators of Bases
        for(HyriGameTeam team : this.getTeams()){
            final BWGameTeam gameTeam = ((BWGameTeam)team);

            final Location loc = gameTeam.getGeneratorLocation();

            final HyriGenerator ironGenerator = new HyriGenerator.Builder(this.plugin, loc, BWBaseIronGenerator.BASE_I)
                    .withItem(BWGameOre.IRON.getItemStack()).build();
            ironGenerator.create();
            gameTeam.setIronGenerator(ironGenerator);

            final HyriGenerator goldGenerator = new HyriGenerator.Builder(this.plugin, loc, BWBaseGoldGenerator.BASE_I)
                    .withItem(BWGameOre.GOLD.getItemStack()).build();
            goldGenerator.create();
            gameTeam.setGoldGenerator(goldGenerator);

        }

        //Spawn Generator of the Middle

        for(Location loc : this.plugin.getConfiguration().getDiamondLocations()){
            final HyriGenerator diamondGenerator = new HyriGenerator.Builder(this.plugin, loc, BWDiamondGenerator.DIAMOND_TIER_I)
                    .withItem(BWGameOre.DIAMOND.getItemStack())
                    .withDefaultHeader(Material.DIAMOND_BLOCK, (player) -> ChatColor.AQUA + HyriBedWars.getLanguageManager().getValue(player, "generator.diamond"))
                    .build();
            diamondGenerator.create();
            this.diamondGenerators.add(diamondGenerator);
        }

        for(Location loc : this.plugin.getConfiguration().getEmeraldLocations()){
            final HyriGenerator emeraldGenerator = new HyriGenerator.Builder(this.plugin, loc, BWEmeraldGenerator.EMERALD_TIER_I)
                    .withItem(BWGameOre.EMERALD.getItemStack())
                    .withDefaultHeader(Material.EMERALD_BLOCK, (player) -> ChatColor.DARK_GREEN + HyriBedWars.getLanguageManager().getValue(player, "generator.emerald"))
                    .build();
            emeraldGenerator.create();
            this.emeraldGenerators.add(emeraldGenerator);
        }
    }

    private void teleportTeams() {
        this.teams.forEach(gameTeam -> gameTeam.getPlayers().forEach(p -> {
            Player player = p.getPlayer();

            Scoreboard s = player.getScoreboard();
            Objective h = s.getObjective("showheatlth") != null ? s.getObjective("showheatlth") : s.registerNewObjective("showheatlth", Criterias.HEALTH);
            h.setDisplaySlot(DisplaySlot.BELOW_NAME);
            h.setDisplayName(ChatColor.RED + "❤");

            player.teleport(this.plugin.getConfiguration().getTeam(gameTeam.getName()).getRespawnLocation());
            player.setGameMode(GameMode.SURVIVAL);
            ((BWGamePlayer) p).respawn();
        }));
        NPCManager.getNPCs().forEach((npc, hologram) -> npc.setCustomNameVisible(false));
    }

    private void setScoreboardForPlayers(){
        this.players.forEach(player -> {
            BWGameScoreboard scoreboard = new BWGameScoreboard(this.plugin, player.getPlayer());
            player.setScoreboard(scoreboard);
            scoreboard.show();
        });
    }

    public BWNextEvent getActualEvent() {
        return this.actualEvent;
    }

    public void setActualEvent(BWNextEvent actualEvent) {
        this.actualEvent = actualEvent;
    }

    public BWGameTask getTask() {
        return this.task;
    }

    public List<HyriGenerator> getDiamondGenerators() {
        return this.diamondGenerators;
    }

    public List<HyriGenerator> getEmeraldGenerators() {
        return this.emeraldGenerators;
    }

    public void addDreamDefender(DreamDefenderEntity dreamDefender) {
        this.dreamDefenders.add(dreamDefender);
    }

    public void removeDreamDefender(UUID uuid){
        this.dreamDefenders.removeIf(dreamDefender -> dreamDefender.getUniqueID().equals(uuid));
    }

    public List<DreamDefenderEntity> getDreamDefenders() {
        return dreamDefenders;
    }

    public DreamDefenderEntity getDreamDefender(UUID uuid){
        return this.dreamDefenders.stream().filter(dm -> dm.getUniqueID().equals(uuid)).findFirst().orElse(null);
    }

    public void addBedBug(BedBugEntity bedBug) {
        this.bedBugs.add(bedBug);
    }

    public void removeBedBug(UUID uuid){
        this.bedBugs.removeIf(bedBug -> bedBug.getUniqueID().equals(uuid));
    }

    public List<BedBugEntity> getBedBugs() {
        return bedBugs;
    }

    public BedBugEntity getBedBug(UUID uuid){
        return this.bedBugs.stream().filter(bb -> bb.getUniqueID().equals(uuid)).findFirst().orElse(null);
    }

    public boolean teamsHasBed() {
        return this.teams.stream().map(t -> (BWGameTeam) t).anyMatch(BWGameTeam::hasBed);
    }
}
