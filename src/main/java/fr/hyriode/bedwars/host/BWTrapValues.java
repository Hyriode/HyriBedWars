package fr.hyriode.bedwars.host;

import fr.hyriode.hyrame.game.util.value.HostValueModifier;
import fr.hyriode.hyrame.game.util.value.ValueProvider;

public class BWTrapValues {

    public static final ValueProvider<Boolean> TRAP_ENABLED = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "traps-enabled"));

    public static final ValueProvider<Boolean> TRAP_BLINDNESS_ENABLED = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "trap-blindness-enabled"));
    public static final ValueProvider<Boolean> TRAP_COUNTER_OFFENSIVE_ENABLED = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "trap-counter-offensive-enabled"));
    public static final ValueProvider<Boolean> TRAP_ALARM_ENABLED = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "trap-alarm-enabled"));
    public static final ValueProvider<Boolean> TRAP_MINER_FATIGUE_ENABLED = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "trap-miner-fatigue-enabled"));

}
