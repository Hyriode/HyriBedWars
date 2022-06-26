package fr.hyriode.bedwars.game.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.api.player.HyriBWPlayer;
import fr.hyriode.bedwars.game.BWGame;
import fr.hyriode.bedwars.game.player.scoreboard.BWPlayerScoreboard;
import fr.hyriode.bedwars.game.shop.*;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.utils.InventoryUtils;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.hyrame.game.HyriGame;
import fr.hyriode.hyrame.game.HyriGamePlayer;
import fr.hyriode.hyrame.game.protocol.HyriLastHitterProtocol;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BWGamePlayer extends HyriGamePlayer {

    public static final String RESPAWN_COUNTDOWN = "respawn";
    public static final String FIREBALL_COUNTDOWN = "fireball";

    private HyriBedWars plugin;
    private BWPlayerScoreboard scoreboard;

    private MaterialArmorShop armor;
    private final List<MaterialShop> itemsPermanent;
    private final List<UpgradeMaterial> materialsUpgrade;
    private final List<String> countdowns;

    private int kills;
    private int finalKills;
    private int deaths;
    private int bedsBroken;

    public BWGamePlayer(HyriGame<?> game, Player player) {
        super(game, player);
        this.materialsUpgrade = new ArrayList<>();
        this.itemsPermanent = new ArrayList<>();
        this.countdowns = new ArrayList<>();
    }

    public void handleLogin(HyriBedWars plugin){
        this.plugin = plugin;
//        this.tracker = new BWTracker(plugin, this);
    }

    public boolean kill() {
        BWGameTeam team = this.getBWTeam();
        if(team.hasBed()) {
            HyriLastHitterProtocol hitterProtocol = this.plugin.getGame().getProtocolManager().getProtocol(HyriLastHitterProtocol.class);
            List<HyriLastHitterProtocol.LastHitter> lastHitters = hitterProtocol.getLastHitters(this.player);

            if (lastHitters != null && !lastHitters.isEmpty()) {
                Player hitter = lastHitters.get(0).asPlayer();
                List<ItemPrice> itemStacks = InventoryUtils.getMoney(this.player.getInventory());

                for (ItemPrice money : itemStacks) {
                    hitter.sendMessage("+" + money.getAmount() + " " + money.getName(hitter));
                    hitter.getInventory().addItem(money.getItemStacks().toArray(new ItemStack[0]));
                }
            }
            return true;
        }
        return false;
    }

    public HyriBWPlayer getAccount() {
        HyriBWPlayer player = HyriAPI.get().getPlayerManager().getPlayer(this.getUUID()).getData("bedwars", HyriBWPlayer.class);

        if(player != null) {
            return player;
        }
        return new HyriBWPlayer();
    }

    private BWGame getGame(){
        return (BWGame) this.game;
    }

    public BWGameTeam getBWTeam(){
        return (BWGameTeam) this.team;
    }

    public void cooldownRespawn() {
        this.addCountdown("respawn", 20*2);
    }

    public void respawn() {
        this.player.teleport(this.getBWTeam().getConfig().getRespawnLocation());
        this.player.setGameMode(GameMode.SURVIVAL);
        this.giveArmor();
        this.giveSword();
        this.itemsPermanent.forEach(
                material -> InventoryUtils.giveInSlot(this.player, 0 /*Faire selon la hotbar*/, material.getFirstItem().getItemStack(this)));
        this.materialsUpgrade.forEach(material -> {
            material.removeTier();
            InventoryUtils.giveInSlot(this.player, 0 /*Faire selon la hotbar*/, material.getItemShopByTier().getItemStack(this));
        });
    }

    public void giveSword(){
        InventoryUtils.giveInSlot(this.player, 0 /*Faire selon la hotbar*/, this.getSword());
    }

    public ItemStack getSword(){
        ItemBuilder itemBuilder = new ItemBuilder(Material.WOOD_SWORD).unbreakable()
                .withItemFlags(ItemFlag.HIDE_UNBREAKABLE)
                .nbt().setBoolean(MetadataReferences.ISPERMANENT, true).toBuilder();

// TODO
//        if(this.getBWTeam().getUpgrades().contains(SHARPNESS)){
//            itemBuilder.withEnchant(Enchantment.DAMAGE_ALL);
//        }
        return itemBuilder.build().clone();
    }

    public void changeGamePlayStyle() {
        HyriBWPlayer account = this.getAccount();
        account.changeGamePlayStyle();
        account.update(this.getUUID());
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
        for (ItemStack armor : this.player.getInventory().getArmorContents()) {
            //TODO sharpness this.getBWTeam().getUpgrade()
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

    public void addCountdown(String name, int timeFinal) {
        this.countdowns.add(name);
        new BukkitRunnable() {
            int time = timeFinal;
            @Override
            public void run() {
                if(isSpectator() || isDead() || time < 0){
                    removeCountdown(name);
                    this.cancel();
                }
                time--;
            }
        }.runTaskTimerAsynchronously(this.plugin, 0L, 1L);
    }

    public boolean isCountdown(String name){
        return this.countdowns.contains(name);
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

    public List<String> getCountdowns() {
        return countdowns;
    }

    public void removeCountdown(String name) {
        this.countdowns.remove(name);
    }
}
