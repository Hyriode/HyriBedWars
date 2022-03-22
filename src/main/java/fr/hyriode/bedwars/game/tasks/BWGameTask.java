package fr.hyriode.bedwars.game.tasks;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGame;
import fr.hyriode.bedwars.game.event.BWNextEvent;
import fr.hyriode.bedwars.game.generator.BWDiamondGenerator;
import fr.hyriode.bedwars.game.generator.BWEmeraldGenerator;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public class BWGameTask extends BukkitRunnable {

    private final HyriBedWars plugin;
    private long time;

    public BWGameTask(HyriBedWars plugin, long time) {
        this.plugin = plugin;
        this.time = time;
    }

    @Override
    public void run() {
        ++time;

        final BWGame game = this.plugin.getGame();

        for (BWNextEvent value : BWNextEvent.values()) {
            if (value.getTimeBeforeEvent() < this.time) {
                game.setActualEvent(value);
            }
        }

        switch (this.plugin.getGame().getActualEvent()) {
            case START:
//                System.out.println("Start");
                break;

            case DIAMOND_GENERATOR_TIER_II:
                game.getDiamondGenerators().forEach(generator -> {
                    generator.upgrade(BWDiamondGenerator.DIAMOND_TIER_II);
                });
                break;

            case DIAMOND_GENERATOR_TIER_III:
                game.getDiamondGenerators().forEach(generator -> {
                    generator.upgrade(BWDiamondGenerator.DIAMOND_TIER_III);
                });
                break;

            case EMERALD_GENERATOR_TIER_II:
                game.getEmeraldGenerators().forEach(generator -> {
                    generator.upgrade(BWEmeraldGenerator.EMERALD_TIER_II);
                });
                break;

            case EMERALD_GENERATOR_TIER_III:
                game.getEmeraldGenerators().forEach(generator -> {
                    generator.upgrade(BWEmeraldGenerator.EMERALD_TIER_III);
                });
                break;

            case BEDS_DESTROY:
                game.getTeams().forEach(team -> {
                    ((BWGameTeam) team).baseArea(loc -> {
                        if(loc.getBlock().getType() == Material.BED_BLOCK){
                            loc.getBlock().setType(Material.AIR);
                        }
                    });
                });
                break;

            case ENDER_DRAGON:
//                System.out.println("Dragons relachés");
                break;

            case GAME_END:
                game.end();
                cancel();
                break;

        }

    }

    public long getTime() {
        return time;
    }
}
