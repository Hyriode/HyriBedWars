package fr.hyriode.bedwars.game.task;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGame;
import fr.hyriode.bedwars.game.BWNextEvent;
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

        if(this.plugin.getGame().getState() == HyriGameState.ENDED) {
            this.cancel();
            return;
        }

        for (BWNextEvent value : BWNextEvent.values()) {
            if (game.getCurrentEvent().getTimeBeforeEvent() < this.time && value.getTimeBeforeEvent() < this.time) {
                if(game.getCurrentEvent().getId() < value.getId()) {
                    game.setCurrentEvent(value);
                    value.getAction().accept(this.plugin);
                    if (value == BWNextEvent.GAME_END) {
                        this.cancel();
                    }
                    break;
                }
            }
        }
    }

    public long getTime() {
        return time;
    }

}
