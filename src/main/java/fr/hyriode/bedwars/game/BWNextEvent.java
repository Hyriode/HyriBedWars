package fr.hyriode.bedwars.game;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.generator.BWDiamondGenerator;
import fr.hyriode.bedwars.game.generator.BWEmeraldGenerator;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.hyrame.language.HyriLanguageMessage;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

public enum BWNextEvent {

    START(0, "start", 0, plugin -> {}),
    DIAMOND_GENERATOR_TIER_II(1, "diamond.II", 360, plugin -> {
        plugin.getGame().getDiamondGenerators().forEach(
                generator -> generator.upgrade(BWDiamondGenerator.TIER_II));
    }),
    DIAMOND_GENERATOR_TIER_III(2, "diamond.III", 720, plugin -> {
        plugin.getGame().getDiamondGenerators().forEach(
                generator -> generator.upgrade(BWDiamondGenerator.TIER_III));
    }),
    EMERALD_GENERATOR_TIER_II(3, "emerald.II", 1080, plugin -> {
        plugin.getGame().getEmeraldGenerators().forEach(
                generator -> {
                    generator.upgrade(BWEmeraldGenerator.TIER_II);
                });
    }),
    EMERALD_GENERATOR_TIER_III(4, "emerald.III", 1440, plugin -> {
        plugin.getGame().getEmeraldGenerators().forEach(
                generator -> generator.upgrade(BWEmeraldGenerator.TIER_III));
    }),
    BEDS_DESTROY(5, "beds-destroy", 1800, plugin -> {
        plugin.getGame().getBWTeams().forEach(BWGameTeam::breakBedWithBlock);
    }),
    ENDER_DRAGON(6, "dragons-spawn", 2400, plugin -> {
//TODO
//        plugin.getGame().getBWTeams().stream().filter(team -> !team.isEliminated() && team.hasBed()).forEach(team -> {
//            ThreadUtil.backOnMainThread(plugin, () -> {
//                team.spawnEnderDragon();
//                if(team.getUpgrades().containsUpgrade(EBWUpgrades.DRAGON)){
//                    team.spawnEnderDragon();
//                }
//            });
//        });
    }),
    GAME_END(7, "game-end", 3000, plugin -> plugin.getGame().end()),

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
        return "game.next-event." + key;
    }

    public HyriLanguageMessage get() {
        return HyriLanguageMessage.get(this.getKey());
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
