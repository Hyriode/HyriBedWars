package fr.hyriode.bedwars.host;

import fr.hyriode.hyrame.game.util.value.HostValueModifier;
import fr.hyriode.hyrame.game.util.value.ValueProvider;

public class BWMapValues {

    public static final ValueProvider<Boolean> BREAK_WORLD = new ValueProvider<>(false)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "break-world"));
    public static final ValueProvider<Integer> COOLDOWN_FIREBALL = new ValueProvider<>(10)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "cooldown-fireball"));

}
