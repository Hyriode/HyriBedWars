package fr.hyriode.bedwars.game.team.upgrade;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGameOre;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.OreStack;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.utils.InventoryBWUtils;
import fr.hyriode.bedwars.utils.StringBWUtils;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum EBWUpgrades implements BWUpgrade{

    SHARPNESS("sharpness", new BWUpgradeTier(0, "sharpness", new ItemStack(Material.IRON_SWORD), new OreStack(BWGameOre.DIAMOND, 4))),
    PROTECTION_ARMOR("reinforced_armor",
            new BWUpgradeTier(0, "protection1", new ItemStack(Material.IRON_CHESTPLATE), new OreStack(BWGameOre.DIAMOND, 2)),
            new BWUpgradeTier(1, "protection2", new ItemStack(Material.IRON_CHESTPLATE, 2), new OreStack(BWGameOre.DIAMOND, 4)),
            new BWUpgradeTier(2, "protection3", new ItemStack(Material.IRON_CHESTPLATE, 3), new OreStack(BWGameOre.DIAMOND, 8)),
            new BWUpgradeTier(3, "protection4", new ItemStack(Material.IRON_CHESTPLATE, 4), new OreStack(BWGameOre.DIAMOND, 16))
    ),
    HASTE("maniac_miner",
            new BWUpgradeTier(0, "haste1", new ItemStack(Material.IRON_PICKAXE), new OreStack(BWGameOre.DIAMOND, 2)),
            new BWUpgradeTier(1, "haste2", new ItemStack(Material.IRON_PICKAXE, 2), new OreStack(BWGameOre.DIAMOND, 4))
    ),
    FORGE("forge",
            new BWUpgradeTier(0, "forge1", new ItemStack(Material.FURNACE), new OreStack(BWGameOre.DIAMOND, 2)),
            new BWUpgradeTier(1, "forge2", new ItemStack(Material.FURNACE, 2), new OreStack(BWGameOre.DIAMOND, 4)),
            new BWUpgradeTier(2, "forge3", new ItemStack(Material.FURNACE, 3), new OreStack(BWGameOre.DIAMOND, 6)),
            new BWUpgradeTier(3, "forge4", new ItemStack(Material.FURNACE, 4), new OreStack(BWGameOre.DIAMOND, 8))
    ),
    HEAL_POOL("heal_pool", new BWUpgradeTier(0, "heal_pool", new ItemStack(Material.BEACON), new OreStack(BWGameOre.DIAMOND, 1))),
    DRAGON("dragon_buff", new BWUpgradeTier(0, "dragon_buff", new ItemStack(Material.DRAGON_EGG), new OreStack(BWGameOre.DIAMOND, 5))),
    ;


    private EBWUpgrades eUpgrades;

    private final BWUpgradeTier[] upgradesTier;
    private final String keyName;

    EBWUpgrades(String keyName, BWUpgradeTier... upgradesTier){
        this.keyName = keyName;
        this.upgradesTier = upgradesTier;
    }

    public String getKeyName() {
        return keyName;
    }

    public BWUpgradeTier[] getUpgradesTier() {
        return upgradesTier;
    }

    public ItemStack getItemUpgrade(Player player, BWGameTeam team){
        List<String> lore = new ArrayList<>();
        final BWUpgradeTier currentTier = team.getUpgrades().containsUpgrade(this) ? team.getUpgrades().getCurrentUpgradeTier(this) : null;
        final BWUpgradeTier nextTier = !team.getUpgrades().containsUpgrade(this) ? this.getUpgradeTier(0) : this.getNextUpgradeTier(team.getUpgrades().getCurrentUpgradeTier(this) != null ? team.getUpgrades().getCurrentUpgradeTier(this).getTier() : 0);

        if(this.getDescription() != null) {
            String[] description = this.getDescription().getForPlayer(player).split("\n");
            for(String desc : description){
                lore.add(ChatColor.GRAY + desc);
            }
            lore.add(ChatColor.GRAY + " ");
        }

        if(this.getMaxTier() == 0){
            lore.add(ChatColor.GRAY + "Cost: " + StringBWUtils.getCountPriceAsString(player, nextTier.getPrice()));
        }else{
            int j = 0;
            for(BWUpgradeTier tier : this.upgradesTier){
                boolean hasTier = currentTier != null && currentTier.getTier() >= j;
                lore.add((hasTier ? ChatColor.GREEN : ChatColor.GRAY) + "Tier " + (j + 1) + ": " + tier.getName().getForPlayer(player) + ", " + StringBWUtils.getCountPriceAsString(player, tier.getPrice()));
                ++j;
            }
        }
        if(currentTier != null && currentTier.getTier() == this.getMaxTier()) {
            lore.add(ChatColor.GREEN + "Unlocked");
        }
        boolean hasItems = InventoryBWUtils.hasItems(player, nextTier.getPrice());
        return new ItemBuilder(currentTier != null ? currentTier.getItemStack() : nextTier.getItemStack())
                .withName((hasItems ? ChatColor.GREEN : ChatColor.RED) + this.getName().getForPlayer(player) + (this.getMaxTier() == 0 ? "" : " " + StringBWUtils.getLevelLang(team.getUpgrades().containsUpgrade(this) ? team.getUpgrades().getCurrentUpgradeTier(this).getTier() + 1 : 1)))
                .withLore(lore)
                .withAllItemFlags().build();
    }

    public HyriLanguageMessage getName(){
        return HyriBedWars.getLanguageManager().getMessage("upgrade." + this.keyName + ".name");
    }

    public HyriLanguageMessage getDescription(){
        return HyriBedWars.getLanguageManager().getMessage("upgrade." + this.keyName + ".lore");
    }

    public BWUpgradeTier getUpgradeTier(int i){
        return Arrays.asList(this.upgradesTier).get(i);
    }

    public BWUpgradeTier getNextUpgradeTier(int i){
        if(i + 1 <= this.getMaxTier()) {
            return Arrays.asList(this.upgradesTier).get(i + 1);
        }
        return Arrays.asList(this.upgradesTier).get(i);
    }

    public boolean isUpgrading(){
        return upgradesTier.length > 1;
    }

    public int getMaxTier(){
        return upgradesTier.length - 1;
    }

    public EBWUpgrades getEUpgrade(){
        return this.eUpgrades;
    }

    public BWUpgrade setEUpgrades(EBWUpgrades eUpgrades) {
        this.eUpgrades = eUpgrades;
        return this;
    }
}
