package fr.hyriode.bedwars.game;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.generator.GeneratorManager;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.task.BWGameTask;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.game.team.BWGameTeamColor;
import fr.hyriode.bedwars.game.type.BWGameType;
import fr.hyriode.bedwars.game.waiting.BWGamePlayItem;
import fr.hyriode.bedwars.utils.MetadataReferences;
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
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BWGame extends HyriGame<BWGamePlayer> {

    private final HyriBedWars plugin;
    private BWEvent nextEvent;

    private final List<HyriGenerator> diamondGenerators;
    private final List<HyriGenerator> emeraldGenerators;

    private final HyriWaitingRoom waitingRoom;

    private BWGameTask task;

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

        this.waitingRoom = new BWWaitingRoom(this, this.plugin);
        this.waitingRoom.setup();
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
        if(p.getName().toLowerCase().startsWith("krin")) {
            p.sendMessage("Oui mec 1");
        }

        this.getPlayer(p.getUniqueId()).handleLogin(this.plugin);

        this.hyrame.getItemManager().giveItem(p, 4, BWGamePlayItem.class);
    }

    @Override
    public void handleLogout(Player p) {
        this.getPlayer(p).update();
        super.handleLogout(p);
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

                    killersLine.add("Tes: " + killerLine.replace("%player%", topKiller.formatNameWithTeam())
                            .replace("%kills%", String.valueOf(topKiller.getKills())));
                    continue;
                }

                killersLine.add("Test: " + killerLine.replace("%player%", HyriLanguageMessage.get("message.game.end.nobody")
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
            gamePlayer.updateStatistics(gamePlayer.getTeam().equals(winner));

            final UUID playerId = gamePlayer.getUniqueId();
            final int kills = gamePlayer.getKills();
            final boolean isWinner = winner.contains(gamePlayer);

            final long hyris = HyriRewardAlgorithm.getHyris(kills, gamePlayer.getPlayTime(), isWinner);
            final double xp = HyriRewardAlgorithm.getXP(kills, gamePlayer.getPlayTime(), isWinner);
            final StringBuilder rewards = new StringBuilder();

            IHyriPlayer hyriPlayer = gamePlayer.asHyriPlayer();

            rewards.append(ChatColor.LIGHT_PURPLE + String.valueOf(hyriPlayer.getHyris().add(hyris).withMessage(false).exec()) + " Hyris");
            rewards.append(" ");
            rewards.append(ChatColor.GREEN + String.valueOf(hyriPlayer.getNetworkLeveling().addExperience(xp)) + " XP");

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

        this.waitingRoom.remove();

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
        return new HyriDeathProtocol.Screen(5, victim -> {
            final BWGamePlayer gamePlayer = this.getPlayer(victim);

            victim.setGameMode(GameMode.SURVIVAL);
            victim.teleport(this.getPlayer(victim).getBWTeam().getConfig().getRespawnLocation());
            victim.playSound(victim.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
            gamePlayer.respawn(true);
        });
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
        if(teams.size() <= 1){
            if(teams.isEmpty()){
                this.end();
                return;
            }
            this.win(teams.get(0));
        }
    }

    public BWEvent nextEvent() {
        return this.nextEvent = this.nextEvent.getNextEvent();
    }
}
