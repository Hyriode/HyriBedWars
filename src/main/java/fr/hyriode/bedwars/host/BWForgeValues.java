package fr.hyriode.bedwars.host;

import fr.hyriode.bedwars.game.generator.GeneratorManager;
import fr.hyriode.bedwars.game.shop.ItemMoney;
import fr.hyriode.hyrame.HyrameLoader;
import fr.hyriode.hyrame.game.util.value.HostValueModifier;
import fr.hyriode.hyrame.game.util.value.ValueProvider;
import fr.hyriode.api.HyriAPI;

import java.util.ArrayList;
import java.util.List;

public class BWForgeValues {

    private static final String IRON = ItemMoney.IRON.getName();
    private static final String GOLD = ItemMoney.GOLD.getName();
    private static final String EMERALD = ItemMoney.EMERALD.getName();

    private static final List<GeneratorModifier<Integer>> SPAWN_LIMIT = new ArrayList<>();
    private static final List<GeneratorModifier<Integer>> SPAWN_BETWEEN = new ArrayList<>();
    private static final List<GeneratorModifier<Boolean>> SPLITTING = new ArrayList<>();

    public static void init() {
        System.out.println("init values");
//        addSpawnLimit(GeneratorManager.FORGE, 0, IRON);
//        addSpawnBetween(GeneratorManager.FORGE, 0, IRON);
//        addSplitting(GeneratorManager.FORGE, 0, IRON);
//        addSpawnLimit(GeneratorManager.FORGE, 0, GOLD);
//        addSpawnBetween(GeneratorManager.FORGE, 0, GOLD);
//        addSplitting(GeneratorManager.FORGE, 0, GOLD);
//
//        addSpawnLimit(GeneratorManager.FORGE, 1, IRON);
//        addSpawnBetween(GeneratorManager.FORGE, 1, IRON);
//        addSplitting(GeneratorManager.FORGE, 1, IRON);
//        addSpawnLimit(GeneratorManager.FORGE, 1, GOLD);
//        addSpawnBetween(GeneratorManager.FORGE, 1, GOLD);
//        addSplitting(GeneratorManager.FORGE, 1, GOLD);
//
//        addSpawnLimit(GeneratorManager.FORGE, 2, IRON);
//        addSpawnBetween(GeneratorManager.FORGE, 2, IRON);
//        addSplitting(GeneratorManager.FORGE, 2, IRON);
//        addSpawnLimit(GeneratorManager.FORGE, 2, GOLD);
//        addSpawnBetween(GeneratorManager.FORGE, 2, GOLD);
//        addSplitting(GeneratorManager.FORGE, 2, GOLD);
//        addSpawnLimit(GeneratorManager.FORGE, 2, EMERALD);
//        addSpawnBetween(GeneratorManager.FORGE, 2, EMERALD);
//        addSplitting(GeneratorManager.FORGE, 2, EMERALD);
//
//        addSpawnLimit(GeneratorManager.FORGE, 3, IRON);
//        addSpawnBetween(GeneratorManager.FORGE, 3, IRON);
//        addSplitting(GeneratorManager.FORGE, 3, IRON);
//        addSpawnLimit(GeneratorManager.FORGE, 3, GOLD);
//        addSpawnBetween(GeneratorManager.FORGE, 3, GOLD);
//        addSplitting(GeneratorManager.FORGE, 3, GOLD);
//        addSpawnLimit(GeneratorManager.FORGE, 3, EMERALD);
//        addSpawnBetween(GeneratorManager.FORGE, 3, EMERALD);
//        addSplitting(GeneratorManager.FORGE, 3, EMERALD);
    }

    private static void addSpawnLimit(String generator, int tier, String drop) {
        SPAWN_LIMIT.add(new GeneratorModifier<>(generator, tier, drop, new ValueProvider<>(0)
                .addModifiers(new HostValueModifier<>(1, Integer.class, formatSpawnLimit(generator, tier, drop)))));
    }

