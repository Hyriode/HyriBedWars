package fr.hyriode.bedwars.game.team.upgrade;

import fr.hyriode.bedwars.game.team.BWGameTeam;

import java.util.HashMap;

public class BWTeamUpgrades {

    private final HashMap<EBWUpgrades, BWUpgradeTier> upgrades = new HashMap<>();

    private final BWGameTeam team;

    public BWTeamUpgrades(BWGameTeam team){
        this.team = team;
    }

    public HashMap<EBWUpgrades, BWUpgradeTier> getUpgrades() {
        return upgrades;
    }

    public boolean containsUpgrade(EBWUpgrades upgrade){
        if(this.upgrades.isEmpty())
            return false;
        return this.upgrades.containsKey(upgrade);
    }

    public BWUpgradeTier getCurrentUpgradeTier(EBWUpgrades upgrade){
        return this.upgrades.get(upgrade);
    }

    public boolean canUpgrade(EBWUpgrades upgrade){
        return this.containsUpgrade(upgrade) && this.getCurrentUpgradeTier(upgrade).getTier() + 1 <= upgrade.getMaxTier();
    }

    public boolean upgrade(EBWUpgrades upgrade){
        if(this.containsUpgrade(upgrade)) {
            if (!this.canUpgrade(upgrade)) {
                return false;
            }
            BWUpgradeTier tier = upgrade.getNextUpgradeTier(this.getCurrentUpgradeTier(upgrade).getTier());

            if (tier.getTier() == 0) {
                this.upgrades.put(upgrade, tier);
                return true;
            }
            if (this.upgrades.get(upgrade).getTier() != tier.getTier()) {
                this.upgrades.put(upgrade, tier);
                return true;
            }
        }else{
            this.upgrades.put(upgrade, upgrade.getUpgradeTier(0));
            return true;
        }
        return false;

    }
}
