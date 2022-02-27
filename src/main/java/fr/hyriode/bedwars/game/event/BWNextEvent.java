package fr.hyriode.bedwars.game.event;

import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.bedwars.HyriBedWars;

public enum BWNextEvent {

    START("game.next-event.start", 0, "DIAMOND_GENERATOR_TIER_II"),
    DIAMOND_GENERATOR_TIER_II("game.next-event.diamond.II", 360, "DIAMOND_GENERATOR_TIER_III"),
    DIAMOND_GENERATOR_TIER_III("game.next-event.diamond.III", 720, "EMERALD_GENERATOR_TIER_II"),
    EMERALD_GENERATOR_TIER_II("game.next-event.emerald.II", 1080, "EMERALD_GENERATOR_TIER_III"),
    EMERALD_GENERATOR_TIER_III("game.next-event.emerald.III", 1440, "BEDS_DESTROY"),
    BEDS_DESTROY("game.next-event.beds-destroy", 1800, "ENDER_DRAGON"),
    ENDER_DRAGON("game.next-event.dragons-spawn", 2400, "GAME_END"),
    GAME_END("game.next-event.game-end", 3000, null),

    ;

    private final String key;
    private final int timeBeforeEvent; //Time in Seconds
    private final String nextEvent;

    BWNextEvent(String key, int timeBeforeEvent, String nextEvent) {
        this.key = key;
        this.timeBeforeEvent = timeBeforeEvent;
        this.nextEvent = nextEvent;
    }

    public int getTimeBeforeEvent() {
        return timeBeforeEvent;
    }

    public String getKey() {
        return key;
    }

    public HyriLanguageMessage get() {
        return HyriBedWars.getLanguageManager().getMessage(this.key);
    }

    public BWNextEvent getNextEvent() {
        return nextEvent == null ? null : BWNextEvent.valueOf(nextEvent);
    }
}