    private static void addSpawnBetween(String generator, int tier, String drop) {
        SPAWN_BETWEEN.add(new GeneratorModifier<>(generator, tier, drop, new ValueProvider<>(0)
                .addModifiers(new HostValueModifier<>(1, Integer.class, formatSpawnBetween(generator, tier, drop)))));
    }

    private static void addSplitting(String generator, int tier, String drop) {
        SPLITTING.add(new GeneratorModifier<>(generator, tier, drop, new ValueProvider<>(false)
                .addModifiers(new HostValueModifier<>(1, Boolean.class, formatSpawnBetween(generator, tier, drop)))));
    }

    public static List<GeneratorModifier<Integer>> getSpawnLimit() {
        return SPAWN_LIMIT;
    }

    public static List<GeneratorModifier<Integer>> getSpawnBetween() {
        return SPAWN_BETWEEN;
    }

    public static List<GeneratorModifier<Boolean>> getSplitting() {
        return SPLITTING;
    }

    public static int getSpawnLimit(String name, int tier, String drop) {
        GeneratorModifier<Integer> generator = SPAWN_LIMIT.stream()
                .filter(provider -> provider.getGenerator().equals(name)
                        && provider.getTier() == tier
                        && provider.getDrop().equals(drop))
                .findFirst().orElse(null);
        System.out.println("getSpawnLimit " + name + " " + tier + " " + drop + " " + generator);
        System.out.println(SPAWN_LIMIT);
        System.out.println(generator);
        if(generator != null) {
            System.out.println(generator.getValue());
            try {
                System.out.println(generator.getValue().getModifiers().get(0).getPriority());
                System.out.println(generator.getValue().getDefaultValue());
                return generator.getValue().get();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static Integer getSpawnBetween(String name, int tier, String drop) {
        GeneratorModifier<Integer> generator = SPAWN_BETWEEN.stream()
                .filter(provider -> provider.getGenerator().equals(name)
                        && provider.getTier() == tier
                        && provider.getDrop().equals(drop))
                .findFirst().orElse(null);
        if(generator != null) {
            return generator.getValue().get();
        }
        return 0;
    }

    public static Boolean getSplitting(String name, int tier, String drop) {
        GeneratorModifier<Boolean> generator = SPLITTING.stream()
                .filter(provider -> provider.getGenerator().equals(name)
                        && provider.getTier() == tier
                        && provider.getDrop().equals(drop))
                .findFirst().orElse(null);
        if(generator != null) {
            return generator.getValue().get();
        }
        return false;
    }

//    public static final ValueProvider<Integer> FORGE_TIER_0_IRON_SPAWN_LIMIT = new ValueProvider<>(0).addModifiers(new HostValueModifier<>(1, Integer.class, formatSpawnLimit(GeneratorManager.FORGE, 0, IRON)));
//    public static final ValueProvider<Integer> FORGE_TIER_0_IRON_SPAWN_BETWEEN = new ValueProvider<>(0).addModifiers(new HostValueModifier<>(1, Integer.class, formatSpawnBetween(GeneratorManager.FORGE, 0, IRON)));
//    public static final ValueProvider<Boolean> FORGE_TIER_0_IRON_SPLITTING = new ValueProvider<>(false).addModifiers(new HostValueModifier<>(1, Boolean.class, formatSplitting(GeneratorManager.FORGE, 0, IRON)));
//    public static final ValueProvider<Integer> FORGE_TIER_0_GOLD_SPAWN_LIMIT = new ValueProvider<>(0).addModifiers(new HostValueModifier<>(1, Integer.class, formatSpawnLimit(GeneratorManager.FORGE, 0, GOLD)));
//    public static final ValueProvider<Integer> FORGE_TIER_0_GOLD_SPAWN_BETWEEN = new ValueProvider<>(0).addModifiers(new HostValueModifier<>(1, Integer.class, formatSpawnBetween(GeneratorManager.FORGE, 0, GOLD)));
//    public static final ValueProvider<Boolean> FORGE_TIER_0_GOLD_SPLITTING = new ValueProvider<>(false).addModifiers(new HostValueModifier<>(1, Boolean.class, formatSplitting(GeneratorManager.FORGE, 0, GOLD)));
//
//    public static final ValueProvider<Integer> FORGE_TIER_1_IRON_SPAWN_LIMIT = new ValueProvider<>(0).addModifiers(new HostValueModifier<>(1, Integer.class, formatSpawnLimit(GeneratorManager.FORGE, 1, IRON)));
//    public static final ValueProvider<Integer> FORGE_TIER_1_IRON_SPAWN_BETWEEN = new ValueProvider<>(0).addModifiers(new HostValueModifier<>(1, Integer.class, formatSpawnBetween(GeneratorManager.FORGE, 1, IRON)));
//    public static final ValueProvider<Boolean> FORGE_TIER_1_IRON_SPLITTING = new ValueProvider<>(false).addModifiers(new HostValueModifier<>(1, Boolean.class, formatSplitting(GeneratorManager.FORGE, 1, IRON)));
//    public static final ValueProvider<Integer> FORGE_TIER_1_GOLD_SPAWN_LIMIT = new ValueProvider<>(0).addModifiers(new HostValueModifier<>(1, Integer.class, formatSpawnLimit(GeneratorManager.FORGE, 1, GOLD)));
//    public static final ValueProvider<Integer> FORGE_TIER_1_GOLD_SPAWN_BETWEEN = new ValueProvider<>(0).addModifiers(new HostValueModifier<>(1, Integer.class, formatSpawnBetween(GeneratorManager.FORGE, 1, GOLD)));
//    public static final ValueProvider<Boolean> FORGE_TIER_1_GOLD_SPLITTING = new ValueProvider<>(false).addModifiers(new HostValueModifier<>(1, Boolean.class, formatSplitting(GeneratorManager.FORGE, 1, GOLD)));
//
//    public static final ValueProvider<Integer> FORGE_TIER_2_IRON_SPAWN_LIMIT = new ValueProvider<>(0).addModifiers(new HostValueModifier<>(1, Integer.class, formatSpawnLimit(GeneratorManager.FORGE, 2, IRON)));
//    public static final ValueProvider<Integer> FORGE_TIER_2_IRON_SPAWN_BETWEEN = new ValueProvider<>(0).addModifiers(new HostValueModifier<>(1, Integer.class, formatSpawnBetween(GeneratorManager.FORGE, 2, IRON)));
//    public static final ValueProvider<Boolean> FORGE_TIER_2_IRON_SPLITTING = new ValueProvider<>(false).addModifiers(new HostValueModifier<>(1, Boolean.class, formatSplitting(GeneratorManager.FORGE, 2, IRON)));
//    public static final ValueProvider<Integer> FORGE_TIER_2_GOLD_SPAWN_LIMIT = new ValueProvider<>(0).addModifiers(new HostValueModifier<>(1, Integer.class, formatSpawnLimit(GeneratorManager.FORGE, 2, GOLD)));
//    public static final ValueProvider<Integer> FORGE_TIER_2_GOLD_SPAWN_BETWEEN = new ValueProvider<>(0).addModifiers(new HostValueModifier<>(1, Integer.class, formatSpawnBetween(GeneratorManager.FORGE, 2, GOLD)));
//    public static final ValueProvider<Boolean> FORGE_TIER_2_GOLD_SPLITTING = new ValueProvider<>(false).addModifiers(new HostValueModifier<>(1, Boolean.class, formatSplitting(GeneratorManager.FORGE, 2, GOLD)));
//    public static final ValueProvider<Integer> FORGE_TIER_2_EMERALD_SPAWN_LIMIT = new ValueProvider<>(0).addModifiers(new HostValueModifier<>(1, Integer.class, formatSpawnLimit(GeneratorManager.FORGE, 2, EMERALD)));
//    public static final ValueProvider<Integer> FORGE_TIER_2_EMERALD_SPAWN_BETWEEN = new ValueProvider<>(0).addModifiers(new HostValueModifier<>(1, Integer.class, formatSpawnBetween(GeneratorManager.FORGE, 2, EMERALD)));
//    public static final ValueProvider<Boolean> FORGE_TIER_2_EMERALD_SPLITTING = new ValueProvider<>(false).addModifiers(new HostValueModifier<>(1, Boolean.class, formatSplitting(GeneratorManager.FORGE, 2, EMERALD)));
//
//    public static final ValueProvider<Integer> FORGE_TIER_3_IRON_SPAWN_LIMIT = new ValueProvider<>(0).addModifiers(new HostValueModifier<>(1, Integer.class, formatSpawnLimit(GeneratorManager.FORGE, 3, IRON)));
//    public static final ValueProvider<Integer> FORGE_TIER_3_IRON_SPAWN_BETWEEN = new ValueProvider<>(0).addModifiers(new HostValueModifier<>(1, Integer.class, formatSpawnBetween(GeneratorManager.FORGE, 3, IRON)));
//    public static final ValueProvider<Boolean> FORGE_TIER_3_IRON_SPLITTING = new ValueProvider<>(false).addModifiers(new HostValueModifier<>(1, Boolean.class, formatSplitting(GeneratorManager.FORGE, 3, IRON)));
//    public static final ValueProvider<Integer> FORGE_TIER_3_GOLD_SPAWN_LIMIT = new ValueProvider<>(0).addModifiers(new HostValueModifier<>(1, Integer.class, formatSpawnLimit(GeneratorManager.FORGE, 3, GOLD)));
//    public static final ValueProvider<Integer> FORGE_TIER_3_GOLD_SPAWN_BETWEEN = new ValueProvider<>(0).addModifiers(new HostValueModifier<>(1, Integer.class, formatSpawnBetween(GeneratorManager.FORGE, 3, GOLD)));
//    public static final ValueProvider<Boolean> FORGE_TIER_3_GOLD_SPLITTING = new ValueProvider<>(false).addModifiers(new HostValueModifier<>(1, Boolean.class, formatSplitting(GeneratorManager.FORGE, 3, GOLD)));
//    public static final ValueProvider<Integer> FORGE_TIER_3_EMERALD_SPAWN_LIMIT = new ValueProvider<>(0).addModifiers(new HostValueModifier<>(1, Integer.class, formatSpawnLimit(GeneratorManager.FORGE, 3, EMERALD)));
//    public static final ValueProvider<Integer> FORGE_TIER_3_EMERALD_SPAWN_BETWEEN = new ValueProvider<>(0).addModifiers(new HostValueModifier<>(1, Integer.class, formatSpawnBetween(GeneratorManager.FORGE, 3, EMERALD)));
//    public static final ValueProvider<Boolean> FORGE_TIER_3_EMERALD_SPLITTING = new ValueProvider<>(false).addModifiers(new HostValueModifier<>(1, Boolean.class, formatSplitting(GeneratorManager.FORGE, 3, EMERALD)));


    public static String formatSpawnLimit(String generator, int tier, String drop) {
        return String.format("spawn-limit-generator-%s-tier-%d-drop-%s", generator, tier, drop);
    }

    public static String formatSpawnBetween(String generator, int tier, String drop) {
        return String.format("spawn-between-generator-%s-tier-%d-drop-%s", generator, tier, drop);
    }

    public static String formatSplitting(String generator, int tier, String drop) {
        return String.format("splitting-generator-%s-tier-%d-drop-%s", generator, tier, drop);
    }

    static class GeneratorModifier<T> {

        private final String generator;
        private final int tier;
        private final String drop;
        private final ValueProvider<T> value;

        public GeneratorModifier(String generator, int tier, String drop, ValueProvider<T> value) {
            this.generator = generator;
            this.tier = tier;
            this.drop = drop;
            System.out.println("VALUE: " + value);
            this.value = value;
        }

        public String getGenerator() {
            return generator;
        }

        public int getTier() {
            return tier;
        }

        public String getDrop() {
            return drop;
        }

        public ValueProvider<T> getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "GeneratorModifier{" +
                    "generator='" + generator + '\'' +
                    ", tier=" + tier +
                    ", drop='" + drop + '\'' +
                    ", value=" + value +
                    '}';
        }
    }

}
