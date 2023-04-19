package fr.hyriode.bedwars.host;

import fr.hyriode.hyrame.game.util.value.HostValueModifier;
import fr.hyriode.hyrame.game.util.value.ValueProvider;

public class BWEntityValues {

    public static final ValueProvider<Integer> ENTITY_MAX_HEALTH = new ValueProvider<>(20)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "entity-max-health"));
    public static final ValueProvider<Integer> ENTITY_MAX_ARMOR = new ValueProvider<>(20);

}
