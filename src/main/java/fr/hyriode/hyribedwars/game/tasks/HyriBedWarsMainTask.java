package fr.hyriode.hyribedwars.game.tasks;

import fr.hyriode.hyribedwars.HyriBedWars;
import fr.hyriode.hyribedwars.game.event.EHyriBedWarsNextEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class HyriBedWarsMainTask extends BukkitRunnable {

    private final HyriBedWars plugin;
    private long index;

    public HyriBedWarsMainTask(HyriBedWars plugin, long index) {
        this.plugin = plugin;
        this.index = index;
    }

    @Override
    public void run() {

        for (EHyriBedWarsNextEvent value : EHyriBedWarsNextEvent.values()) {
            if (value.getTimeBeforeEvent() == this.index) {
                this.plugin.getGame().setActualEvent(value);
            }
        }

        switch (this.plugin.getGame().getActualEvent()) {

            case DIAMOND_GENERATOR_TIER_II:
                System.out.println("Diamants passés au tier 2");
                break;
            case DIAMOND_GENERATOR_TIER_III:
                System.out.println("Diamants passés au tier 3");
                break;

            case EMERALD_GENERATOR_TIER_II:
                System.out.println("Emeraudes passées au tier 2");
                break;

            case EMERALD_GENERATOR_TIER_III:
                System.out.println("Emeraudes passés au tier 3");
                break;

            case BEDS_DESTROY:
                System.out.println("Tous les lits détruits");
                break;

            case ENDER_DRAGON:
                System.out.println("Dragons relachés");
                break;

            case GAME_END:
                System.out.println("Fin de partie");
                this.plugin.getGame().end();
                cancel();
                break;

        }


    }
}
