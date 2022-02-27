package fr.hyriode.bedwars.game.team.upgrade;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.utils.InventoryBWUtils;
import fr.hyriode.bedwars.utils.StringBWUtils;
import fr.hyriode.hyrame.game.team.HyriGameTeam;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BWUpgrade {

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

    public ItemStack getItemUpgrade(Player player, BWGameTeam team, int i){
        List<String> lore = new ArrayList<>();
        BWUpgradeTier currentTier = this.getUpgradeTier(0);

        if(this.getDescription() != null) {
            String[] description = this.getDescription().getForPlayer(player).split("\n");
            for(String desc : description){
                lore.add(ChatColor.GRAY + desc);
            }
            lore.add(ChatColor.GRAY + " ");
        }

        if(this.getMaxTier() == 0){
            lore.add(ChatColor.GRAY + "Cost: " + StringBWUtils.getCountPriceAsString(player, currentTier.getPrice()));
        }else{
            int j = 0;
            for(BWUpgradeTier tier : this.upgradesTier){
                ++j;
                lore.add(ChatColor.GRAY + "Tier " + j + ": " + tier.getName().getForPlayer(player) + ", " + StringBWUtils.getCountPriceAsString(player, tier.getPrice()));
            }
        }
        boolean hasItems = InventoryBWUtils.hasItems(player, currentTier.getPrice());
        return new ItemBuilder(currentTier.getItemStack())
                .withName((hasItems ? ChatColor.GREEN : ChatColor.RED) + this.getName().getForPlayer(player) + (this.getMaxTier() == 0 ? "" : " " + StringBWUtils.getLevelLang(team.getUpgrades().getCurrentUpgradeTier(this.getKeyName()).getTier() + 1)))
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
        if(this.getTiers() + 1 <= this.getMaxTier()) {
            return Arrays.asList(this.upgradesTier).get(i + 1);
        }
        return Arrays.asList(this.upgradesTier).get(i);
    }

    public int getTiers(){
        return this.upgradesTier.length;
    }

    public boolean isUpgrading(){
        return upgradesTier.length > 1;
    }

    public int getMaxTier(){
        return upgradesTier.length - 1;
    }
}
