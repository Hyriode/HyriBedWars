package fr.hyriode.bedwars.game.team.upgrade;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.utils.InventoryBWUtils;
import fr.hyriode.bedwars.utils.StringBWUtils;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BWUpgrade {

    private EBWUpgrades eUpgrades;

    private final BWUpgradeTier[] upgradesTier;
    private final String keyName;

    public BWUpgrade(String keyName, BWUpgradeTier... upgradesTier){
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
        final BWUpgradeTier currentTier = team.getUpgrades().containsUpgrade(this.getKeyName()) ? team.getUpgrades().getCurrentUpgradeTier(this.getKeyName()) : null;
        final BWUpgradeTier nextTier = !team.getUpgrades().containsUpgrade(this.getKeyName()) ? this.getUpgradeTier(0) : this.getNextUpgradeTier(team.getUpgrades().getCurrentUpgradeTier(this.getKeyName()) != null ? team.getUpgrades().getCurrentUpgradeTier(this.getKeyName()).getTier() : 0);

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
                .withName((hasItems ? ChatColor.GREEN : ChatColor.RED) + this.getName().getForPlayer(player) + (this.getMaxTier() == 0 ? "" : " " + StringBWUtils.getLevelLang(team.getUpgrades().containsUpgrade(this.getKeyName()) ? team.getUpgrades().getCurrentUpgradeTier(this.getKeyName()).getTier() + 1 : 1)))
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
        System.out.println(this.getMaxTier());
        if(i + 1 <= this.getMaxTier()) {
            System.out.println("OUI");
            return Arrays.asList(this.upgradesTier).get(i + 1);
        }
        System.out.println("nnG");
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
