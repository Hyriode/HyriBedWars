package fr.hyriode.bedwars.game.team.upgrade;

import fr.hyriode.bedwars.configuration.BWUpgradeConfig;
import fr.hyriode.bedwars.game.BWGameType;

public class BWUpgrades {

    public static void init(BWGameType gameType){
        BWUpgradeConfig config = new BWUpgradeConfig();

        EBWUpgrades.SHARPNESS.getUpgradeTier(0).changePrice(config.getSharpnessPrice(gameType));

        EBWUpgrades.PROTECTION_ARMOR.getUpgradeTier(0).changePrice(config.getProtectionArmorPrice0(gameType));
        EBWUpgrades.PROTECTION_ARMOR.getUpgradeTier(1).changePrice(config.getProtectionArmorPrice1(gameType));
        EBWUpgrades.PROTECTION_ARMOR.getUpgradeTier(2).changePrice(config.getProtectionArmorPrice2(gameType));
        EBWUpgrades.PROTECTION_ARMOR.getUpgradeTier(3).changePrice(config.getProtectionArmorPrice3(gameType));

        EBWUpgrades.HASTE.getUpgradeTier(0).changePrice(config.getHastePrice0(gameType));
        EBWUpgrades.HASTE.getUpgradeTier(1).changePrice(config.getHastePrice1(gameType));

        EBWUpgrades.FORGE.getUpgradeTier(0).changePrice(config.getForgePrice0(gameType));
        EBWUpgrades.FORGE.getUpgradeTier(1).changePrice(config.getForgePrice1(gameType));
        EBWUpgrades.FORGE.getUpgradeTier(2).changePrice(config.getForgePrice2(gameType));
        EBWUpgrades.FORGE.getUpgradeTier(3).changePrice(config.getForgePrice3(gameType));

        EBWUpgrades.HEAL_POOL.getUpgradeTier(0).changePrice(config.getHealPoolPrice(gameType));

        EBWUpgrades.DRAGON.getUpgradeTier(0).changePrice(config.getDragonBuffPrice(gameType));
    }

}
