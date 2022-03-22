package fr.hyriode.bedwars.game;

import fr.hyriode.bedwars.game.generator.BWBaseGoldGenerator;
import fr.hyriode.bedwars.game.generator.BWBaseIronGenerator;
import fr.hyriode.bedwars.game.generator.BWDiamondGenerator;
import fr.hyriode.bedwars.game.generator.BWEmeraldGenerator;
import fr.hyriode.bedwars.game.material.BWMaterial;
import fr.hyriode.bedwars.game.npc.inventory.upgrade.BWUpgradeGui;
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
import fr.hyriode.bedwars.game.npc.BWNPCType;
import fr.hyriode.bedwars.game.npc.inventory.shop.pages.BWShopQuickBuy;
import fr.hyriode.bedwars.game.scoreboard.BWGameScoreboard;
import fr.hyriode.bedwars.game.tasks.BWGameTask;
import fr.hyriode.bedwars.game.team.EBWGameTeam;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

public class BWGame extends HyriGame<BWGamePlayer> {

    private final HyriBedWars plugin;
    private final BWGameType gameType;
    private BWNextEvent actualEvent;

    private final List<HyriGenerator> diamondGenerators = new ArrayList<>();
    private final List<HyriGenerator> emeraldGenerators = new ArrayList<>();

    private BWGameTask task;

    public BWGame(IHyrame hyrame, HyriBedWars plugin) {
        super(hyrame, plugin, "bedwars", HyriBedWars.NAME, BWGamePlayer.class);

        this.plugin = plugin;
        this.gameType = this.plugin.getConfiguration().getGameType();

        this.minPlayers = this.gameType.getMinPlayers();
        this.maxPlayers = this.gameType.getMaxPlayers();

        this.registerTeams();
    }

    private void registerTeams() {
        for(int i = 0; i < gameType.getMaxTeams() && i < EBWGameTeam.values().length ; ++i){
            this.registerTeam(new BWGameTeam(EBWGameTeam.values()[i], this.gameType.getTeamsSize(), this.plugin));
        }
    }

    @Override
    public void handleLogin(Player p) {
        super.handleLogin(p);
//        if(this.state == HyriGameState.PLAYING){
//            this.getPlayer(p.getUniqueId()).kill();
//            return;
//        }

        p.getInventory().setArmorContents(null);
        p.getInventory().clear();
        p.setGameMode(GameMode.ADVENTURE);
        p.setFoodLevel(20);
        p.setHealth(20);
        p.setLevel(0);
        p.setExp(0.0F);
        p.teleport(this.plugin.getConfiguration().getWaitingSpawn());
        p.getActivePotionEffects().forEach(potionEffect -> p.removePotionEffect(potionEffect.getType()));

        this.getPlayer(p.getUniqueId()).setPlugin(this.plugin);

        HyriBWPlayer account = this.plugin.getAPI().getPlayerManager().getPlayer(p.getUniqueId());

        if(account == null){
            account = new HyriBWPlayer(p.getUniqueId());
        }

        this.getPlayer(p.getUniqueId()).setAccount(account);

        HyriGameItems.TEAM_CHOOSER.give(this.hyrame, p, 0);
        HyriGameItems.LEAVE_ITEM.give(this.hyrame, p, 8);
    }

    @Override
    public void handleLogout(Player p) {
        super.handleLogout(p);
    }

