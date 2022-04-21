package fr.hyriode.bedwars.game.material;

import fr.hyriode.bedwars.configuration.HyriBWConfiguration;

public class BWMaterials {

    public static void init(HyriBWConfiguration conf){
        HyriBWConfiguration.Ores ores = conf.getOres();
        BWMaterial.WOOL.getItemShop().changePrice(ores.getWoolPrice());
        BWMaterial.HARD_CLAY.getItemShop().changePrice(ores.getHardClayPrice());
        BWMaterial.GLASS.getItemShop().changePrice(ores.getGlassPrice());
        BWMaterial.END_STONE.getItemShop().changePrice(ores.getEndStonePrice());
        BWMaterial.LADDER.getItemShop().changePrice(ores.getLadderPrice());
        BWMaterial.PLANKS.getItemShop().changePrice(ores.getPlanksPrice());
        BWMaterial.OBSIDIAN.getItemShop().changePrice(ores.getObsidianPrice());

        BWMaterial.STONE_SWORD.getItemShop().changePrice(ores.getStoneSwordPrice());
        BWMaterial.IRON_SWORD.getItemShop().changePrice(ores.getIronSwordPrice());
        BWMaterial.DIAMOND_SWORD.getItemShop().changePrice(ores.getDiamondSwordPrice());
        BWMaterial.STICK.getItemShop().changePrice(ores.getStickPrice());

        BWMaterial.CHAINMAIL_ARMOR.getItemShop().changePrice(ores.getChainmailArmorPrice());
        BWMaterial.IRON_ARMOR.getItemShop().changePrice(ores.getIronArmorPrice());
        BWMaterial.DIAMOND_ARMOR.getItemShop().changePrice(ores.getDiamondArmorPrice());

        BWMaterial.SHEARS.getItemShop().changePrice(ores.getShearsPrice());
        BWMaterial.PICKAXE.getItemUpgradable().getTierItem(0).changePrice(ores.getPickaxeTier1Price());
        BWMaterial.PICKAXE.getItemUpgradable().getTierItem(1).changePrice(ores.getPickaxeTier2Price());
        BWMaterial.PICKAXE.getItemUpgradable().getTierItem(2).changePrice(ores.getPickaxeTier3Price());
        BWMaterial.PICKAXE.getItemUpgradable().getTierItem(3).changePrice(ores.getPickaxeTier4Price());
        BWMaterial.AXE.getItemUpgradable().getTierItem(0).changePrice(ores.getAxeTier1Price());
        BWMaterial.AXE.getItemUpgradable().getTierItem(1).changePrice(ores.getAxeTier2Price());
        BWMaterial.AXE.getItemUpgradable().getTierItem(2).changePrice(ores.getAxeTier3Price());
        BWMaterial.AXE.getItemUpgradable().getTierItem(3).changePrice(ores.getAxeTier4Price());

        BWMaterial.ARROW.getItemShop().changePrice(ores.getArrowPrice());
        BWMaterial.BOW.getItemShop().changePrice(ores.getBowPrice());
        BWMaterial.BOW_POWER.getItemShop().changePrice(ores.getBowPowerPrice());
        BWMaterial.BOW_PUNCH.getItemShop().changePrice(ores.getBowPunchPrice());

        BWMaterial.POTION_SPEED.getItemShop().changePrice(ores.getPotionSpeedPrice());
        BWMaterial.POTION_JUMP.getItemShop().changePrice(ores.getPotionJumpPrice());
        BWMaterial.POTION_INVISIBILITY.getItemShop().changePrice(ores.getPotionInvisibilityPrice());

        BWMaterial.GOLDEN_APPLE.getItemShop().changePrice(ores.getGoldenApplePrice());
        BWMaterial.BEDBUG.getItemShop().changePrice(ores.getBedBugPrice());
        BWMaterial.DREAM_DEFENDER.getItemShop().changePrice(ores.getDreamDefenderPrice());
        BWMaterial.FIREBALL.getItemShop().changePrice(ores.getFireballPrice());
        BWMaterial.TNT.getItemShop().changePrice(ores.getTntPrice());
        BWMaterial.ENDER_PEARL.getItemShop().changePrice(ores.getEnderPearlPrice());
        BWMaterial.WATER.getItemShop().changePrice(ores.getWaterPrice());
        BWMaterial.BRIDGE_EGG.getItemShop().changePrice(ores.getBridgeEggPrice());
        BWMaterial.MAGIC_MILK.getItemShop().changePrice(ores.getMagicMilkPrice());
        BWMaterial.SPONGE.getItemShop().changePrice(ores.getSpongePrice());
        BWMaterial.COMPACT_POP_UP_TOWER.getItemShop().changePrice(ores.getPopupTowerPrice());

    }
}
