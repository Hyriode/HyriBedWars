package fr.hyriode.bedwars.configuration;

import fr.hyriode.bedwars.game.BWGameType;

public class BWOreConfig{

    //BLOCKS

    private int woolPrice;
    private int hardClayPrice;
    private int glassPrice;
    private int endStonePrice;
    private int ladderPrice;
    private int planksPrice;
    private int obsidianPrice;

    //MELEE

    private int stoneSwordPrice;
    private int ironSwordPrice;
    private int diamondSwordPrice;
    private int stickPrice;

    //ARMOR

    private int chainmailArmorPrice;
    private int ironArmorPrice;
    private int diamondArmorPrice;

    // TOOLS

    private int shearsPrice;

    private int pickaxeTier1Price;
    private int pickaxeTier2Price;
    private int pickaxeTier3Price;
    private int pickaxeTier4Price;

    private int axeTier1Price;
    private int axeTier2Price;
    private int axeTier3Price;
    private final int axeTier4Price;

    //RANGED

    private int arrowPrice;
    private int bowPrice;
    private int bowPowerPrice;
    private int bowPunchPrice;

    //POTIONS

    private int potionSpeedPrice;
    private int potionJumpPrice;
    private int potionInvisibilityPrice;

    //UTILITY

    private int goldenApplePrice;
    private int bedBugPrice;
    private int dreamDefenderPrice;
    private int fireballPrice;
    private int tntPrice;
    private int enderPearlPrice;
    private int waterPrice;
    private int bridgeEggPrice;
    private int magicMilkPrice;
    private int spongePrice;
    private int popupTowerPrice;

    public BWOreConfig(){
        this.woolPrice = 4;
        this.hardClayPrice = 12;
        this.glassPrice = 12;
        this.endStonePrice = 24;
        this.ladderPrice = 4;
        this.planksPrice = 4;
        this.obsidianPrice = 4;

        this.stoneSwordPrice = 10;
        this.ironSwordPrice = 7;
        this.diamondSwordPrice = 4;
        this.stickPrice = 5;

        this.chainmailArmorPrice = 30;
        this.ironArmorPrice = 12;
        this.diamondArmorPrice = 6;

        this.shearsPrice = 20;

        this.pickaxeTier1Price = 10;
        this.pickaxeTier2Price = 10;
        this.pickaxeTier3Price = 3;
        this.pickaxeTier4Price = 6;

        this.axeTier1Price = 10;
        this.axeTier2Price = 10;
        this.axeTier3Price = 3;
        this.axeTier4Price = 6;

        this.arrowPrice = 2;
        this.bowPrice = 12;
        this.bowPowerPrice = 20;
        this.bowPunchPrice = 6;

        this.potionSpeedPrice = 1;
        this.potionJumpPrice = 1;
        this.potionInvisibilityPrice = 2;

        this.goldenApplePrice = 3;
        this.bedBugPrice = 30;
        this.dreamDefenderPrice = 120;
        this.fireballPrice = 40;
        this.tntPrice = 4;
        this.enderPearlPrice = 4;
        this.waterPrice = 3;
        this.bridgeEggPrice = 1;
        this.magicMilkPrice = 4;
        this.spongePrice = 3;
        this.popupTowerPrice = 24;
    }

    public int getArrowPrice(BWGameType gameType) {
        return arrowPrice;
    }

    public int getDiamondSwordPrice(BWGameType gameType) {
        switch (gameType){
            case TRIO:
            case SQUAD:
                return 3;
        }
        return diamondSwordPrice;
    }

    public int getEndStonePrice(BWGameType gameType) {
        return endStonePrice;
    }

    public int getGlassPrice(BWGameType gameType) {
        return glassPrice;
    }

    public int getHardClayPrice(BWGameType gameType) {
        return hardClayPrice;
    }

    public int getIronSwordPrice(BWGameType gameType) {
        return ironSwordPrice;
    }

    public int getLadderPrice(BWGameType gameType) {
        return ladderPrice;
    }

    public int getObsidianPrice(BWGameType gameType) {
        return obsidianPrice;
    }

    public int getPlanksPrice(BWGameType gameType) {
        return planksPrice;
    }

    public int getAxeTier1Price(BWGameType gameType) {
        return axeTier1Price;
    }

    public int getChainmailArmorPrice(BWGameType gameType) {
        return chainmailArmorPrice;
    }

    public int getStoneSwordPrice(BWGameType gameType) {
        return stoneSwordPrice;
    }

    public int getIronArmorPrice(BWGameType gameType) {
        return ironArmorPrice;
    }

    public int getAxeTier2Price(BWGameType gameType) {
        return axeTier2Price;
    }

    public int getStickPrice(BWGameType gameType) {
        return stickPrice;
    }

    public int getWoolPrice(BWGameType gameType) {
        return woolPrice;
    }

    public int getAxeTier3Price(BWGameType gameType) {
        return axeTier3Price;
    }

    public int getBowPrice(BWGameType gameType) {
        return bowPrice;
    }

    public int getFireballPrice(BWGameType gameType) {
        return fireballPrice;
    }

    public int getShearsPrice(BWGameType gameType) {
        return shearsPrice;
    }

    public int getPotionSpeedPrice(BWGameType gameType) {
        return potionSpeedPrice;
    }

    public int getGoldenApplePrice(BWGameType gameType) {
        return goldenApplePrice;
    }

    public int getDreamDefenderPrice(BWGameType gameType) {
        return dreamDefenderPrice;
    }

    public int getEnderPearlPrice(BWGameType gameType) {
        return enderPearlPrice;
    }

    public int getBridgeEggPrice(BWGameType gameType) {
        return bridgeEggPrice;
    }

    public int getAxeTier4Price(BWGameType gameType) {
        return axeTier4Price;
    }

    public int getBedBugPrice(BWGameType gameType) {
        return bedBugPrice;
    }

    public int getPickaxeTier2Price(BWGameType gameType) {
        return pickaxeTier2Price;
    }

    public int getBowPowerPrice(BWGameType gameType) {
        return bowPowerPrice;
    }

    public int getPickaxeTier1Price(BWGameType gameType) {
        return pickaxeTier1Price;
    }

    public int getPickaxeTier4Price(BWGameType gameType) {
        return pickaxeTier4Price;
    }

    public int getPopupTowerPrice(BWGameType gameType) {
        return popupTowerPrice;
    }

    public int getPotionInvisibilityPrice(BWGameType gameType) {
        return potionInvisibilityPrice;
    }

    public int getSpongePrice(BWGameType gameType) {
        switch (gameType){
            case TRIO:
            case SQUAD:
                return 6;
        }
        return spongePrice;
    }

    public int getMagicMilkPrice(BWGameType gameType) {
        return magicMilkPrice;
    }

    public int getDiamondArmorPrice(BWGameType gameType) {
        return diamondArmorPrice;
    }

    public int getTntPrice(BWGameType gameType) {
        return tntPrice;
    }

    public int getPickaxeTier3Price(BWGameType gameType) {
        return pickaxeTier3Price;
    }

    public int getWaterPrice(BWGameType gameType) {
        switch (gameType){
            case TRIO:
            case SQUAD:
                return 6;
        }
        return waterPrice;
    }

    public int getBowPunchPrice(BWGameType gameType) {
        return bowPunchPrice;
    }

    public int getPotionJumpPrice(BWGameType gameType) {
        return potionJumpPrice;
    }
}
