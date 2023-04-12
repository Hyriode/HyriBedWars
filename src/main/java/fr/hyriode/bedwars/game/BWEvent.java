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

public enum BWEvent {

    DIAMOND_GENERATOR_TIER_II(0, "diamond.II", 360, plugin -> {
        System.out.println("UPGRADE DIAMOND GENERATOR TIER II");
        plugin.getGame().getDiamondGenerators().forEach(
                generator -> generator.upgrade(HyriBedWars.getGeneratorManager().getGeneratorByName(GeneratorManager.DIAMOND)
                        .getTier(1).getTierGenerator().get(0)));
    }),
    DIAMOND_GENERATOR_TIER_III(1, "diamond.III", 360, plugin -> {
        plugin.getGame().getDiamondGenerators().forEach(
                generator -> generator.upgrade(HyriBedWars.getGeneratorManager().getGeneratorByName(GeneratorManager.DIAMOND)
                        .getTier(2).getTierGenerator().get(0)));
    }),
    EMERALD_GENERATOR_TIER_II(2, "emerald.II", 360, plugin -> {
        plugin.getGame().getEmeraldGenerators().forEach(
                generator -> {
                    generator.upgrade(HyriBedWars.getGeneratorManager().getGeneratorByName(GeneratorManager.EMERALD)
                            .getTier(1).getTierGenerator().get(0));
                });
    }),
    EMERALD_GENERATOR_TIER_III(3, "emerald.III", 360, plugin -> {
        plugin.getGame().getEmeraldGenerators().forEach(
                generator -> generator.upgrade(HyriBedWars.getGeneratorManager().getGeneratorByName(GeneratorManager.EMERALD)
                        .getTier(2).getTierGenerator().get(0)));
    }),
    BEDS_DESTROY(4, "beds-destroy", 360, plugin -> {
        plugin.getGame().getBWTeams().forEach(BWGameTeam::breakBedWithBlock);
    }),
    ENDER_DRAGON(5, "dragons-spawn", 600, plugin -> {
        plugin.getGame().getBWTeams(team -> !team.isEliminated()).forEach(team -> {
            team.spawnEnderDragon();
            if(team.getUpgradeTeam().hasUpgrade(UpgradeManager.DRAGON_BUFF)){
                Bukkit.getScheduler().runTaskLater(plugin, team::spawnEnderDragon, 20L);
            }
        });
    }),
    GAME_END(6, "game-end", 600, plugin -> plugin.getGame().checkWin()),
    ;

    private final int id;
    private final String key;
    private final int timeBeforeEvent; //Time in Seconds
    private final Consumer<HyriBedWars> action;

    BWEvent(int id, String key, int timeBeforeEvent, Consumer<HyriBedWars> action) {
        this.id = id;
        this.key = key;
        this.timeBeforeEvent = timeBeforeEvent;
        this.action = action;
    }

    public int getId() {
        return id;
    }

    public int getTime() {
        return timeBeforeEvent;
    }

    public String getKey() {
        return "game.next-event." + key;
    }

    public HyriLanguageMessage get() {
        return HyriLanguageMessage.get(this.getKey());
    }

    public void action(HyriBedWars plugin){
        this.action.accept(plugin);
    }

    public int getNextEventId() {
        return id + 1;
    }

    public int getBeforeEventId() {
        return id - 1;
    }

    public BWEvent getNextEvent(){
        return this.getNextEventId() > BWEvent.values().length - 1 ? null : getEventById(this.getNextEventId());
    }

    public BWEvent getBeforeEvent(){
        return this.getBeforeEventId() < 0 ? null : getEventById(this.getBeforeEventId());
    }

    public static BWEvent getEventById(int id){
        Optional<BWEvent> result = Arrays.stream(BWEvent.values()).filter(ne -> ne.getId() == id).findFirst();
        return result.orElse(null);
    }

}
