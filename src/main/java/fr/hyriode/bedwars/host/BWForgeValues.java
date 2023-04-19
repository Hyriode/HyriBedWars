package fr.hyriode.bedwars.host;

import fr.hyriode.bedwars.game.generator.GeneratorManager;
import fr.hyriode.bedwars.game.shop.ItemMoney;
import fr.hyriode.hyrame.HyrameLoader;
import fr.hyriode.hyrame.game.util.value.HostValueModifier;
import fr.hyriode.hyrame.game.util.value.ValueProvider;
import fr.hyriode.api.HyriAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BWForgeValues {

    private static final String IRON = ItemMoney.IRON.getName();
    private static final String GOLD = ItemMoney.GOLD.getName();
    private static final String EMERALD = ItemMoney.EMERALD.getName();

    private static final Map<String, ValueProvider<Integer>> SPAWN_LIMIT = new HashMap<>();
    private static final Map<String, ValueProvider<Integer>> SPAWN_BETWEEN = new HashMap<>();
    private static final Map<String, ValueProvider<Boolean>> SPLITTING = new HashMap<>();

    public static void init() {
        addDrop(GeneratorManager.FORGE, 0, IRON);
        addDrop(GeneratorManager.FORGE, 0, GOLD);

        addDrop(GeneratorManager.FORGE, 1, IRON);
        addDrop(GeneratorManager.FORGE, 1, GOLD);

        addDrop(GeneratorManager.FORGE, 2, IRON);
        addDrop(GeneratorManager.FORGE, 2, GOLD);
        addDrop(GeneratorManager.FORGE, 2, EMERALD);

        addDrop(GeneratorManager.FORGE, 3, IRON);
        addDrop(GeneratorManager.FORGE, 3, GOLD);
        addDrop(GeneratorManager.FORGE, 3, EMERALD);
    }

    private static void addDrop(String generator, int tier, String drop) {
        addSpawnLimit(generator, tier, drop);
        addSpawnBetween(generator, tier, drop);
        addSplitting(generator, tier, drop);
    }

    private static void addSpawnLimit(String generator, int tier, String drop) {
        String optionName = formatSpawnLimit(generator, tier, drop);
        SPAWN_LIMIT.put(optionName, new ValueProvider<>(20)
                .addModifiers(new HostValueModifier<>(1, Integer.class, optionName)));
    }

    private static void addSpawnBetween(String generator, int tier, String drop) {
        String optionName = formatSpawnBetween(generator, tier, drop);
        SPAWN_BETWEEN.put(optionName, new ValueProvider<>(20)
                .addModifiers(new HostValueModifier<>(1, Integer.class, optionName)));
    }

    private static void addSplitting(String generator, int tier, String drop) {
        String optionName = formatSplitting(generator, tier, drop);
        SPLITTING.put(optionName, new ValueProvider<>(false)
                .addModifiers(new HostValueModifier<>(1, Boolean.class, optionName)));
    }

    public static Map<String, ValueProvider<Boolean>> getSplitting() {
        return SPLITTING;
    }

    public static Map<String, ValueProvider<Integer>> getSpawnBetween() {
        return SPAWN_BETWEEN;
    }

    public static Map<String, ValueProvider<Integer>> getSpawnLimit() {
        return SPAWN_LIMIT;
    }

    public static int getSpawnLimit(String name, int tier, String drop) {
        ValueProvider<Integer> generator = SPAWN_LIMIT.get(formatSpawnLimit(name, tier, drop));
        if(generator != null) {
            try {
                return generator.get();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static Integer getSpawnBetween(String name, int tier, String drop) {
        ValueProvider<Integer> generator = SPAWN_BETWEEN.get(formatSpawnBetween(name, tier, drop));
        if(generator != null) {
            try {
                return generator.get();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static Boolean getSplitting(String name, int tier, String drop) {
        ValueProvider<Boolean> generator = SPLITTING.get(formatSplitting(name, tier, drop));
        if(generator != null) {
            try {
                return generator.get();
            }catch (Exception e) {
                e.printStackTrace();
            }
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
