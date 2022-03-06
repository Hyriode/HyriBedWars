package fr.hyriode.bedwars.game.team.upgrade;

import fr.hyriode.bedwars.game.team.BWGameTeam;

import java.util.HashMap;
import java.util.stream.Collectors;

public class BWTeamUpgrades {

    private final HashMap<BWUpgrade, BWUpgradeTier> upgrades = new HashMap<>();

    private final BWGameTeam team;

    public BWTeamUpgrades(BWGameTeam team){
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
        return null;
    }

    public BWUpgradeTier getCurrentUpgradeTier(String name){
        return this.upgrades.get(this.getUpgrade(name));
    }

    public boolean canUpgrade(String name){
        return this.containsUpgrade(name) && this.getCurrentUpgradeTier(name).getTier() + 1 <= this.getUpgrade(name).getMaxTier();
    }

    public boolean upgrade(BWUpgrade upgrades){
        String name = upgrades.getKeyName();
        if(this.containsUpgrade(name)) {
            if (!this.canUpgrade(name)) {
                return false;
            }
            BWUpgrade upgrade = this.getUpgrade(name);

            BWUpgradeTier tier = upgrade.getNextUpgradeTier(this.getCurrentUpgradeTier(name).getTier());

            if (tier.getTier() == 0) {
                this.upgrades.put(upgrade, tier);
                return true;
            }
            if (this.upgrades.get(upgrade).getTier() != tier.getTier()) {
                this.upgrades.put(upgrade, tier);
                return true;
            }
        }else{
            this.upgrades.put(upgrades, upgrades.getUpgradeTier(0));
            return true;
        }
        return false;

    }
}
