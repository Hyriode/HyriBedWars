package fr.hyriode.bedwars.game;

import fr.hyriode.bedwars.api.player.HyriBWPlayer;
import fr.hyriode.bedwars.api.shop.HyriHotbarCategory;
import fr.hyriode.bedwars.game.material.OreStack;
import fr.hyriode.bedwars.game.material.tracker.BWTrackerItem;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.game.team.upgrade.EBWUpgrades;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.hyrame.game.HyriGame;
import fr.hyriode.hyrame.game.HyriGamePlayer;
import fr.hyriode.hyrame.game.protocol.HyriLastHitterProtocol;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.material.BWMaterial;
import fr.hyriode.bedwars.game.material.ItemShopUpgradable;
import fr.hyriode.bedwars.game.material.upgradable.ArmorBW;
import fr.hyriode.bedwars.game.scoreboard.BWGameScoreboard;
import fr.hyriode.bedwars.utils.InventoryBWUtils;
import fr.hyriode.hyrame.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BWGamePlayer extends HyriGamePlayer {

    private HyriBedWars plugin;

    private BWGameScoreboard scoreboard;

    private BWMaterial permanentArmor;
    private final List<ItemShopUpgradable> upgradableItems = new ArrayList<>();
    private final List<BWMaterial> permanentItems = new ArrayList<>();

    private HyriBWPlayer account;

    private boolean cooldownTrap;
    private boolean cooldownFireball;
    private boolean cooldownInvisibilityParticle;
    private boolean cooldownRespawn;

    private int kills;
    private int deaths;
    private int finalKills;
    private int bedsBroken;

    public BWGamePlayer(HyriGame<?> game, Player player) {
        super(game, player);
    }

    public void handleLogin(HyriBedWars plugin){
        this.plugin = plugin;
    }

    public void respawn(){
        this.player.getInventory().addItem(new ItemBuilder(Material.WOOD_SWORD).unbreakable().nbt().setBoolean(MetadataReferences.ISPERMANENT, true).build());
        this.giveTracker();
        this.giveArmor();
        this.giveItemsPermanent();
        this.activeUpgradesTeam();
    }

    private void giveTracker() {
        int slot = this.getAccount().getSlotByHotbar(HyriHotbarCategory.COMPASS);
        if(slot != -1){
            this.plugin.getHyrame().getItemManager().giveItem(this.player, slot, BWTrackerItem.class);
            return;
        }
        this.plugin.getHyrame().getItemManager().giveItem(this.player, 17, BWTrackerItem.class);
    }

    public void activeUpgradesTeam() {
        this.getHyriTeam().getUpgrades().getUpgrades().forEach((upgrade, bwUpgradeTier) ->
                this.activeUpgradesTeam(upgrade, bwUpgradeTier.getTier()));
    }

    public void activeUpgradesTeam(EBWUpgrades upgrade, int tier){
        upgrade.active(this.plugin, this, tier);
    }

    public void activeUpgradesTeam(EBWUpgrades upgrade){
        if(this.getHyriTeam().getUpgrades().getCurrentUpgradeTier(upgrade) != null)
            this.activeUpgradesTeam(upgrade, this.getHyriTeam().getUpgrades().getCurrentUpgradeTier(upgrade).getTier());
    }

    public void giveItemsPermanent(){
        Arrays.asList(BWMaterial.values()).forEach(material -> {
            if(!material.isItemUpgradable() && material.getItemShop().isPermanent() && this.hasPermanentItem(material)){
                this.player.getInventory().addItem(material.getItemShop().getItemStack());
            }else if(material.isItemUpgradable() && upgradableItems.contains(material.getItemUpgradable())){
                this.player.getInventory().addItem(this.getItemUpgradable(material).getTierItem().getItemStack());
            }
        });
    }

    public void giveArmor(){
        this.player.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).unbreakable()
                .withLeatherArmorColor(this.getTeam().getColor().getDyeColor().getColor())
                .withEnchant(Enchantment.WATER_WORKER).build());
        this.player.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).unbreakable()
                .withLeatherArmorColor(this.getTeam().getColor().getDyeColor().getColor()).build());
        if(permanentArmor == null){
            this.player.getInventory().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).unbreakable()
                    .withLeatherArmorColor(this.getTeam().getColor().getDyeColor().getColor())
                    .build());
            this.player.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS).unbreakable()
                    .withLeatherArmorColor(this.getTeam().getColor().getDyeColor().getColor()).build());
        }else{
            ArmorBW armorBW = permanentArmor.getArmor();
            this.player.getInventory().setLeggings(new ItemBuilder(armorBW.getLeggings()).unbreakable().build());
            this.player.getInventory().setBoots(new ItemBuilder(armorBW.getBoots()).unbreakable().build());
        }
        this.player.updateInventory();
    }

    public void giveArmor(BWMaterial material){
        this.setPermanentArmor(material.getArmor());
        if(!this.player.hasPotionEffect(PotionEffectType.INVISIBILITY))
            this.giveArmor();
    }

    public void setScoreboard(BWGameScoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public void setPermanentArmor(ArmorBW armor){
        if(this.permanentArmor == null || this.permanentArmor.getArmor().getLevel() < armor.getLevel())
            this.permanentArmor = armor.getHyriMaterial();
    }

    public BWMaterial getPermanentArmor() {
        return permanentArmor;
    }

    public boolean hasPermanentItem(BWMaterial material) {
        return this.permanentItems.stream().anyMatch(oreStack -> oreStack.getItemShop().getHyriMaterial() == material);
    }

    public void addPermanentItem(BWMaterial material) {
        this.permanentItems.add(material);
    }

    public List<BWMaterial> getPermanentItems() {
        return permanentItems;
    }

    public void giveUpgradeItem(BWMaterial material){
        ItemStack itemPrevious = this.getItemUpgradable(material).getPreviousTierItem().getItemStack();
        ItemStack itemUpgrade = this.getItemUpgradable(material).getTierItem().getItemStack();
        if(InventoryBWUtils.hasItem(this.player, itemPrevious)){
            InventoryBWUtils.replaceItem(this.player, itemPrevious, itemUpgrade);
        }else
            this.player.getInventory().addItem(itemUpgrade);
    }

    public void nextUpgradeItem(BWMaterial newMaterial) {
        if(newMaterial.isItemUpgradable()) {
            if (!this.upgradableItems.isEmpty()) {
                for (ItemShopUpgradable item : this.upgradableItems) {
                    if (item.getHyriMaterial() == newMaterial) {
                        item.updateNextTier();
                        this.giveUpgradeItem(newMaterial);
                        return;
                    }
                }
            }
            this.upgradableItems.add(newMaterial.getItemUpgradable());
            this.giveUpgradeItem(newMaterial);
        }
    }

    public boolean hasUpgradeMaterial(BWMaterial material) {
        if(material.isItemUpgradable()){
            for (ItemShopUpgradable item : this.upgradableItems) {
                if(item.getHyriMaterial() == material){
                    return true;
                }
            }
        }
        return false;
    }

    public List<ItemShopUpgradable> getUpgradableItems() {
        return this.upgradableItems;
    }

    public ItemShopUpgradable getItemUpgradable(BWMaterial material){
        return this.upgradableItems.stream().filter(item -> item.getHyriMaterial() == material).findFirst().orElse(null);
    }

    public void clearArmor() {
        this.player.getInventory().setArmorContents(null);
    }

    public BWGameTeam getHyriTeam(){
        return (BWGameTeam) this.team;
    }

    public BWGameScoreboard getScoreboard() {
        return scoreboard;
    }

    public void downItemsUpgradable() {
        for(ItemShopUpgradable item : this.upgradableItems){
            item.downUpgrade();
        }
    }

    public HyriBWPlayer getAccount() {
        return account;
    }

    public void setAccount(HyriBWPlayer account) {
        this.account = account;
    }

    public boolean kill(){
        this.downItemsUpgradable();

        final Player hitter = this.getLastHitter();

        if(hitter != null && !this.isSpectator()) {
            final List<OreStack> itemStacks = InventoryBWUtils.getOresInventory(this.player, BWGameOre.GOLD,
                    BWGameOre.IRON, BWGameOre.DIAMOND, BWGameOre.EMERALD);
            for (OreStack item : itemStacks) {
                hitter.getInventory().addItem(item.getItemStack());
                hitter.sendMessage(item.getItem().getColor() + "+" + item.getAmount() + " " + item.getItem().getName().getForPlayer(hitter));
            }
        }
        PlayerUtil.resetPlayer(player, true);

        this.ignoreGenerators(true);
        this.plugin.getGame().getPlayer(player).addDeath();

        if(this.getHyriTeam().hasBed()) {
            if (hitter != null) {
                this.plugin.getGame().getPlayer(hitter).addKill();
            }
            this.plugin.getGame().getPlayers().forEach(p -> p.getScoreboard().update());
            return true;
        }
        this.setSpectator(true);
        if(hitter != null){
            this.plugin.getGame().getPlayer(hitter).addFinalKills();
        }
        this.plugin.getGame().win();
        System.out.println("Final Kill");
        this.plugin.getGame().getPlayers().forEach(p -> p.getScoreboard().update());
        return false;
    }

    public void ignoreGenerators(boolean ignore){
        if(ignore){
            this.game.getTeams().forEach(team -> {
                ((BWGameTeam) team).getGoldGenerator().addIgnoredPlayer(this.player);
                ((BWGameTeam) team).getIronGenerator().addIgnoredPlayer(this.player);
            });
            this.plugin.getGame().getDiamondGenerators().forEach(generator -> {
                generator.addIgnoredPlayer(this.player);
            });
            this.plugin.getGame().getEmeraldGenerators().forEach(generator -> {
                generator.addIgnoredPlayer(this.player);
            });
            return;
        }
        this.game.getTeams().forEach(team -> {
            ((BWGameTeam) team).getGoldGenerator().removeIgnoredPlayer(this.player);
            ((BWGameTeam) team).getIronGenerator().removeIgnoredPlayer(this.player);
        });
        this.plugin.getGame().getDiamondGenerators().forEach(generator -> {
            generator.removeIgnoredPlayer(this.player);
        });
        this.plugin.getGame().getEmeraldGenerators().forEach(generator -> {
            generator.removeIgnoredPlayer(this.player);
        });
    }

    public Player getLastHitter(){
        final List<HyriLastHitterProtocol.LastHitter> lastHitters = this.game.getProtocolManager().getProtocol(HyriLastHitterProtocol.class).getLastHitters(this.player);

        if(lastHitters != null){
            return lastHitters.get(0).asPlayer();
        }

        return null;
    }

    public boolean isCooldownTrap() {
        return cooldownTrap;
    }

    public void setCooldownTrap(boolean cooldownTrap) {
        this.cooldownTrap = cooldownTrap;
    }

    public boolean isCooldownFireball() {
        return cooldownFireball;
    }

    public void setCooldownFireball(boolean cooldownFireball) {
        this.cooldownFireball = cooldownFireball;
    }

    public boolean isCooldownInvisibilityParticle() {
        return cooldownInvisibilityParticle;
    }

    public void setCooldownInvisibilityParticle(boolean cooldownInvisibilityParticle) {
        this.cooldownInvisibilityParticle = cooldownInvisibilityParticle;
        if(cooldownInvisibilityParticle)
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.setCooldownInvisibilityParticle(false), 10);
    }

    public boolean isCooldownRespawn() {
        return cooldownRespawn;
    }

    public void setCooldownRespawn(boolean cooldownRespawn) {
        this.cooldownRespawn = cooldownRespawn;
    }

    public int getKills() {
        return this.kills;
    }

    public int getFinalKills() {
        return this.finalKills;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public int getBedsBroken() {
        return this.bedsBroken;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setFinalKills(int finalKills) {
        this.finalKills = finalKills;
    }

    public void setBedsBroken(int bedsBroken) {
        this.bedsBroken = bedsBroken;
    }

    public void addDeath(int death){
        this.deaths += death;
    }

    public void addDeath(){
        this.addDeath(1);
    }

    public void addKills(int kills){
        this.kills += kills;
    }

    public void addKill(){
        this.addKills(1);
    }

    public void addFinalKills(int finalKills){
        this.finalKills += finalKills;
    }

    public void addFinalKills(){
        this.addFinalKills(1);
    }

    public void addBedsBroken(int bedsBroken){
        this.bedsBroken += bedsBroken;
    }

    public void addBedsBroken(){
        this.addBedsBroken(1);
    }

    public void giveSword() {
        InventoryBWUtils.addItem(player, 0, new ItemBuilder(Material.WOOD_SWORD).unbreakable().nbt().setBoolean(MetadataReferences.ISPERMANENT, true).build());
        this.activeUpgradesTeam(EBWUpgrades.SHARPNESS);
    }
}
