package fr.hyriode.bedwars.game;

import fr.hyriode.hyrame.game.HyriGame;
import fr.hyriode.hyrame.game.HyriGamePlayer;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.npc.inventory.EHyriBWShopNavBar;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWMaterial;
import fr.hyriode.bedwars.game.npc.inventory.shop.ItemShop;
import fr.hyriode.bedwars.game.npc.inventory.shop.ItemShopUpgradable;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.upgradable.ArmorBW;
import fr.hyriode.bedwars.game.scoreboard.BWGameScoreboard;
import fr.hyriode.bedwars.utils.InventoryBWUtils;
import org.bukkit.Material;
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
    private final List<ItemShopUpgradable> itemsUpgradable = new ArrayList<>();
    private boolean permanentShears;

    public BWGamePlayer(HyriGame<?> game, Player player) {
        super(game, player);
    }

    public void respawn(){
        this.player.getInventory().addItem(new ItemStack(Material.WOOD_SWORD));
        giveItemsPermanent();
        giveArmor();
    }

    public void giveItemsPermanent(){
        //TODO à refaire un peu mieux
        Arrays.asList(BWMaterial.values()).forEach(material -> {
            if(material.isUpgradable() && itemsUpgradable.contains((ItemShopUpgradable) material.getItemShop().getItem())){
                this.player.getInventory().addItem(material.getItemShop().getItemStack());
            }
        });
    }

    public void giveArmor(){
        this.player.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET)
                .withLeatherArmorColor(this.getTeam().getColor().getDyeColor().getColor()).build());
        this.player.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .withLeatherArmorColor(this.getTeam().getColor().getDyeColor().getColor()).build());
        if(permanentArmor == null){
            this.player.getInventory().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS)
                    .withLeatherArmorColor(this.getTeam().getColor().getDyeColor().getColor()).build());
            this.player.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS)
                    .withLeatherArmorColor(this.getTeam().getColor().getDyeColor().getColor()).build());
        }else{
            ArmorBW armorBW = ((ArmorBW)permanentArmor.getItemShop().getItem());
            this.player.getInventory().setLeggings(armorBW.getLeggings());
            this.player.getInventory().setBoots(armorBW.getBoots());
        }
    }

    public void giveArmor(BWMaterial material){
        this.setPermanentArmor(material);
        if(!this.player.hasPotionEffect(PotionEffectType.INVISIBILITY))
            this.giveArmor();
    }

    public void setScoreboard(BWGameScoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public void setPermanentArmor(BWMaterial material){
        ItemShop itemShop = material.getItemShop().getItem();
        if(material.getCategory() == EHyriBWShopNavBar.ARMOR && itemShop instanceof ArmorBW){
            if(this.permanentArmor == null || ((ArmorBW) this.permanentArmor.getItemShop().getItem()).getLevel() < ((ArmorBW)itemShop).getLevel())
                this.permanentArmor = material;
        }
    }

    public BWMaterial getPermanentArmor() {
        return permanentArmor;
    }

    public boolean hasPermanentShears() {
        return permanentShears;
    }

    public void setPermanentShears(boolean hasPermanentShears) {
        this.permanentShears = hasPermanentShears;
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
        if(newMaterial.isUpgradable()) {
            if (!this.itemsUpgradable.isEmpty()) {
                for (ItemShopUpgradable item : this.itemsUpgradable) {
                    if (item.getHyriMaterial() == newMaterial) {
                        item.updateNextTier();
                        this.giveUpgradeItem(newMaterial);
                        return;
                    }
                }
            }
            this.itemsUpgradable.add(newMaterial.getItemUpgradable());
            this.giveUpgradeItem(newMaterial);
        }
    }

    public boolean hasUpgradeMaterial(BWMaterial material) {
        if(material.isUpgradable()){
            for (ItemShopUpgradable item : this.itemsUpgradable) {
                if(item.getHyriMaterial() == material){
                    return true;
                }
            }
        }
        return false;
    }

    public List<ItemShopUpgradable> getItemsUpgradable() {
        return this.itemsUpgradable;
    }

    public ItemShopUpgradable getItemUpgradable(BWMaterial material){
        return this.itemsUpgradable.stream().filter(item -> item.getHyriMaterial() == material).findFirst().orElse(null);
    }

    public void clearArmor() {
        this.player.getInventory().setArmorContents(null);
    }
}
