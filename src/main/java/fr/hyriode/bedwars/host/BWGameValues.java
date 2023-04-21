package fr.hyriode.bedwars.host;

import fr.hyriode.hyrame.game.util.value.HostValueModifier;
import fr.hyriode.hyrame.game.util.value.ValueProvider;

public class BWGameValues {

    public static final ValueProvider<Integer> BED_BREAKING_DELAY = new ValueProvider<>(0)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "time-before-destroy-bed"));
    public static final ValueProvider<Integer> RESPAWNING_DELAY = new ValueProvider<>(5)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "time-before-spawn"));
    public static final ValueProvider<Integer> LIMIT_POS_Y = new ValueProvider<>(0)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "limit-pos-y"));

    public static final ValueProvider<Integer> DIAMOND_GENERATOR_RATE = new ValueProvider<>(1)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "drop-number-diamond"));
    public static final ValueProvider<Integer> EMERALD_GENERATOR_RATE = new ValueProvider<>(1)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "drop-number-emerald"));
    public static final ValueProvider<Integer> FORGE_GENERATOR_RATE = new ValueProvider<>(100)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "drop-number-forge"));

}
