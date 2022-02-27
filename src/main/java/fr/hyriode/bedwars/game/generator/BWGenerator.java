package fr.hyriode.bedwars.game.generator;

import fr.hyriode.bedwars.game.BWGameOre;
import fr.hyriode.bedwars.game.generator.item.BWItemGenerator;

public enum BWGenerator {

    BASE_TIER_I(new BWGeneratorTier(getGeneratorName("base.tier.I"), true, new BWItemGenerator(BWGameOre.IRON.getItemStack(), 48, 20), new BWItemGenerator(BWGameOre.GOLD.getItemStack(), 16, 40))),
    BASE_TIER_II(new BWGeneratorTier(getGeneratorName("base.tier.II"), true, new BWItemGenerator(BWGameOre.IRON.getItemStack(), 48, 20))),
    BASE_TIER_III(new BWGeneratorTier(getGeneratorName("base.tier.III"), true, new BWItemGenerator(BWGameOre.IRON.getItemStack(), 48, 20))),
    BASE_TIER_IV(new BWGeneratorTier(getGeneratorName("base.tier.IV"), true, new BWItemGenerator(BWGameOre.IRON.getItemStack(), 48, 20))),// new HyriBWGeneratorTier(getGeneratorName("base.tier.IV"), 16, 10, true)),
//    DIAMOND_TIER_I(new HyriBWGeneratorTier(getGeneratorName("diamond.tier.I"), 4, 60, false)),
//    DIAMOND_TIER_II(new HyriBWGeneratorTier(getGeneratorName("diamond.tier.II"), 4, 60, false)),
//    DIAMOND_TIER_III(new HyriBWGeneratorTier(getGeneratorName("diamond.tier.III"), 4, 60, false)),
//    EMERALD_TIER_I(new HyriBWGeneratorTier(getGeneratorName("emerald.tier.I"), 2, 60, false)),
//    EMERALD_TIER_II(new HyriBWGeneratorTier(getGeneratorName("emerald.tier.II"), 2, 60, false)),
//    EMERALD_TIER_III(new HyriBWGeneratorTier(getGeneratorName("emerald.tier.III"), 2, 60, false)),

    ;

    private final BWGeneratorTier generator;

    BWGenerator(BWGeneratorTier generator){
        this.generator = generator;
    }

    public BWGeneratorTier getGenerator(){
        return this.generator;
    }

    private static String getGeneratorName(String generator){
        return "generator." + generator + ".name";
    }



}
