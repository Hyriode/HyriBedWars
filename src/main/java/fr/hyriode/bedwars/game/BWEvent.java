package fr.hyriode.bedwars.game;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.generator.GeneratorManager;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.bedwars.game.upgrade.UpgradeManager;
import fr.hyriode.bedwars.host.BWEventValues;
import fr.hyriode.hyrame.game.util.value.ValueProvider;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public enum BWEvent {

    DIAMOND_GENERATOR_TIER_II(0, "diamond.II", () -> BWEventValues.EVENT_TIME_DIAMOND_II, plugin -> {
        plugin.getGame().getDiamondGenerators().forEach(
                generator -> generator.upgrade(HyriBedWars.getGeneratorManager().getGeneratorByName(GeneratorManager.DIAMOND)
                        .getTier(1).getTierGenerator().get(0)));
    }, new ItemBuilder(Material.DIAMOND, 2).build()),
    DIAMOND_GENERATOR_TIER_III(1, "diamond.III", () -> BWEventValues.EVENT_TIME_DIAMOND_III, plugin -> {
        plugin.getGame().getDiamondGenerators().forEach(
                generator -> generator.upgrade(HyriBedWars.getGeneratorManager().getGeneratorByName(GeneratorManager.DIAMOND)
                        .getTier(2).getTierGenerator().get(0)));
    }, new ItemBuilder(Material.DIAMOND, 3).build()),
    EMERALD_GENERATOR_TIER_II(2, "emerald.II", () -> BWEventValues.EVENT_TIME_EMERALD_II, plugin -> {
        plugin.getGame().getEmeraldGenerators().forEach(
                generator -> {
                    generator.upgrade(HyriBedWars.getGeneratorManager().getGeneratorByName(GeneratorManager.EMERALD)
                            .getTier(1).getTierGenerator().get(0));
                });
    }, new ItemBuilder(Material.EMERALD, 2).build()),
    EMERALD_GENERATOR_TIER_III(3, "emerald.III", () -> BWEventValues.EVENT_TIME_EMERALD_III, plugin -> {
        plugin.getGame().getEmeraldGenerators().forEach(
                generator -> generator.upgrade(HyriBedWars.getGeneratorManager().getGeneratorByName(GeneratorManager.EMERALD)
                        .getTier(2).getTierGenerator().get(0)));
    }, new ItemBuilder(Material.EMERALD, 3).build()),
    BEDS_DESTROY(4, "beds-destroy", () -> BWEventValues.EVENT_TIME_BEDS_DESTROY, plugin -> {
        plugin.getGame().getBWTeams().forEach(team -> {
            if(!team.isEliminated()) {
                team.breakBedWithBlock(true);
            }
        });
    }, new ItemBuilder(Material.BED).build()),
    ENDER_DRAGON(5, "dragons-spawn", () -> BWEventValues.EVENT_TIME_ENDER_DRAGON, plugin -> {
        plugin.getGame().getBWTeams(team -> !team.isEliminated()).forEach(team -> {
            team.spawnEnderDragon();
            if(team.getUpgradeTeam().hasUpgrade(UpgradeManager.DRAGON_BUFF)){
                Bukkit.getScheduler().runTaskLater(plugin, team::spawnEnderDragon, 20L);
            }
        });
    }, new ItemBuilder(Material.DRAGON_EGG).build()),
    GAME_END(6, "game-end", () -> BWEventValues.EVENT_TIME_GAME_END, plugin -> plugin.getGame().checkWin(),
            new ItemBuilder(Material.WOOD_DOOR).build()),
    ;

    private final int id;
    private final String key;
    private final Supplier<ValueProvider<Integer>> timeBeforeEvent; //Time in Seconds
    private final Consumer<HyriBedWars> action;
    private final ItemStack icon;

    BWEvent(int id, String key, Supplier<ValueProvider<Integer>> timeBeforeEvent, Consumer<HyriBedWars> action, ItemStack icon) {
        this.id = id;
        this.key = key;
        this.timeBeforeEvent = timeBeforeEvent;
        this.action = action;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public ValueProvider<Integer> getTime() {
        return this.timeBeforeEvent.get();
    }

    public String getKey() {
        return key;
    }

    public ItemStack getIcon() {
        return icon.clone();
    }

    public HyriLanguageMessage get() {
        return HyriLanguageMessage.get("game.next-event." + this.getKey());
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
