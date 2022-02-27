package fr.hyriode.bedwars.game.team.upgrade;

import fr.hyriode.bedwars.game.team.BWGameTeam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BWUpgrades {

    private final HashMap<BWUpgrade, BWUpgradeTier> upgrades = new HashMap<>();

    private final BWGameTeam team;

    public BWUpgrades(BWGameTeam team){
        this.team = team;
    }

    public HashMap<BWUpgrade, BWUpgradeTier> getUpgrades() {
        return upgrades;
    }

    public boolean containsUpgrade(String name){
        if(this.upgrades.isEmpty())
            return false;
        return this.upgrades.keySet().stream().map(BWUpgrade::getKeyName).collect(Collectors.toList()).contains(name);
    }

    public BWUpgrade getUpgrade(String name){
        if(this.containsUpgrade(name)) {
            return this.upgrades.keySet().stream().filter(upgrade -> upgrade.getKeyName().equals(name))
                    .collect(Collectors.toList()).get(0);
        }
        BWUpgrade base = Arrays.stream(EBWUpgrades.values()).filter(ebwUpgrades -> ebwUpgrades.getUpgrade().getKeyName().equals(name))
                .collect(Collectors.toList()).get(0).getUpgrade();
        this.upgrades.put(base, base.getUpgradeTier(0));
        return base;
    }

    public BWUpgradeTier getCurrentUpgradeTier(String name){
        return this.upgrades.get(this.getUpgrade(name));
    }

    public boolean upgrade(String name){
        if(this.getCurrentUpgradeTier(name).getTier() + 1 > this.getUpgrade(name).getMaxTier())
            return false;
        BWUpgrade upgrade = this.getUpgrade(name);
        BWUpgradeTier tier = upgrade.getNextUpgradeTier(this.getCurrentUpgradeTier(name).getTier());
        if(this.upgrades.get(upgrade) != tier) {
            this.upgrades.put(upgrade, tier);
            return true;
        }
        return false;

    }
}
