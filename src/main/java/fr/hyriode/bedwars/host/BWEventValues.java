package fr.hyriode.bedwars.host;

import fr.hyriode.hyrame.game.util.value.HostValueModifier;
import fr.hyriode.hyrame.game.util.value.ValueProvider;

public class BWEventValues {

    public static final ValueProvider<Integer> EVENT_TIME_DIAMOND_II = new ValueProvider<>(360)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "event-time-diamond.II"));
    public static final ValueProvider<Integer> EVENT_TIME_DIAMOND_III = new ValueProvider<>(360)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "event-time-diamond.III"));
    public static final ValueProvider<Integer> EVENT_TIME_EMERALD_II = new ValueProvider<>(360)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "event-time-emerald.II"));
    public static final ValueProvider<Integer> EVENT_TIME_EMERALD_III = new ValueProvider<>(360)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "event-time-emerald.III"));
    public static final ValueProvider<Integer> EVENT_TIME_BEDS_DESTROY = new ValueProvider<>(360)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "event-time-beds-destroy"));
    public static final ValueProvider<Integer> EVENT_TIME_ENDER_DRAGON = new ValueProvider<>(600)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "event-time-dragons-spawn"));
    public static final ValueProvider<Integer> EVENT_TIME_GAME_END = new ValueProvider<>(600)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "event-time-game-end"));

}
