package fr.hyriode.bedwars.game;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.generator.GeneratorManager;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.bedwars.game.upgrade.UpgradeManager;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

public enum BWNextEvent {

    START(0, "start", 0, plugin -> {}),
    DIAMOND_GENERATOR_TIER_II(1, "diamond.II", 360, plugin -> {
        plugin.getGame().getDiamondGenerators().forEach(
                generator -> generator.upgrade(HyriBedWars.getGeneratorManager().getGeneratorByName(GeneratorManager.DIAMOND)
                        .getTier(1).getTierGenerator().get(0)));
    }),
    DIAMOND_GENERATOR_TIER_III(2, "diamond.III", 720, plugin -> {
        plugin.getGame().getDiamondGenerators().forEach(
                generator -> generator.upgrade(HyriBedWars.getGeneratorManager().getGeneratorByName(GeneratorManager.DIAMOND)
                        .getTier(2).getTierGenerator().get(0)));
    }),
    EMERALD_GENERATOR_TIER_II(3, "emerald.II", 1080, plugin -> {
        plugin.getGame().getEmeraldGenerators().forEach(
                generator -> {
                    generator.upgrade(HyriBedWars.getGeneratorManager().getGeneratorByName(GeneratorManager.EMERALD)
                            .getTier(1).getTierGenerator().get(0));
                });
    }),
    EMERALD_GENERATOR_TIER_III(4, "emerald.III", 1440, plugin -> {
        plugin.getGame().getEmeraldGenerators().forEach(
                generator -> generator.upgrade(HyriBedWars.getGeneratorManager().getGeneratorByName(GeneratorManager.EMERALD)
                        .getTier(2).getTierGenerator().get(0)));
    }),
    BEDS_DESTROY(5, "beds-destroy", 1800, plugin -> {
        plugin.getGame().getBWTeams().forEach(BWGameTeam::breakBedWithBlock);
    }),
    ENDER_DRAGON(6, "dragons-spawn", 2400, plugin -> {
        plugin.getGame().getBWTeams(team -> !team.isEliminated() && team.hasBed()).forEach(team -> {
            team.spawnEnderDragon();
            if(team.getUpgradeTeam().hasUpgrade(UpgradeManager.DRAGON_BUFF)){
                Bukkit.getScheduler().runTaskLater(plugin, team::spawnEnderDragon, 20L);
            }
        });
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
