package fr.hyriode.bedwars.game.material;

import fr.hyriode.bedwars.configuration.BWOreConfig;
import fr.hyriode.bedwars.game.BWGameType;

public class BWMaterials {

    public static void init(BWGameType gameType){
        BWOreConfig ores = new BWOreConfig();

        BWMaterial.WOOL.getItemShop().changePrice(ores.getWoolPrice(gameType));
        BWMaterial.HARD_CLAY.getItemShop().changePrice(ores.getHardClayPrice(gameType));
        BWMaterial.GLASS.getItemShop().changePrice(ores.getGlassPrice(gameType));
        BWMaterial.END_STONE.getItemShop().changePrice(ores.getEndStonePrice(gameType));
        BWMaterial.LADDER.getItemShop().changePrice(ores.getLadderPrice(gameType));
        BWMaterial.PLANKS.getItemShop().changePrice(ores.getPlanksPrice(gameType));
        BWMaterial.OBSIDIAN.getItemShop().changePrice(ores.getObsidianPrice(gameType));

        BWMaterial.STONE_SWORD.getItemShop().changePrice(ores.getStoneSwordPrice(gameType));
        BWMaterial.IRON_SWORD.getItemShop().changePrice(ores.getIronSwordPrice(gameType));
        BWMaterial.DIAMOND_SWORD.getItemShop().changePrice(ores.getDiamondSwordPrice(gameType));
        BWMaterial.STICK.getItemShop().changePrice(ores.getStickPrice(gameType));

        BWMaterial.CHAINMAIL_ARMOR.getItemShop().changePrice(ores.getChainmailArmorPrice(gameType));
        BWMaterial.IRON_ARMOR.getItemShop().changePrice(ores.getIronArmorPrice(gameType));
        BWMaterial.DIAMOND_ARMOR.getItemShop().changePrice(ores.getDiamondArmorPrice(gameType));

        BWMaterial.SHEARS.getItemShop().changePrice(ores.getShearsPrice(gameType));
        BWMaterial.PICKAXE.getItemUpgradable().getTierItem(0).changePrice(ores.getPickaxeTier1Price(gameType));
        BWMaterial.PICKAXE.getItemUpgradable().getTierItem(1).changePrice(ores.getPickaxeTier2Price(gameType));
        BWMaterial.PICKAXE.getItemUpgradable().getTierItem(2).changePrice(ores.getPickaxeTier3Price(gameType));
        BWMaterial.PICKAXE.getItemUpgradable().getTierItem(3).changePrice(ores.getPickaxeTier4Price(gameType));
        BWMaterial.AXE.getItemUpgradable().getTierItem(0).changePrice(ores.getAxeTier1Price(gameType));
        BWMaterial.AXE.getItemUpgradable().getTierItem(1).changePrice(ores.getAxeTier2Price(gameType));
        BWMaterial.AXE.getItemUpgradable().getTierItem(2).changePrice(ores.getAxeTier3Price(gameType));
        BWMaterial.AXE.getItemUpgradable().getTierItem(3).changePrice(ores.getAxeTier4Price(gameType));

        BWMaterial.ARROW.getItemShop().changePrice(ores.getArrowPrice(gameType));
        BWMaterial.BOW.getItemShop().changePrice(ores.getBowPrice(gameType));
        BWMaterial.BOW_POWER.getItemShop().changePrice(ores.getBowPowerPrice(gameType));
        BWMaterial.BOW_PUNCH.getItemShop().changePrice(ores.getBowPunchPrice(gameType));

        BWMaterial.POTION_SPEED.getItemShop().changePrice(ores.getPotionSpeedPrice(gameType));
        BWMaterial.POTION_JUMP.getItemShop().changePrice(ores.getPotionJumpPrice(gameType));
        BWMaterial.POTION_INVISIBILITY.getItemShop().changePrice(ores.getPotionInvisibilityPrice(gameType));

        BWMaterial.GOLDEN_APPLE.getItemShop().changePrice(ores.getGoldenApplePrice(gameType));
        BWMaterial.BEDBUG.getItemShop().changePrice(ores.getBedBugPrice(gameType));
        BWMaterial.DREAM_DEFENDER.getItemShop().changePrice(ores.getDreamDefenderPrice(gameType));
        BWMaterial.FIREBALL.getItemShop().changePrice(ores.getFireballPrice(gameType));
        BWMaterial.TNT.getItemShop().changePrice(ores.getTntPrice(gameType));
        BWMaterial.ENDER_PEARL.getItemShop().changePrice(ores.getEnderPearlPrice(gameType));
        BWMaterial.WATER.getItemShop().changePrice(ores.getWaterPrice(gameType));
        BWMaterial.BRIDGE_EGG.getItemShop().changePrice(ores.getBridgeEggPrice(gameType));
        BWMaterial.MAGIC_MILK.getItemShop().changePrice(ores.getMagicMilkPrice(gameType));
        BWMaterial.SPONGE.getItemShop().changePrice(ores.getSpongePrice(gameType));
        BWMaterial.COMPACT_POP_UP_TOWER.getItemShop().changePrice(ores.getPopupTowerPrice(gameType));

    }
}
