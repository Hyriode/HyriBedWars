package fr.hyriode.bedwars.game.event;

import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.bedwars.HyriBedWars;

import java.util.Arrays;
import java.util.Optional;

public enum BWNextEvent {

    START(0, "game.next-event.start", 0),
    DIAMOND_GENERATOR_TIER_II(1, "game.next-event.diamond.II", 360),
    DIAMOND_GENERATOR_TIER_III(2, "game.next-event.diamond.III", 720),
    EMERALD_GENERATOR_TIER_II(3, "game.next-event.emerald.II", 1080),
    EMERALD_GENERATOR_TIER_III(4, "game.next-event.emerald.III", 1440),
    BEDS_DESTROY(5, "game.next-event.beds-destroy", 1800),
    ENDER_DRAGON(6, "game.next-event.dragons-spawn", 2400),
    GAME_END(7, "game.next-event.game-end", 3000),

    ;

    private final int id;
    private final String key;
    private final int timeBeforeEvent; //Time in Seconds

    BWNextEvent(int id, String key, int timeBeforeEvent) {
        this.id = id;
        this.key = key;
        this.timeBeforeEvent = timeBeforeEvent;
    }

    public int getId() {
        return id;
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

    public int getNextEventId() {
        return id + 1;
    }

    public BWNextEvent getNextEvent(){
        return this.getNextEventId() > 7 ? null : getNextEventById(this.getNextEventId());
    }

    public static BWNextEvent getNextEventById(int id){
        Optional<BWNextEvent> result = Arrays.stream(BWNextEvent.values()).filter(ne -> ne.getId() == id).findFirst();
        return result.orElse(null);
    }
}
