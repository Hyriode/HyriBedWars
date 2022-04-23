package fr.hyriode.bedwars.configuration;

import fr.hyriode.bedwars.game.BWGameType;

public class BWUpgradeConfig {

    private int sharpnessPrice;

    private int protectionArmorPrice0;
    private int protectionArmorPrice1;
    private int protectionArmorPrice2;
    private int protectionArmorPrice3;

    private int hastePrice0;
    private int hastePrice1;

    private int forgePrice0;
    private int forgePrice1;
    private int forgePrice2;
    private int forgePrice3;

    private int healPoolPrice;

    private int dragonBuffPrice;


    public BWUpgradeConfig(){

        this.sharpnessPrice = 4;

        this.protectionArmorPrice0 = 2;
        this.protectionArmorPrice1 = 4;
        this.protectionArmorPrice2 = 8;
        this.protectionArmorPrice3 = 16;

        this.hastePrice0 = 2;
        this.hastePrice1 = 4;

        this.forgePrice0 = 2;
        this.forgePrice1 = 4;
        this.forgePrice2 = 6;
        this.forgePrice3 = 8;

        this.healPoolPrice = 1;

        this.dragonBuffPrice = 5;
    }

    public int getSharpnessPrice(BWGameType gameType) {
        switch (gameType) {
            case TRIO:
            case SQUAD:
                return 8;
        }
        return this.sharpnessPrice;
    }

    public int getProtectionArmorPrice0(BWGameType gameType) {
        switch (gameType) {
            case TRIO:
            case SQUAD:
                return 5;
        }
        return this.protectionArmorPrice0;
    }

    public int getProtectionArmorPrice1(BWGameType gameType) {
        switch (gameType) {
            case TRIO:
            case SQUAD:
                return 10;
        }
        return this.protectionArmorPrice1;
    }

    public int getProtectionArmorPrice2(BWGameType gameType) {
        switch (gameType) {
            case TRIO:
            case SQUAD:
                return 20;
        }
        return protectionArmorPrice2;
    }

    public int getProtectionArmorPrice3(BWGameType gameType) {
        switch (gameType) {
            case TRIO:
            case SQUAD:
                return 30;
        }
        return protectionArmorPrice3;
    }

    public int getHastePrice0(BWGameType gameType) {
        switch (gameType) {
            case TRIO:
            case SQUAD:
                return 4;
        }
        return hastePrice0;
    }

    public int getHastePrice1(BWGameType gameType) {
        switch (gameType) {
            case TRIO:
            case SQUAD:
                return 6;
        }
        return hastePrice1;
    }

    public int getForgePrice0(BWGameType gameType) {
        switch (gameType) {
            case TRIO:
            case SQUAD:
                return 4;
        }
        return forgePrice0;
    }

    public int getForgePrice1(BWGameType gameType) {
        switch (gameType) {
            case TRIO:
            case SQUAD:
                return 8;
        }
        return forgePrice1;
    }

    public int getForgePrice2(BWGameType gameType) {
        switch (gameType) {
            case TRIO:
            case SQUAD:
                return 12;
        }
        return forgePrice2;
    }

    public int getForgePrice3(BWGameType gameType) {
        switch (gameType) {
            case TRIO:
            case SQUAD:
                return 16;
        }
        return forgePrice3;
    }

    public int getHealPoolPrice(BWGameType gameType) {
        switch (gameType) {
            case TRIO:
            case SQUAD:
                return 3;
        }
        return healPoolPrice;
    }

    public int getDragonBuffPrice(BWGameType gameType) {
        return dragonBuffPrice;
    }
}
