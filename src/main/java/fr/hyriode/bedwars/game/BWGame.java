package fr.hyriode.bedwars.game;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.settings.HyriLanguage;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.generator.BWDiamondGenerator;
import fr.hyriode.bedwars.game.generator.BWEmeraldGenerator;
import fr.hyriode.bedwars.game.gui.manager.GuiManager;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.player.scoreboard.BWPlayerScoreboard;
import fr.hyriode.bedwars.game.shop.ShopCategory;
import fr.hyriode.bedwars.game.task.BWGameTask;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.game.team.BWGameTeamColor;
import fr.hyriode.bedwars.game.test.BWGameInfo;
import fr.hyriode.bedwars.game.type.BWGameType;
import fr.hyriode.bedwars.game.waiting.BWGamePlayItem;
import fr.hyriode.bedwars.manager.pnj.BWNPCType;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.hyrame.game.HyriGame;
import fr.hyriode.hyrame.game.HyriGameState;
import fr.hyriode.hyrame.game.HyriGameType;
import fr.hyriode.hyrame.game.protocol.HyriDeathProtocol;
import fr.hyriode.hyrame.game.protocol.HyriLastHitterProtocol;
import fr.hyriode.hyrame.game.team.HyriGameTeam;
import fr.hyriode.hyrame.game.waitingroom.HyriWaitingRoom;
import fr.hyriode.hyrame.generator.HyriGenerator;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.hyrame.npc.NPCManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Criterias;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BWGame extends HyriGame<BWGamePlayer> {

    private final HyriBedWars plugin;
    private BWNextEvent currentEvent;

    private final List<HyriGenerator> diamondGenerators;
    private final List<HyriGenerator> emeraldGenerators;

    private final HyriWaitingRoom waitingRoom;

    private BWGameTask task;

    public BWGame(HyriBedWars plugin) {
        super(plugin.getHyrame(),
                plugin,
                HyriAPI.get().getConfiguration().isDevEnvironment() ?
                        new BWGameInfo("bedwars", "BedWars")
                        : HyriAPI.get().getGameManager().getGameInfo("bedwars"),
                BWGamePlayer.class,
                HyriAPI.get().getConfiguration().isDevEnvironment() ?
                        BWGameType.DOUBLES
                        : HyriGameType.getFromData(BWGameType.values())
        );
        this.plugin = plugin;
        this.diamondGenerators = new ArrayList<>();
        this.emeraldGenerators = new ArrayList<>();
        this.currentEvent = BWNextEvent.START;

        this.description = HyriLanguageMessage.get("game.description");

        this.registerTeams();

        this.waitingRoom = new HyriWaitingRoom(this, Material.BED, this.plugin.getConfiguration().getWaitingRoom());
        this.waitingRoom.setup();
    }

    private void registerTeams() {
        for(int i = 0 ; i < ((BWGameType) this.type).getMaxTeams() ; ++i){
            this.registerTeam(new BWGameTeam(BWGameTeamColor.values()[i], this.getType().getTeamsSize(), this.plugin, this));
        }
    }

    @Override
    public void handleLogin(Player p) {
        super.handleLogin(p);

        this.getPlayer(p.getUniqueId()).handleLogin(this.plugin);

        this.hyrame.getItemManager().giveItem(p, 4, BWGamePlayItem.class);

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

        long time = System.currentTimeMillis();
        this.teleportTeams();
        this.createGenerators();
        System.out.println("Temps de démarrage : " + (System.currentTimeMillis() - time));

    }

    private void teleportTeams(){
        this.players.forEach(player -> {
            this.createScoreboard(player.getPlayer());
            player.respawn(false);

        });
        this.getBWTeams().forEach(team -> {
            if(team.isEliminated()) {
                team.breakBedWithBlock();
            }
            this.createForgeGenerator(team);
            this.spawnNPC(team);
        });
    }

    private void createScoreboard(Player player){
        BWPlayerScoreboard scoreboard = new BWPlayerScoreboard(this.plugin, this, player);

        this.getPlayer(player.getUniqueId()).setScoreboard(scoreboard);
        scoreboard.show();

        this.showHeart(player);
    }

    private void showHeart(Player player){
        Scoreboard s = player.getScoreboard();
        Objective h = s.getObjective("showheatlth") != null ? s.getObjective("showheatlth") : s.registerNewObjective("showheatlth", Criterias.HEALTH);
        h.getScore(player.getName()).setScore(20);
        h.setDisplaySlot(DisplaySlot.BELOW_NAME);
        h.setDisplayName(ChatColor.RED + "❤");
    }

    private void spawnNPC(BWGameTeam team){
        for (Player player : this.players.stream().map(BWGamePlayer::getPlayer).collect(Collectors.toList())) {
            NPCManager.sendNPC(NPCManager.createNPC(team.getConfig().getShopNPCLocation(),
                            BWNPCType.SHOP.getDefaultSkin(),
                            BWNPCType.SHOP.getLore(HyriLanguage.EN))
                    .setShowingToAll(false)
                    .addPlayer(player)
                    .setInteractCallback((rightClick, p) -> {
                        if (rightClick) GuiManager.openShopGui(this.plugin, p, ShopCategory.QUICK_BUY);
                    }));
            NPCManager.sendNPC(NPCManager.createNPC(team.getConfig().getUpgradeNPCLocation(),
                            BWNPCType.UPGRADE.getDefaultSkin(),
                            BWNPCType.UPGRADE.getLore(HyriLanguage.EN))
                    .setShowingToAll(false)
                    .addPlayer(player)
                    .setInteractCallback((rightClick, p) -> {
                        if(rightClick) GuiManager.openUpgradeGui(this.plugin, p);
                    }));
        }
    }

    private void createForgeGenerator(BWGameTeam team){
        team.getIronGenerator().create();
        team.getGoldGenerator().create();
    }

    private void createGenerators(){
        this.plugin.getConfiguration().getDiamondGeneratorLocations().forEach(loc -> {
                    HyriGenerator generator = new HyriGenerator.Builder(this.plugin, loc, BWDiamondGenerator.TIER_I)
                            .withItem(new ItemStack(Material.DIAMOND))
                            .withDefaultHeader(Material.DIAMOND_BLOCK, (player) -> ChatColor.AQUA + "" + ChatColor.BOLD + HyriLanguageMessage.get("generator.diamond").getForPlayer(player))
                            .withDefaultAnimation().build();
                    generator.create();
                    this.diamondGenerators.add(generator);
        });
        this.plugin.getConfiguration().getEmeraldGeneratorLocations()
                .forEach(loc -> {
                    HyriGenerator generator = new HyriGenerator.Builder(this.plugin, loc, BWEmeraldGenerator.TIER_I)
                            .withItem(new ItemStack(Material.EMERALD))
                            .withDefaultHeader(Material.EMERALD_BLOCK, (player) -> ChatColor.DARK_GREEN + "" + ChatColor.BOLD + HyriLanguageMessage.get("generator.emerald").getForPlayer(player))
                            .withDefaultAnimation()
                            .build();
                    generator.create();
                    this.emeraldGenerators.add(generator);
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

    @Override
    public void handleLogout(Player p) {
        super.handleLogout(p);
        this.checkWin();
    }

    @Override
    public void win(HyriGameTeam winner) {
        super.win(winner);

    }

    public BWGameType getType(){
        return (BWGameType) this.type;
    }

    public List<BWGameTeam> getBWTeams(){
        return this.teams.stream().map(team -> (BWGameTeam) team).collect(Collectors.toList());
    }

    public BWNextEvent getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(BWNextEvent currentEvent) {
        this.currentEvent = currentEvent;
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
}
