package fr.hyriode.bedwars.game.team.upgrade;

import fr.hyriode.hyrame.language.HyriLanguageMessage;

public interface BWUpgrade {

    String getKeyName();

    BWUpgradeTier[] getUpgradesTier();

    HyriLanguageMessage getName();

    HyriLanguageMessage getDescription();

    BWUpgradeTier getUpgradeTier(int i);

    BWUpgradeTier getNextUpgradeTier(int i);

    boolean isUpgrading();

    int getMaxTier();

    BWUpgrade setEUpgrades(EBWUpgrades eUpgrades);
}
