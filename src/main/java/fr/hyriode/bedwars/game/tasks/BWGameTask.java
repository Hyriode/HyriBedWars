package fr.hyriode.bedwars.game.tasks;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.event.BWNextEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BWGameTask extends BukkitRunnable {

    private final HyriBedWars plugin;
    private long index;

    public BWGameTask(HyriBedWars plugin, long index) {
        this.plugin = plugin;
        this.index = index;
    }

    @Override
    public void run() {
        ++index;

        for (BWNextEvent value : BWNextEvent.values()) {
            if (value.getTimeBeforeEvent() < this.index) {
                this.plugin.getGame().setActualEvent(value);
            }
        }

        switch (this.plugin.getGame().getActualEvent()) {
            case START:
//                System.out.println("Start");
                break;

            case DIAMOND_GENERATOR_TIER_II:
//                System.out.println("Diamants passés au tier 2");
                break;

            case DIAMOND_GENERATOR_TIER_III:
//                System.out.println("Diamants passés au tier 3");
                break;

            case EMERALD_GENERATOR_TIER_II:
//                System.out.println("Emeraudes passées au tier 2");
                break;

            case EMERALD_GENERATOR_TIER_III:
//                System.out.println("Emeraudes passés au tier 3");
                break;

            case BEDS_DESTROY:
//                System.out.println("Tous les lits détruits");
                break;

            case ENDER_DRAGON:
//                System.out.println("Dragons relachés");
                break;

            case GAME_END:
//                System.out.println("Fin de partie");
                this.plugin.getGame().end();
                cancel();
                break;

        }

    }

    public long getIndex() {
        return index;
    }
}