    @Override
    public void start() {
        super.start();

        this.setActualEvent(BWNextEvent.START);
        this.updateBed();
        this.updateAPI();

        this.spawnNPCs();

        this.spawnGenerators();

        this.task = new BWGameTask(this.plugin, this.gameTime);
        this.task.runTaskTimerAsynchronously(this.plugin, 0, 20);

        this.teleportTeams();

        this.setScoreboardForPlayers();
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
                this.setBaseQuickBuy(account);
                this.plugin.getAPI().getPlayerManager().sendPlayer(account);
            }
        }
    }

    private void setBaseQuickBuy(HyriBWPlayer account){
        account.putMaterialQuickBuy(19, BWMaterial.WOOL.name());
        account.putMaterialQuickBuy(20, BWMaterial.FIREBALL.name());
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

                final NPC shop = NPCManager.createNPC(locShop, npcShopSkin,
                        Collections.singletonList(ChatColor.BOLD + BWNPCType.SHOP.getLanguageName().getValue(language)));
                final NPC upgrade = NPCManager.createNPC(locUpgrade, npcUpgradeSkin,
                        Collections.singletonList(ChatColor.BOLD + BWNPCType.UPGRADE.getLanguageName().getValue(language)));

                shop.setShowingToAll(false);
                shop.addPlayer(player.getPlayer());
                shop.setInteractCallback((rightClick, clicker) -> {
                    if (rightClick) new BWShopQuickBuy(this.plugin, clicker).open();
                });

                upgrade.setShowingToAll(false);
                upgrade.addPlayer(player.getPlayer());
                upgrade.setInteractCallback((rightClick, clicker) -> {
                    if (rightClick) new BWUpgradeGui(this.plugin, clicker).open();
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
                    .withDefaultHeader(Material.DIAMOND_BLOCK, (player) -> HyriBedWars.getLanguageManager().getValue(player, "generator.diamond"))
                    .build();
            diamondGenerator.create();
            this.diamondGenerators.add(diamondGenerator);
        }

        for(Location loc : this.plugin.getConfiguration().getEmeraldLocations()){
            final HyriGenerator emeraldGenerator = new HyriGenerator.Builder(this.plugin, loc, BWEmeraldGenerator.EMERALD_TIER_I)
                    .withItem(BWGameOre.EMERALD.getItemStack())
                    .withDefaultHeader(Material.EMERALD_BLOCK, (player) -> HyriBedWars.getLanguageManager().getValue(player, "generator.emerald"))
                    .build();
            emeraldGenerator.create();
            this.emeraldGenerators.add(emeraldGenerator);
        }
    }

    private void teleportTeams() {
        this.teams.forEach(gameTeam -> {
            gameTeam.getPlayers().forEach(player -> {
                player.getPlayer().teleport(this.plugin.getConfiguration().getTeam(gameTeam.getName()).getRespawnLocation());
                player.getPlayer().setGameMode(GameMode.SURVIVAL);
                this.getPlayer(player.getUUID()).respawn();

            });
        });
    }

    private void setScoreboardForPlayers(){
        this.players.forEach(player -> {
            BWGameScoreboard scoreboard = new BWGameScoreboard(this.plugin, player.getPlayer());
            player.setScoreboard(scoreboard);
            scoreboard.show();
        });
    }

    /*
    * TODO Soon, in dev
    * */
    private HyriBWNPCCosmetic getCosmeticByUpPlayersRank(List<HyriGamePlayer> players){
        HyriBWPlayer finalPlayer = null;

        for (final HyriGamePlayer player : players) {
            final IHyriPlayer iHyriPlayer = HyriAPI.get().getPlayerManager().getPlayer(player.getUUID());
            final EHyriRank rank = iHyriPlayer.getRank().getType();
            final int rankId = rank.getId();
            HyriBWPlayer playerAPI = this.plugin.getAPI().getPlayerManager().getPlayer(player.getUUID());

//            if(finalPlayer == null){
//                finalPlayer = playerAPI;
//            }else if(finalPlayer.getPlayer().getRank().getType().getId() > rankId && playerAPI.getCurrentCosmetic() != null){
//                finalPlayer = playerAPI;
//            }
        }
//        if(finalPlayer != null && finalPlayer.getCurrentCosmetic() != null && finalPlayer.getCurrentCosmetic() instanceof HyriBWNPCCosmetic)
//            return (HyriBWNPCCosmetic) finalPlayer.getCurrentCosmetic();
//        else
            return null;

    }

    public void win(){
        this.teams.stream().filter(team -> !((BWGameTeam) team).isEliminated()).findFirst().ifPresent(this::win);
    }

    @Override
    public void win(HyriGameTeam winner) {
        super.win(winner);
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
}
