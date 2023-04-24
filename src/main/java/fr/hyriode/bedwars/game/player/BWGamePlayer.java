package fr.hyriode.bedwars.game.player;

import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.api.player.BWPlayerStatistics;
import fr.hyriode.bedwars.api.player.BWPlayerData;
import fr.hyriode.bedwars.game.player.cosmetic.npc.NPCSkin;
import fr.hyriode.bedwars.game.player.hotbar.HotbarCategory;
import fr.hyriode.bedwars.game.player.scoreboard.BWPlayerScoreboard;
import fr.hyriode.bedwars.game.player.traker.TeamTraker;
import fr.hyriode.bedwars.game.shop.*;
import fr.hyriode.bedwars.game.shop.material.MaterialArmorShop;
import fr.hyriode.bedwars.game.shop.material.MaterialShop;
import fr.hyriode.bedwars.game.shop.material.upgrade.UpgradeMaterial;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.game.team.upgrade.UpgradeTeam;
import fr.hyriode.bedwars.game.type.BWGameType;
import fr.hyriode.bedwars.game.upgrade.Upgrade;
import fr.hyriode.bedwars.game.upgrade.UpgradeManager;
import fr.hyriode.bedwars.manager.pnj.PNJ;
import fr.hyriode.bedwars.utils.InventoryUtils;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.hyrame.game.HyriGamePlayer;
import fr.hyriode.hyrame.game.HyriGameType;
import fr.hyriode.hyrame.game.protocol.HyriLastHitterProtocol;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.PlayerUtil;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BWGamePlayer extends HyriGamePlayer {

    public static final String RESPAWN_COUNTDOWN = "respawn";
    public static final String FIREBALL_COUNTDOWN = "fireball";
    public static final String TRAP_COUNTDOWN = "trap";

    private HyriBedWars plugin;
    private BWPlayerScoreboard scoreboard;
    private BWPlayerData account;
    private NPCSkin npcSkin;

    private TeamTraker teamTracker;
    private MaterialArmorShop armor;
    private final List<MaterialShop> itemsPermanent;
    private final List<UpgradeMaterial> materialsUpgrade;
    private final List<CountdownPlayer> countdowns;

    private int kills;
    private int finalKills;
    private int deaths;
    private int bedsBroken;

    public BWGamePlayer(Player player) {
        super(player);
        this.materialsUpgrade = new ArrayList<>();
        this.itemsPermanent = new ArrayList<>();
        this.countdowns = new ArrayList<>();
        this.npcSkin = new NPCSkin(new NPCSkin.Skin(PNJ.Type.BLAZE), new NPCSkin.Skin(PNJ.Type.VILLAGER));

        this.account = this.asHyriPlayer().getData().read("bedwars", new BWPlayerData());

        if(this.account == null) {
            this.account = new BWPlayerData();
        }
    }

    public void handleLogin(HyriBedWars plugin){
        this.plugin = plugin;
        this.teamTracker = new TeamTraker(plugin, this);
    }

    public void updateStatistics(boolean isWinner) {
        BWPlayerStatistics playerStatistics = this.getStatistics();
        BWPlayerStatistics.Data statistics = playerStatistics.getData(this.plugin.getGame().getType());
        statistics.addKills(this.kills);
        statistics.addFinalKills(this.finalKills);
        statistics.addDeaths(this.deaths);
        statistics.addBedsBroken(this.bedsBroken);
        statistics.addPlayedGames(1);
        if(isWinner){
            statistics.addWins(1);
            statistics.addCurrentWinStreak(1);
            if(statistics.getCurrentWinStreak() > statistics.getBestWinStreak()){
                statistics.setBestWinStreak(statistics.getCurrentWinStreak());
            }
        }else {
            statistics.setCurrentWinStreak(0);
        }

        playerStatistics.update(this.asHyriPlayer());
    }

    public boolean kill() {
        HyriLastHitterProtocol hitterProtocol = this.plugin.getGame().getProtocolManager().getProtocol(HyriLastHitterProtocol.class);
        List<HyriLastHitterProtocol.LastHitter> lastHitters = hitterProtocol.getLastHitters(this.player);
        boolean finalKill = !this.getBWTeam().hasBed();

        this.addDeaths(1);

        if (lastHitters != null && !lastHitters.isEmpty()) {
            BWGamePlayer hitter = (BWGamePlayer) lastHitters.get(0).asGamePlayer();
            if(!hitter.isSpectator() && !hitter.isDead()) {
                List<ItemPrice> itemStacks = InventoryUtils.getMoney(this.player.getInventory());

                for (ItemPrice money : itemStacks) {
                    hitter.getPlayer().sendMessage(money.getColor() + "+" + money.getAmount().get().get() + " " + money.getName(hitter));
                    hitter.getPlayer().getInventory().addItem(money.getItemStacks().toArray(new ItemStack[0]));
                }

                hitter.addKills(1);
                if(finalKill) {
                    hitter.addFinalKills(1);
                }
            }
        }

        if(finalKill) {
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.plugin.getGame().checkWin(), 1L);
        }
        return !finalKill;
    }

    public BWPlayerData getAccount() {
        return this.account;
    }

    public BWGameTeam getBWTeam(){
        return (BWGameTeam) this.getTeam();
    }

    public void respawn(boolean respawn) {
        this.player.teleport(this.getBWTeam().getConfig().getRespawnLocation());
        this.player.setGameMode(GameMode.SURVIVAL);
        this.giveArmor();
        this.giveSword();
        this.giveTracker();
        this.itemsPermanent.forEach(
                material -> InventoryUtils.giveInSlot(this.player, 0 /*Faire selon la hotbar*/, material.getFirstItem().getItemStack(this)));
        this.materialsUpgrade.forEach(material -> {
            material.removeTier();
            InventoryUtils.giveInSlot(this.player, 0 /*Faire selon la hotbar*/, material.getItemShopByTier().getItemStack(this));
        });
        this.applyUpgrades();
        if(respawn) {
            this.addCountdown(RESPAWN_COUNTDOWN, 20*2);
        }
    }

    private void applyUpgrades(){
        BWGameTeam team = this.getBWTeam();

        team.getUpgradeTeam().getUpgrades().forEach(upgradeLite -> {
            Upgrade upgrade = HyriBedWars.getUpgradeManager().getUpgradeByName(upgradeLite.getName());

            if(upgrade.isForPlayer()) {
                Bukkit.getScheduler().runTaskLater(this.plugin, () -> upgrade.upgrade(this, upgrade.getTier(team.getUpgradeTeam().getTier(upgradeLite.getName())), false), 2L);
            }
        });
    }

    public void giveSword(){
        ItemStack sword = this.getSword();

        if(this.player.getItemOnCursor().getType() != sword.getType()) {
            int hotbar = this.getAccount().getSlotByHotbar(this.player, sword, HotbarCategory.MELEE);
            InventoryUtils.giveInSlot(this.player, hotbar, sword);
        }

        this.applySharpness();
    }

    public ItemStack getSword(){
        return new ItemBuilder(Material.WOOD_SWORD).unbreakable()
                .withItemFlags(ItemFlag.HIDE_UNBREAKABLE)
                .nbt().setBoolean(MetadataReferences.ISPERMANENT, true).build().clone();
    }

    public void applySharpness(){
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            BWGameTeam team = this.getBWTeam();
            if(team == null || team.getUpgradeTeam() == null) return;
            UpgradeTeam upgradeTeam = team.getUpgradeTeam();

            if(upgradeTeam.hasUpgrade(UpgradeManager.SHARPNESS)){
                Upgrade upgrade = HyriBedWars.getUpgradeManager().getUpgradeByName(UpgradeManager.SHARPNESS);
                upgrade.upgrade(this, upgrade.getTier(upgradeTeam.getTier(UpgradeManager.SHARPNESS)), false);
            }
            this.getPlayer().updateInventory();
        }, 3L);
    }

    public void applyProtection(){
        BWGameTeam team = this.getBWTeam();
        if(team == null || team.getUpgradeTeam() == null) return;
        UpgradeTeam upgradeTeam = team.getUpgradeTeam();

        if(upgradeTeam.hasUpgrade(UpgradeManager.PROTECTION)){
            Upgrade upgrade = HyriBedWars.getUpgradeManager().getUpgradeByName(UpgradeManager.PROTECTION);
            upgrade.upgrade(this, upgrade.getTier(upgradeTeam.getTier(UpgradeManager.PROTECTION)), false);
        }
    }

    public void giveTracker() {
        ItemStack tracker = this.getItemTracker();

        if(this.player.getItemOnCursor().getType() != tracker.getType()) {
            int slot = this.getAccount().getSlotByHotbar(this.player, tracker, HotbarCategory.COMPASS);
            if(slot == -1) slot = 17;
            InventoryUtils.giveInSlot(this.player, slot, tracker);
        }
    }

    public ItemStack getItemTracker(){
        return new ItemBuilder(Material.COMPASS)
                .withName(ChatColor.GREEN + HyriLanguageMessage.get("tracker.inv.item.name").getValue(this))
                .nbt()
                .setBoolean(MetadataReferences.COMPASS, true)
                .setBoolean(MetadataReferences.ISPERMANENT, true)
                .build().clone();
    }

    public void update(){
        this.account.update(this.getUniqueId());
    }

    public List<UpgradeMaterial> getMaterialsUpgrade() {
        return materialsUpgrade;
    }

    public boolean containsMaterialUpgrade(MaterialShop materialShop){
        return this.materialsUpgrade.stream().filter(upgradeMaterial -> upgradeMaterial.getMaterial() == materialShop).findFirst().orElse(null) != null;
    }

    public UpgradeMaterial getMaterialUpgrade(MaterialShop materialShop) {
        return this.materialsUpgrade.stream()
                .filter(upgradeMaterial -> upgradeMaterial.getMaterial().getName().equals(materialShop.getName()))
                .findFirst().orElse(null);
    }

    public UpgradeMaterial addUpgradeMaterial(MaterialShop material) {
        UpgradeMaterial upgradeMaterial = new UpgradeMaterial(material);
        this.materialsUpgrade.add(upgradeMaterial);
        return upgradeMaterial;
    }

    public MaterialArmorShop getArmor() {
        return armor;
    }

    public void setArmor(MaterialArmorShop armor) {
        this.armor = armor;
    }

    public void giveArmor(){
        Color color = this.getTeam().getColor().getDyeColor().getColor();
        this.player.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET)
                .withLeatherArmorColor(color).withEnchant(Enchantment.WATER_WORKER)
                .unbreakable().withItemFlags(ItemFlag.HIDE_UNBREAKABLE).build());
        this.player.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .withLeatherArmorColor(color).unbreakable().withItemFlags(ItemFlag.HIDE_UNBREAKABLE).build());
        if(this.armor != null){
            this.player.getInventory().setLeggings(new ItemBuilder(this.armor.getLeggings())
                    .unbreakable().withItemFlags(ItemFlag.HIDE_UNBREAKABLE).build());
            this.player.getInventory().setBoots(new ItemBuilder(this.armor.getBoots())
                    .unbreakable().withItemFlags(ItemFlag.HIDE_UNBREAKABLE).build());
        }else {
            this.player.getInventory().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS)
                    .withLeatherArmorColor(color).unbreakable().withItemFlags(ItemFlag.HIDE_UNBREAKABLE).build());
            this.player.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS)
                    .withLeatherArmorColor(color).unbreakable().withItemFlags(ItemFlag.HIDE_UNBREAKABLE).build());
        }
    }

    public List<MaterialShop> getItemsPermanent() {
        return itemsPermanent;
    }

    public void addItemPermanent(MaterialShop materialShop){
        if(materialShop.isPermanent()) {
            this.itemsPermanent.add(materialShop);
        }
    }

    public boolean containsItemPermanent(MaterialShop material) {
        if (material != null){
            return this.itemsPermanent.contains(material);
        }
        return false;
    }

    public void addCountdown(String name, int time){
        this.addCountdown(name, time, null);
    }

    public void addCountdown(String name, int timeFinal, String keyName) {
        if(this.hasCountdown(name)) {
            this.getCountdown(name).setTime(timeFinal);
            return;
        }
        this.countdowns.add(new CountdownPlayer(this, name).start(this.plugin, timeFinal, keyName));
    }

    public CountdownPlayer getCountdown(String name) {
        return this.countdowns.stream().filter(cp -> cp.getName().equals(name)).findFirst().orElse(null);
    }

    public boolean hasCountdown(String name){
        return this.getCountdown(name) != null;
    }

    public void removeCountdown(String name) {
        this.countdowns.remove(this.getCountdown(name));
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getFinalKills() {
        return finalKills;
    }

    public int getBedsBroken() {
        return bedsBroken;
    }

    public void setScoreboard(BWPlayerScoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public BWPlayerScoreboard getScoreboard() {
        return scoreboard;
    }

    public HyriBedWars getPlugin() {
        return plugin;
    }

    public List<CountdownPlayer> getCountdowns() {
        return countdowns;
    }

    public void hideArmor() {
        for (BWGamePlayer player : this.plugin.getGame().getPlayers()) {
            if(!player.getBWTeam().equals(this.getBWTeam())){
                PlayerUtil.hideArmor(this.player, player.getPlayer());
            }
        }
    }

    public void showArmor() {
        for (BWGamePlayer player : this.plugin.getGame().getPlayers()) {
            PlayerUtil.showArmor(this.player, player.getPlayer());
        }
    }

    public boolean isFullInventory() {
        return InventoryUtils.isFull(this.player);
    }

    public TeamTraker getTracker() {
        return this.teamTracker;
    }

    public BWPlayerStatistics getStatistics() {
        IHyriPlayer player = this.asHyriPlayer();
        BWPlayerStatistics playerStatistics = player.getStatistics().read("bedwars", new BWPlayerStatistics());

        if(playerStatistics == null) {
            playerStatistics = new BWPlayerStatistics();
            playerStatistics.update(player);
        }

        return playerStatistics;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setFinalKills(int finalKills) {
        this.finalKills = finalKills;
    }

    public void setBedsBroken(int bedsBroken) {
        this.bedsBroken = bedsBroken;
    }

    public void addKills(int kills) {
        this.kills += kills;
    }

    public void addDeaths(int deaths) {
        this.deaths += deaths;
    }

    public void addFinalKills(int finalKills) {
        this.finalKills += finalKills;
    }

    public void addBedsBroken(int bedsBroken) {
        this.bedsBroken += bedsBroken;
    }

    public NPCSkin getNPCSkin() {
        return this.npcSkin;
    }
}
