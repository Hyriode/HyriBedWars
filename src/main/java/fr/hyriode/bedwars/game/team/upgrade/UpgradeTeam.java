package fr.hyriode.bedwars.game.team.upgrade;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.team.BWGameTeam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpgradeTeam {

    private final List<UpgradeTeam.Upgrade> upgrades;

    public UpgradeTeam(){
        this.upgrades = new ArrayList<>();
    }

    public List<UpgradeTeam.Upgrade> getUpgrades() {
        return upgrades;
    }

    public int getTier(String name){
        return this.hasUpgrade(name) ? this.getUpgradeByName(name).getTier() : 0;
    }

    public int getNextTier(String name){
        return this.hasUpgrade(name) ? this.getTier(name) + 1 : 0;
    }

    public Upgrade getUpgradeByName(String name){
        return this.upgrades.stream().filter(upgrade -> upgrade.getName().equals(name)).findFirst().orElse(null);
    }

    public boolean hasUpgrade(String name) {
        return this.getUpgradeByName(name) != null;
    }

    public Upgrade addUpgrade(String name) {
        Upgrade upgrade = new Upgrade(name, 0);
        this.upgrades.add(upgrade);
        return upgrade;
    }

    public static class Upgrade{

        private final String name;
        private int tier;

        public Upgrade(String name, int tier){
            this.name = name;
            this.tier = tier;
        }

        public String getName() {
            return name;
        }

        public int getTier() {
            return tier;
        }

        public void addTier(int tier){
            this.tier += tier;
        }

        public void addTier(){
            this.addTier(1);
        }


    }
}
