package fr.hyriode.bedwars.game;

import fr.hyriode.bedwars.configuration.HyriBWConfiguration;
import fr.hyriode.bedwars.game.npc.inventory.BWUpgradeGui;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.game.HyriGame;
import fr.hyriode.hyrame.game.HyriGamePlayer;
import fr.hyriode.hyrame.game.team.HyriGameTeam;
import fr.hyriode.hyrame.game.util.HyriGameItems;
import fr.hyriode.hyrame.generator.HyriGenerator;
import fr.hyriode.hyrame.npc.NPC;
import fr.hyriode.hyrame.npc.NPCManager;
import fr.hyriode.hyrame.npc.NPCSkin;
import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import fr.hyriode.hyriapi.rank.EHyriRank;
import fr.hyriode.hyriapi.settings.HyriLanguage;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.api.player.HyriBWPlayer;
import fr.hyriode.bedwars.api.player.cosmetic.HyriBWNPCCosmetic;
import fr.hyriode.bedwars.game.event.BWNextEvent;
import fr.hyriode.bedwars.game.generator.BWGenerator;
import fr.hyriode.bedwars.game.npc.EHyriBWNPCType;
import fr.hyriode.bedwars.game.npc.inventory.pages.BWShopQuickBuy;
import fr.hyriode.bedwars.game.scoreboard.BWGameScoreboard;
import fr.hyriode.bedwars.game.tasks.BWGameTask;
import fr.hyriode.bedwars.game.team.EBWGameTeam;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class BWGame extends HyriGame<BWGamePlayer> {

    private final HyriBedWars plugin;
    private final BWGameType gameType;
    private BWNextEvent actualEvent;

    private BWGameTask task;

    public BWGame(IHyrame hyrame, HyriBedWars plugin) {
        super(hyrame, plugin, "bedwars", HyriBedWars.NAME, BWGamePlayer.class);

        this.plugin = plugin;
        this.gameType = BWGameType.TRIO;

        this.minPlayers = 4;
        this.maxPlayers = 8;

        this.registerTeams();
    }

    private void registerTeams() {
        if (gameType.equals(BWGameType.SOLO) || gameType.equals(BWGameType.DOUBLES)) {

            for (EBWGameTeam gameTeam : EBWGameTeam.values()) {
                this.registerTeam(new BWGameTeam(gameTeam, this.gameType.getTeamSize(), this.plugin));
            }

        } else {

            this.registerTeam(new BWGameTeam(EBWGameTeam.RED, this.gameType.getTeamSize(), this.plugin));
            this.registerTeam(new BWGameTeam(EBWGameTeam.BLUE, this.gameType.getTeamSize(), this.plugin));
            this.registerTeam(new BWGameTeam(EBWGameTeam.YELLOW, this.gameType.getTeamSize(), this.plugin));
            this.registerTeam(new BWGameTeam(EBWGameTeam.GREEN, this.gameType.getTeamSize(), this.plugin));

        }
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

        HyriGameItems.TEAM_CHOOSER.give(this.hyrame, p, 0);
        HyriGameItems.LEAVE_ITEM.give(this.hyrame, p, 8);

        this.getPlayer(p.getUniqueId()).setPlugin(this.plugin);
    }

    @Override
    public void handleLogout(Player p) {
        super.handleLogout(p);
    }

    @Override
    public void start() {
        super.start();
        this.setActualEvent(BWNextEvent.START);

        this.spawnNPCs();

        this.spawnGenerators();

        this.task = new BWGameTask(this.plugin, this.gameTime);
        this.task.runTaskTimerAsynchronously(this.plugin, 0, 20);

        this.teleportTeams();

        this.teams.forEach(team -> {
            if(team.getPlayers().size() == 0) {
                ((BWGameTeam) team).setHasBed(false);
                ((BWGameTeam) team).setEliminated(true);
            }
        });

        this.setScoreboardForPlayers();
    }

    private void setScoreboardForPlayers(){
        this.players.forEach(player -> {
            BWGameScoreboard scoreboard = new BWGameScoreboard(this.plugin, player.getPlayer());
            player.setScoreboard(scoreboard);
            scoreboard.show();
        });
    }

    private void teleportTeams() {
        this.teams.forEach(gameTeam -> {
            gameTeam.getPlayers().forEach(player -> {
                this.getPlayer(player.getUUID()).giveArmor();
                player.getPlayer().teleport(this.plugin.getConfiguration().getTeam(gameTeam.getName()).getRespawnLocation());
            });
        });

    }

    private void spawnGenerators(){
        for(HyriBWConfiguration.Team team : this.plugin.getConfiguration().getTeams()){
            Location loc = team.getGeneratorLocation();

            Arrays.asList(BWGenerator.BASE_TIER_I.getGenerator().getItemsToGen()).forEach(generatorTier -> {
                HyriGenerator generator = new HyriGenerator.Builder(this.plugin, loc, generatorTier).withItem(generatorTier.getItemStack()).build();
                generator.create();
            });
        }
    }

    private void spawnNPCs(){
        for(HyriGameTeam team : this.plugin.getGame().getTeams()) {
            final Location locShop = this.plugin.getConfiguration().getTeam("blue").getShopNPCLocation();
            final Location locUpgrade = this.plugin.getConfiguration().getTeam("blue").getUpgradeNPCLocation();

            final NPCSkin npcShopSkin = this.getCosmeticByUpPlayersRank(team.getPlayers()) != null ?
                    this.getCosmeticByUpPlayersRank(team.getPlayers()).getSkin() : EHyriBWNPCType.SHOP.getDefaultSkin();
            final NPCSkin npcUpgradeSkin = this.getCosmeticByUpPlayersRank(team.getPlayers()) != null ?
                    this.getCosmeticByUpPlayersRank(team.getPlayers()).getSkin() : EHyriBWNPCType.UPGRADE.getDefaultSkin();

            for (HyriGamePlayer player : this.players) {
                final HyriLanguage language = HyriAPI.get().getPlayerSettingsManager()
                        .getPlayerSettings(player.getUUID()).getLanguage();
                final NPC shop = NPCManager.createNPC(locShop, npcShopSkin,
                        Collections.singletonList(ChatColor.BOLD + EHyriBWNPCType.SHOP.getLanguageName().getValue(language)));
                final NPC upgrade = NPCManager.createNPC(locUpgrade, npcUpgradeSkin,
                        Collections.singletonList(ChatColor.BOLD + EHyriBWNPCType.UPGRADE.getLanguageName().getValue(language)));
                shop.addPlayer(player.getPlayer());
                upgrade.addPlayer(player.getPlayer());
                shop.setInteractCallback((rightClick, clicker) -> {
                    if (rightClick)
                        new BWShopQuickBuy(this.plugin, clicker).open();
                });
                upgrade.setInteractCallback((rightClick, clicker) -> {
                    if (rightClick)
                        new BWUpgradeGui(this.plugin, clicker).open();
                });
            }
        }
    }

    private HyriBWNPCCosmetic getCosmeticByUpPlayersRank(List<HyriGamePlayer> players){
        HyriBWPlayer finalPlayer = null;

        for (final HyriGamePlayer player : players) {
            final IHyriPlayer iHyriPlayer = HyriAPI.get().getPlayerManager().getPlayer(player.getUUID());
            final EHyriRank rank = iHyriPlayer.getRank().getType();
            final int rankId = rank.getId();
            HyriBWPlayer playerAPI = this.plugin.getAPI().getPlayerManager().getPlayer(player.getUUID());

            if(finalPlayer == null){
                finalPlayer = playerAPI;
            }else if(finalPlayer.getPlayer().getRank().getType().getId() > rankId && playerAPI.getCurrentCosmetic() != null){
                finalPlayer = playerAPI;
            }
        }
        if(finalPlayer != null && finalPlayer.getCurrentCosmetic() != null && finalPlayer.getCurrentCosmetic() instanceof HyriBWNPCCosmetic)
            return (HyriBWNPCCosmetic) finalPlayer.getCurrentCosmetic();
        else
            return null;

    }

    @Override
    public void win(HyriGameTeam winner) {
        super.win(winner);
    }

    public HyriGameTeam getWinner() {
        final List<HyriGameTeam> teamsAlive = new ArrayList<>(this.teams);
        HyriGameTeam winner = null;

        for (HyriGameTeam team : this.teams) {
            BWGameTeam gameTeam = (BWGameTeam) team;

            if (gameTeam.isEliminated()) {
                teamsAlive.remove(team);
            }

            if (teams.size() == 1) {
                winner = teamsAlive.get(0);
            }
        }
        return winner;
    }

    public BWNextEvent getActualEvent() {
        return actualEvent;
    }

    public void setActualEvent(BWNextEvent actualEvent) {
        this.actualEvent = actualEvent;
    }

    public BWGameTask getTask() {
        return task;
    }
}
