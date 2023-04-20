package fr.hyriode.bedwars.host;

import fr.hyriode.bedwars.config.BWConfiguration;
import fr.hyriode.bedwars.game.generator.GeneratorManager;
import fr.hyriode.bedwars.game.generator.StandardGenerator;
import fr.hyriode.bedwars.game.shop.ItemMoney;
import fr.hyriode.bedwars.game.type.BWGameType;
import fr.hyriode.hyrame.HyrameLoader;
import fr.hyriode.hyrame.game.util.value.HostValueModifier;
import fr.hyriode.hyrame.game.util.value.ValueProvider;
import fr.hyriode.api.HyriAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BWForgeValues {

    private static final String IRON = ItemMoney.IRON.getName();
    private static final String GOLD = ItemMoney.GOLD.getName();
    private static final String EMERALD = ItemMoney.EMERALD.getName();
    private static final String DIAMOND = ItemMoney.DIAMOND.getName();

    private static final Map<String, ValueProvider<Integer>> SPAWN_LIMIT = new HashMap<>();
    private static final Map<String, ValueProvider<Integer>> SPAWN_BETWEEN = new HashMap<>();
    private static final Map<String, ValueProvider<Boolean>> SPLITTING = new HashMap<>();

    public static void init(Supplier<BWConfiguration> configurationSupplier, BWGameType gameType) {
        addDrop(configurationSupplier, gameType, GeneratorManager.FORGE, 0, IRON);
        addDrop(configurationSupplier, gameType, GeneratorManager.FORGE, 0, GOLD);

        addDrop(configurationSupplier, gameType, GeneratorManager.FORGE, 1, IRON);
        addDrop(configurationSupplier, gameType, GeneratorManager.FORGE, 1, GOLD);

        addDrop(configurationSupplier, gameType, GeneratorManager.FORGE, 2, IRON);
        addDrop(configurationSupplier, gameType, GeneratorManager.FORGE, 2, GOLD);
        addDrop(configurationSupplier, gameType, GeneratorManager.FORGE, 2, EMERALD);

        addDrop(configurationSupplier, gameType, GeneratorManager.FORGE, 3, IRON);
        addDrop(configurationSupplier, gameType, GeneratorManager.FORGE, 3, GOLD);
        addDrop(configurationSupplier, gameType, GeneratorManager.FORGE, 3, EMERALD);

        addDrop(configurationSupplier, gameType, GeneratorManager.FORGE, 4, IRON);
        addDrop(configurationSupplier, gameType, GeneratorManager.FORGE, 4, GOLD);
        addDrop(configurationSupplier, gameType, GeneratorManager.FORGE, 4, EMERALD);

        addDrop(configurationSupplier, gameType, GeneratorManager.DIAMOND, 0, DIAMOND);
        addDrop(configurationSupplier, gameType, GeneratorManager.DIAMOND, 1, DIAMOND);
        addDrop(configurationSupplier, gameType, GeneratorManager.DIAMOND, 2, DIAMOND);

        addDrop(configurationSupplier, gameType, GeneratorManager.EMERALD, 0, EMERALD);
        addDrop(configurationSupplier, gameType, GeneratorManager.EMERALD, 1, EMERALD);
        addDrop(configurationSupplier, gameType, GeneratorManager.EMERALD, 2, EMERALD);
    }

    private static void addDrop(Supplier<BWConfiguration> configurationSupplier, BWGameType gameType, String generator, int tier, String drop) {
        addSpawnLimit(gameType, generator, tier, drop);
        addSpawnBetween(configurationSupplier, gameType, generator, tier, drop);
        addSplitting(gameType, generator, tier, drop);
    }

    private static void addSpawnLimit(BWGameType gameType, String generator, int tier, String drop) {
        String optionName = formatSpawnLimit(generator, tier, drop);
        Integer spawnLimit = StandardGenerator.getSpawnLimit(gameType, generator, tier, drop);
        SPAWN_LIMIT.put(optionName, new ValueProvider<>(spawnLimit != null ? spawnLimit : 20)
                .addModifiers(new HostValueModifier<>(1, Integer.class, optionName)));
    }

    private static void addSpawnBetween(Supplier<BWConfiguration> dropSupplier, BWGameType gameType, String generator, int tier, String dropName) {
        String optionName = formatSpawnBetween(generator, tier, dropName);
        Integer spawnBetween = StandardGenerator.getSpawnBetween(gameType, generator, tier, dropName);
        BWConfiguration.Generator gen = dropSupplier.get().getGeneratorsBase().stream().filter(g -> g.getTier() == tier).findFirst().orElse(null);
        if(gen != null) {
            BWConfiguration.Generator.Drop drop = gen.getDrops().stream().filter(d -> d.getItemName().equals(dropName)).findFirst().orElse(null);
            if(drop != null) {
                spawnBetween = drop.getSpawnBetween();
            }
        }
        SPAWN_BETWEEN.put(optionName, new ValueProvider<>(spawnBetween != null ? spawnBetween : 20)
                .addModifiers(new HostValueModifier<>(1, Integer.class, optionName)));
    }

    private static void addSplitting(BWGameType gameType, String generator, int tier, String drop) {
        String optionName = formatSplitting(generator, tier, drop);
        Boolean splitting = StandardGenerator.getSplitting(gameType, generator, tier, drop);
        SPLITTING.put(optionName, new ValueProvider<>(splitting != null ? splitting : false)
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

    public static ValueProvider<Integer> getSpawnLimit(String name, int tier, String drop) {
        String optionName = formatSpawnLimit(name, tier, drop);
        ValueProvider<Integer> generator = SPAWN_LIMIT.get(optionName);
        return generator != null ? generator : new ValueProvider<>(0);
    }

    public static ValueProvider<Integer> getSpawnBetween(String name, int tier, String drop) {
        ValueProvider<Integer> generator = SPAWN_BETWEEN.get(formatSpawnBetween(name, tier, drop));
        return generator != null ? generator : new ValueProvider<>(0);
    }

    public static ValueProvider<Boolean> getSplitting(String name, int tier, String drop) {
        ValueProvider<Boolean> generator = SPLITTING.get(formatSplitting(name, tier, drop));
        return generator != null ? generator : new ValueProvider<>(false);
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
