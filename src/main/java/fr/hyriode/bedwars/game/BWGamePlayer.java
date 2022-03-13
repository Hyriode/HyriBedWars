package fr.hyriode.bedwars.game;

import fr.hyriode.bedwars.api.player.HyriBWPlayer;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.game.team.upgrade.EBWUpgrades;
import fr.hyriode.hyrame.game.HyriGame;
import fr.hyriode.hyrame.game.HyriGamePlayer;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.BWMaterial;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.ItemShopUpgradable;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.upgradable.ArmorBW;
import fr.hyriode.bedwars.game.scoreboard.BWGameScoreboard;
import fr.hyriode.bedwars.utils.InventoryBWUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
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

    public BWGamePlayer(HyriGame<?> game, Player player) {
        super(game, player);
    }

    public void respawn(){
        this.player.getInventory().addItem(new ItemStack(Material.WOOD_SWORD));
        this.giveArmor();
        this.giveItemsPermanent();
        this.setUpgradesTeam();
    }

    public void setUpgradesTeam(){
        this.getHyriTeam().getUpgrades().getUpgrades().forEach((upgrade, bwUpgradeTier) ->
                this.setUpgradesTeam(upgrade, bwUpgradeTier.getTier()));
    }

    public void setUpgradesTeam(EBWUpgrades upgrade, int tier){
        switch (upgrade){
            case SHARPNESS:
                InventoryBWUtils.changeItemsSlot(this.player, itemStack -> itemStack.addEnchantment(Enchantment.DAMAGE_ALL, 1),
                        new ItemStack(Material.WOOD_SWORD), BWMaterial.STONE_SWORD.getItemShop().getItemStack(), BWMaterial.IRON_SWORD.getItemShop().getItemStack(), BWMaterial.DIAMOND_SWORD.getItemShop().getItemStack());
                break;
            case HASTE:
                if(this.player.hasPotionEffect(PotionEffectType.FAST_DIGGING))
                    this.player.removePotionEffect(PotionEffectType.FAST_DIGGING);
                this.player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 99999 * 20, tier));
                break;
            case PROTECTION_ARMOR:
                for (ItemStack armor : this.player.getInventory().getArmorContents()) {
                    armor.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, tier + 1);
                }
                break;
        }
    }

    public void setUpgradesTeam(EBWUpgrades upgrade){
        if(this.getHyriTeam().getUpgrades().getCurrentUpgradeTier(upgrade) != null)
            this.setUpgradesTeam(upgrade, this.getHyriTeam().getUpgrades().getCurrentUpgradeTier(upgrade).getTier());
    }

    public void giveItemsPermanent(){
        Arrays.asList(BWMaterial.values()).forEach(material -> {
            if(!material.isItemUpgradable() && material.getItemShop().isPermanent() && this.hasPermanentItem(material)){
                //TODO à faire selon les slots récents :c
                this.player.getInventory().addItem(material.getItemShop().getItemStack());
            }else if(material.isItemUpgradable() && upgradableItems.contains(material.getItemUpgradable())){
                this.player.getInventory().addItem(this.getItemUpgradable(material).getTierItem().getItemStack());
            }
        });
    }

    public void giveArmor(){
        this.player.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET)
                .withLeatherArmorColor(this.getTeam().getColor().getDyeColor().getColor())
                .withEnchant(Enchantment.WATER_WORKER).build());
        this.player.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .withLeatherArmorColor(this.getTeam().getColor().getDyeColor().getColor()).build());
        if(permanentArmor == null){
            this.player.getInventory().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS)
                    .withLeatherArmorColor(this.getTeam().getColor().getDyeColor().getColor()).build());
            this.player.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS)
                    .withLeatherArmorColor(this.getTeam().getColor().getDyeColor().getColor()).build());
        }else{
            ArmorBW armorBW = permanentArmor.getArmor();
            this.player.getInventory().setLeggings(new ItemStack(armorBW.getLeggings()));
            this.player.getInventory().setBoots(new ItemStack(armorBW.getBoots()));
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

    public void setPlugin(HyriBedWars plugin){
        this.plugin = plugin;
    }

    public void giveUpgradeItem(BWMaterial material){
        ItemStack itemPrevious = this.getItemUpgradable(material).getPreviousTierItem().getItemStack();
        ItemStack itemUpgrade = this.getItemUpgradable(material).getTierItem().getItemStack();
        if(InventoryBWUtils.hasItems(this.player, itemPrevious)){
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
}
