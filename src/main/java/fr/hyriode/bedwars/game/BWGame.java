package fr.hyriode.bedwars.game;

import com.avaje.ebean.SqlRow;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.leaderboard.IHyriLeaderboardProvider;
import fr.hyriode.api.leveling.NetworkLeveling;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.generator.GeneratorManager;
import fr.hyriode.bedwars.game.gui.manager.GuiManager;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.shop.ShopCategory;
import fr.hyriode.bedwars.game.task.BWGameTask;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.game.team.BWGameTeamColor;
import fr.hyriode.bedwars.game.type.BWGameType;
import fr.hyriode.bedwars.game.waiting.BWGamePlayItem;
import fr.hyriode.bedwars.host.BWGameValues;
import fr.hyriode.bedwars.manager.pnj.BWNPCType;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import fr.hyriode.hyrame.game.HyriGame;
import fr.hyriode.hyrame.game.HyriGameState;
import fr.hyriode.hyrame.game.HyriGameType;
import fr.hyriode.hyrame.game.protocol.HyriDeathProtocol;
import fr.hyriode.hyrame.game.protocol.HyriLastHitterProtocol;
import fr.hyriode.hyrame.game.team.HyriGameTeam;
import fr.hyriode.hyrame.game.util.HyriGameMessages;
import fr.hyriode.hyrame.game.util.HyriRewardAlgorithm;
import fr.hyriode.hyrame.game.waitingroom.HyriWaitingRoom;
import fr.hyriode.hyrame.generator.HyriGenerator;
import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.hyrame.npc.NPC;
import fr.hyriode.hyrame.npc.NPCManager;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BWGame extends HyriGame<BWGamePlayer> {

    private final HyriBedWars plugin;
    private BWEvent nextEvent;

    private final List<HyriGenerator> diamondGenerators;
    private final List<HyriGenerator> emeraldGenerators;

    private BWGameTask task;
    private Map<UUID/*player id*/, List<UUID/*npc id*/>> npcs = new HashMap<>();
    private boolean canBreakBed = true;
    private BWBedTask bedTask;


    public BWGame(HyriBedWars plugin) {
        super(plugin.getHyrame(),
                plugin,
                HyriAPI.get().getConfig().isDevEnvironment() ?
                        HyriAPI.get().getGameManager().createGameInfo("bedwars", "BedWars")
                        : HyriAPI.get().getGameManager().getGameInfo("bedwars"),
                BWGamePlayer.class,
                HyriAPI.get().getConfig().isDevEnvironment() ?
                        BWGameType.TRIO
                        : HyriGameType.getFromData(BWGameType.values())
        );
        this.plugin = plugin;
        this.diamondGenerators = new ArrayList<>();
        this.emeraldGenerators = new ArrayList<>();
        this.nextEvent = BWEvent.DIAMOND_GENERATOR_TIER_II;

        this.description = HyriLanguageMessage.get("game.description");
        this.reconnectionTime = 120;

        this.waitingRoom = new BWWaitingRoom(this, this.plugin::getConfiguration);
    }

    private void registerTeams() {
        for(int i = 0 ; i < ((BWGameType) this.type).getMaxTeams() ; ++i){
            this.registerTeam(new BWGameTeam(BWGameTeamColor.values()[i], this.getType().getTeamsSize(), this.plugin));
        }
    }

    @Override
    public void postRegistration() {
        super.postRegistration();

        this.registerTeams();
    }

    @Override
    public void handleLogin(Player p) {
        super.handleLogin(p);

        if(this.getState() == HyriGameState.WAITING || this.getState() == HyriGameState.READY) {
            this.getPlayer(p.getUniqueId()).handleLogin(this.plugin);

            this.hyrame.getItemManager().giveItem(p, 4, BWGamePlayItem.class);
            return;
        }

        checkNpc(p);
    }

    public void checkNpc(Player player) {
        for (UUID npcId : this.npcs.get(player.getUniqueId())) {
            NPCManager.getNPCs().stream().filter(npc -> npc.getUniqueID().equals(npcId)).forEach(npc -> {
                if(npc.getPlayers().isEmpty()) {
                    npc.addPlayer(player);
                }
            });
        }
    }

    @Override
    public void handleLogout(Player p) {
        BWGamePlayer player = this.getPlayer(p);
        player.update();
        super.handleLogout(p);

        if(this.getState() == HyriGameState.PLAYING
                && player.getBWTeam().hasBed() && player.getBWTeam().getOnlinePlayers().isEmpty()) {
            player.getBWTeam().breakBedWithBlock(true);
        }

        this.checkWin();
    }

    @Override
    public void win(HyriGameTeam winner) {
        super.win(winner);
        List<HyriLanguageMessage> positions = Arrays.asList(
                HyriLanguageMessage.get("message.game.end.1"),
                HyriLanguageMessage.get("message.game.end.2"),
                HyriLanguageMessage.get("message.game.end.3")
        );

        final List<BWGamePlayer> topKillers = new ArrayList<>(this.players);

        topKillers.sort((o1, o2) -> o2.getKills() - o1.getKills());

        final Function<Player, List<String>> killersLineProvider = player -> {
            final List<String> killersLine = new ArrayList<>();

            for (int i = 0; i <= 2; i++) {
                final String killerLine = HyriLanguageMessage.get("message.game.end.kills").getValue(player)
                        .replace("%position%", positions.get(i).getValue(player));

                if (topKillers.size() > i){
                    final BWGamePlayer topKiller = topKillers.get(i);

                    killersLine.add(killerLine.replace("%player%", topKiller.formatNameWithTeam())
                            .replace("%kills%", String.valueOf(topKiller.getKills())));
                    continue;
                }

                killersLine.add(killerLine.replace("%player%", HyriLanguageMessage.get("message.game.end.nobody")
                        .getValue(player)).replace("%kills%", "0"));
            }

            return killersLine;
        };

        for (Player player : Bukkit.getOnlinePlayers()) {
            final BWGamePlayer gamePlayer = this.getPlayer(player);

            if (gamePlayer == null) {
                player.spigot().sendMessage(HyriGameMessages.createWinMessage(this, player, winner, killersLineProvider.apply(player), null));
            }
        }

        this.players.forEach(gamePlayer -> {
            final boolean host = HyriAPI.get().getServer().getAccessibility() == HyggServer.Accessibility.HOST;
            final boolean isWinner = winner.contains(gamePlayer);

            if (!host){
                gamePlayer.update();
                gamePlayer.updateStatistics(isWinner);
            }

            IHyriPlayer hyriPlayer = gamePlayer.asHyriPlayer();
            final UUID playerId = gamePlayer.getUniqueId();
            final int kills = gamePlayer.getKills();

            final long hyris = host ? 0 : hyriPlayer.getHyris().add(HyriRewardAlgorithm.getHyris(kills, gamePlayer.getPlayTime(), isWinner)).withMessage(false).exec();
            final double xp = host ? 0 : hyriPlayer.getNetworkLeveling().addExperience(HyriRewardAlgorithm.getXP(kills, gamePlayer.getPlayTime(), isWinner));
            final String rewards = ChatColor.LIGHT_PURPLE + String.valueOf(hyris) + " Hyris " + ChatColor.GREEN + xp + " XP";

            if(!host) {
                final IHyriLeaderboardProvider provider = HyriAPI.get().getLeaderboardProvider();

                provider.getLeaderboard(NetworkLeveling.LEADERBOARD_TYPE, "bedwars-experience").incrementScore(playerId, xp);
                provider.getLeaderboard(HyriBedWars.ID, "kills").incrementScore(playerId, kills);
                provider.getLeaderboard(HyriBedWars.ID, "beds-destroyed").incrementScore(playerId, gamePlayer.getBedsBroken());

                if (isWinner) {
                    provider.getLeaderboard(HyriBedWars.ID, "victories").incrementScore(playerId, 1);
                }
            }

            hyriPlayer.update();

            if (gamePlayer.isOnline()) {
                final Player player = gamePlayer.getPlayer();

                player.spigot().sendMessage(HyriGameMessages.createWinMessage(this, player, winner, killersLineProvider.apply(player), rewards.toString()));
            } else if (HyriAPI.get().getPlayerManager().isOnline(playerId)) {
                HyriAPI.get().getPlayerManager().sendMessage(playerId, HyriGameMessages.createOfflineWinMessage(this, hyriPlayer, rewards.toString()));
            }
        });

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

            player.teleport(this.plugin.getConfiguration().getWaitingRoom().getSpawn().asBukkit());
            return this.getPlayer(player).kill();
        }, this.createDeathScreen(), HyriDeathProtocol.ScreenHandler.Default.class)
                .withOptions(new HyriDeathProtocol.Options().withYOptions(yOptions)));

        this.task = new BWGameTask(this.plugin);
        if(BWGameValues.BED_BREAKING_DELAY.get() != 0) {
            this.bedTask = new BWBedTask(this.plugin);
        }

        this.teleportTeams();
        this.createGenerators();
    }

    private void teleportTeams(){
        this.getBWTeams().forEach(BWGameTeam::start);
    }

    private void createGenerators(){
        GeneratorManager gm = HyriBedWars.getGeneratorManager();
        this.plugin.getConfiguration().getDiamondGeneratorLocations().forEach(loc -> {
            for (HyriGenerator generator : gm.getGeneratorByName(GeneratorManager.DIAMOND).getTier(0)
                    .getGenerators(this.plugin, loc).values()) {
                generator.create();
                this.diamondGenerators.add(generator);
            }
        });
        this.plugin.getConfiguration().getEmeraldGeneratorLocations()
                .forEach(loc -> {
                    for (HyriGenerator generator : gm.getGeneratorByName(GeneratorManager.EMERALD).getTier(0)
                            .getGenerators(this.plugin, loc).values()) {
                        generator.create();
                        this.emeraldGenerators.add(generator);
                    }
                });
    }

    private HyriDeathProtocol.Screen createDeathScreen() {
        return new HyriDeathProtocol.Screen(BWGameValues.RESPAWNING_DELAY.get(), victim -> {
            final BWGamePlayer gamePlayer = this.getPlayer(victim);
            if(gamePlayer == null || gamePlayer.isSpectator())
                return;
            victim.setGameMode(GameMode.SURVIVAL);
            victim.teleport(this.getPlayer(victim).getBWTeam().getConfig().getRespawnLocation());
            victim.playSound(victim.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
            gamePlayer.respawn(true);
        });
    }

    public NPC getNPCUpgrade(Location loc, Player pl) {
//        PNJ.Type skinUpgrade = player.getNPCSkin().getUpgrade().getSkinEntity();
//            if(skinUpgrade == PNJ.Type.NPC){
        NPC npcUpgrade = NPCManager.createNPC(loc,
                        BWNPCType.UPGRADE.getDefaultSkin(),
                        BWNPCType.UPGRADE.getLore(pl))
                .setShowingToAll(false)
                .addPlayer(pl)
                .setInteractCallback((rightClick, p) -> {
                    BWGamePlayer bwPlayer = this.plugin.getGame().getPlayer(p.getUniqueId());
                    if(rightClick && bwPlayer != null && !bwPlayer.isSpectator() && !bwPlayer.isDead())
                        GuiManager.openUpgradeGui(this.plugin, p);
                });

//            } else {
//                EntityInteractManager.createEntity(this.getConfig().getUpgradeNPCLocation(), skinUpgrade.getClassEntity(), BWNPCType.UPGRADE.getLore(pl))
//                        .setVisibleAll(false)
//                        .addPlayer(pl)
//                        .setInteractCallback((rightClick, p) -> {
//                            System.out.println("rightClick");
//                            System.out.println(p);
//                            BWGamePlayer bwPlayer = this.plugin.getGame().getPlayer(p.getUniqueId());
//                            System.out.println(bwPlayer);
//                            if(rightClick && !bwPlayer.isSpectator() && !bwPlayer.isDead()) GuiManager.openUpgradeGui(this.plugin, p);
//                        }).spawn();
//            }
        return npcUpgrade;
    }

    public NPC getNPCShop(Location loc, Player pl) {
//        PNJ.Type skinShop = player.getNPCSkin().getShop().getSkinEntity();
//            if(skinShop == PNJ.Type.NPC){
        NPC npcShop = NPCManager.createNPC(loc,
                        BWNPCType.SHOP.getDefaultSkin(),
                        BWNPCType.SHOP.getLore(pl))
                .setShowingToAll(false)
                .addPlayer(pl)
                .setInteractCallback((rightClick, p) -> {
                    BWGamePlayer bwPlayer = this.plugin.getGame().getPlayer(p.getUniqueId());
                    if (rightClick && bwPlayer != null && !bwPlayer.isSpectator() && !bwPlayer.isDead())
                        GuiManager.openShopGui(this.plugin, p, ShopCategory.QUICK_BUY);
                });
//            } else {
//                EntityInteractManager.createEntity(this.getConfig().getShopNPCLocation(), skinShop.getClassEntity(), BWNPCType.SHOP.getLore(pl))
//                        .setVisibleAll(false)
//                        .addPlayer(pl)
//                        .setInteractCallback((rightClick, p) -> {
//                            BWGamePlayer bwPlayer = this.plugin.getGame().getPlayer(p.getUniqueId());
//                            if (rightClick && !bwPlayer.isSpectator() && !bwPlayer.isDead())
//                                GuiManager.openShopGui(this.plugin, p, ShopCategory.QUICK_BUY);
//                        }).spawn();
//            }
        return npcShop;
    }

    public BWGameType getType(){
        return (BWGameType) this.type;
    }

    public List<BWGameTeam> getBWTeams(){
        return this.teams.stream().map(team -> (BWGameTeam) team).collect(Collectors.toList());
    }

    public List<BWGameTeam> getBWTeams(Predicate<BWGameTeam> filter){
        return this.getBWTeams().stream().filter(filter).collect(Collectors.toList());
    }

    public List<BWGameTeam> getPlayingTeam(){
        return this.getBWTeams(team -> !team.isEliminated());
    }

    public BWEvent getNextEvent() {
        return nextEvent;
    }

    public void setNextEvent(BWEvent nextEvent) {
        this.nextEvent = nextEvent;
    }

    public List<HyriGenerator> getDiamondGenerators() {
        return diamondGenerators;
    }

    public List<HyriGenerator> getEmeraldGenerators() {
        return emeraldGenerators;
    }

    public BWGameTask getTask() {
        return task;
    }

    public void checkWin() {
        if(this.plugin.getGame().getState() != HyriGameState.PLAYING) return;
        List<BWGameTeam> teams = this.getBWTeams().stream().filter(team -> !team.isEliminated()).collect(Collectors.toList());

        if(teams.size() <= 1 || this.nextEvent == null){
            if(teams.isEmpty()){
                this.end();
                return;
            }
            if(this.nextEvent == null) {
                BWGameTeam team = this.getTeamMostKills();
                if(team == null) {
                    team = teams.get(ThreadLocalRandom.current().nextInt(Math.max(teams.size() - 1, 0)));
                }
                this.win(team);
                return;
            }
            this.win(teams.get(0));
        }
    }

    private BWGameTeam getTeamMostKills(){
        return this.getBWTeams().stream().max(Comparator.comparingInt(BWGameTeam::getTotalKills)).orElse(null);
    }

    public BWEvent nextEvent() {
        return this.nextEvent = this.nextEvent.getNextEvent();
    }

    public void addNPC(Player pl, NPC... npc) {
        List<UUID> npcs = this.npcs.containsKey(pl.getUniqueId()) ? this.npcs.get(pl.getUniqueId()) : new ArrayList<>();
        npcs.addAll(Arrays.stream(npc).map(Entity::getUniqueID).collect(Collectors.toList()));
        this.npcs.put(pl.getUniqueId(), npcs);
    }

    public Map<UUID, List<UUID>> getNPCs() {
        return npcs;
    }

    public void setBedBreakable(boolean canBreakBed) {
        this.canBreakBed = canBreakBed;
    }

    public boolean isCanBreakBed() {
        return canBreakBed;
    }

    public void updateScoreboards() {
        for (BWGamePlayer onlinePlayer : this.getOnlinePlayers()) {
            onlinePlayer.getScoreboard().update();
        }
    }
}
