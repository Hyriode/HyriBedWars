package fr.hyriode.bedwars.game.event;

import fr.hyriode.bedwars.game.generator.BWDiamondGenerator;
import fr.hyriode.bedwars.game.generator.BWEmeraldGenerator;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.game.team.upgrade.EBWUpgrades;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.hyrame.utils.ThreadUtil;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

public enum BWNextEvent {

    START(0, "game.next-event.start", 0, plugin -> {}),
    DIAMOND_GENERATOR_TIER_II(1, "game.next-event.diamond.II", 360, plugin -> {
    plugin.getGame().getDiamondGenerators().forEach(
            generator -> generator.upgrade(BWDiamondGenerator.DIAMOND_TIER_II));
    }),
    DIAMOND_GENERATOR_TIER_III(2, "game.next-event.diamond.III", 720, plugin -> {
        plugin.getGame().getDiamondGenerators().forEach(
                generator -> generator.upgrade(BWDiamondGenerator.DIAMOND_TIER_III));
    }),
    EMERALD_GENERATOR_TIER_II(3, "game.next-event.emerald.II", 1080, plugin -> {
        plugin.getGame().getEmeraldGenerators().forEach(
                generator -> {
            generator.upgrade(BWEmeraldGenerator.EMERALD_TIER_II);
        });
    }),
    EMERALD_GENERATOR_TIER_III(4, "game.next-event.emerald.III", 1440, plugin -> {
        plugin.getGame().getEmeraldGenerators().forEach(
                generator -> generator.upgrade(BWEmeraldGenerator.EMERALD_TIER_III));
    }),
    BEDS_DESTROY(5, "game.next-event.beds-destroy", 1800, plugin -> {
        plugin.getGame().getTeams().forEach(team -> ((BWGameTeam) team).removeBed());
    }),
    ENDER_DRAGON(6, "game.next-event.dragons-spawn", 2400, plugin -> {
        plugin.getGame().getTeams().stream().map(team -> (BWGameTeam)team).filter(team -> !team.isEliminated() && team.hasBed()).forEach(team -> {
            ThreadUtil.backOnMainThread(plugin, () -> {
                team.spawnEnderDragon();
                if(team.getUpgrades().containsUpgrade(EBWUpgrades.DRAGON)){
                    team.spawnEnderDragon();
                }
            });
        });
    }),
    GAME_END(7, "game.next-event.game-end", 3000, plugin -> plugin.getGame().end()),

    ;

    private final int id;
    private final String key;
    private final int timeBeforeEvent; //Time in Seconds
    private final Consumer<HyriBedWars> action;

    BWNextEvent(int id, String key, int timeBeforeEvent, Consumer<HyriBedWars> action) {
        this.id = id;
        this.key = key;
        this.timeBeforeEvent = timeBeforeEvent;
        this.action = action;
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

    public Consumer<HyriBedWars> getAction(){
        return this.action;
    }
}
