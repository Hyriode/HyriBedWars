package fr.hyriode.bedwars.game.task;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGame;
import fr.hyriode.bedwars.game.BWEvent;
import fr.hyriode.hyrame.game.HyriGameState;
import org.bukkit.scheduler.BukkitRunnable;

public class BWGameTask extends BukkitRunnable {

    private final HyriBedWars plugin;
    private long time;

    public BWGameTask(HyriBedWars plugin) {
        this.plugin = plugin;
        this.runTaskTimer(this.plugin, 0, 20);
    }

    @Override
    public void run() {
        ++time;

        final BWGame game = this.plugin.getGame();

        if(game.getState() == HyriGameState.ENDED) {
            this.plugin.getGame().updateScoreboards();
            this.cancel();
            return;
        }

        BWEvent currentNextEvent = game.getNextEvent();
        int timeSecond = currentNextEvent.getTime().get();

        if (timeSecond < this.time) {
            BWEvent nextEvent = game.nextEvent();

            this.time = 0;
            currentNextEvent.action(this.plugin);
            if(nextEvent == null) {
                this.cancel();
            }
        }
        this.plugin.getGame().updateScoreboards();
    }

    public long getTime() {
        return time;
    }

}
