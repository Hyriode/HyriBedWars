package fr.hyriode.bedwars.host;

import fr.hyriode.hyrame.game.util.value.HostValueModifier;
import fr.hyriode.hyrame.game.util.value.ValueProvider;

public class BWEntityValues {

    public static final ValueProvider<Double> BED_BUG_MAX_HEALTH = new ValueProvider<>(10.0D)
            .addModifiers(new HostValueModifier<>(1, Double.class, "bed-bug-max-health"));
    public static final ValueProvider<Integer> BED_BUG_TIME_ALIVE = new ValueProvider<>(15)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "bed-bug-time-alive"));

    public static final ValueProvider<Double> DREAM_DEFENDER_MAX_HEALTH = new ValueProvider<>(120.0D)
            .addModifiers(new HostValueModifier<>(1, Double.class, "dream-defender-max-health"));
    public static final ValueProvider<Integer> DREAM_DEFENDER_TIME_ALIVE = new ValueProvider<>(240)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "dream-defender-time-alive"));

}
