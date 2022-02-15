package fr.hyriode.hyribedwars.game.event;

import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.hyribedwars.HyriBedWars;

public enum EHyriBedWarsNextEvent {

    DIAMOND_GENERATOR_TIER_II("game.next-event.diamond.II", 0),
    DIAMOND_GENERATOR_TIER_III("game.next-event.diamond.III", 0),
    EMERALD_GENERATOR_TIER_II("game.next-event.emerald.II", 0),
    EMERALD_GENERATOR_TIER_III("game.next-event.emerald.III", 0),
    BEDS_DESTROY("game.next-event.beds-destroy", 0),
    ENDER_DRAGON("game.next-event.dragons-spawn", 0),
    GAME_END("game.next-event.game-end", 0),

    ;

    private final String key;
    private final int timeBeforeEvent;

    EHyriBedWarsNextEvent(String key, int timeBeforeEvent) {
        this.key = key;
        this.timeBeforeEvent = timeBeforeEvent;
    }

    public int getTimeBeforeEvent() {
        return timeBeforeEvent;
    }

    public String getKey() {
        return key;
    }

    public HyriLanguageMessage get() {
        return HyriBedWars.getHyriLanguageManager().getMessage(this.key);
    }
}
