package fr.hyriode.bedwars.game;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.host.BWGameValues;
import fr.hyriode.hyrame.game.HyriGameState;
import org.bukkit.scheduler.BukkitRunnable;

public class BWBedTask extends BukkitRunnable {
    private final HyriBedWars plugin;
    private long time;

    public BWBedTask(HyriBedWars plugin) {
        this.plugin = plugin;
        this.time = BWGameValues.BED_BREAKING_DELAY.get() * 60;
        this.runTaskTimer(this.plugin, 0, 20);
        this.plugin.getGame().setBedBreakable(false);
    }

    @Override
    public void run() {
        --time;

        if(this.plugin.getGame().getState() == HyriGameState.ENDED) {
            this.cancel();
            return;
        }

        if(time <= 0) {
            this.plugin.getGame().setBedBreakable(true);
            this.cancel();
        }

    }
}
