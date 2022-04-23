package fr.hyriode.bedwars.game.tasks;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGame;
import fr.hyriode.bedwars.game.event.BWNextEvent;
import fr.hyriode.hyrame.game.HyriGameState;
import org.bukkit.scheduler.BukkitRunnable;

public class BWGameTask extends BukkitRunnable {

    private final HyriBedWars plugin;
    private long time;

    public BWGameTask(HyriBedWars plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        ++time;

        final BWGame game = this.plugin.getGame();

        if(this.plugin.getGame().getState() == HyriGameState.ENDED)
            this.cancel();
        for (BWNextEvent value : BWNextEvent.values()) {
            if (game.getActualEvent().getTimeBeforeEvent() < this.time && value.getTimeBeforeEvent() < this.time) {
                if(game.getActualEvent().getId() < value.getId()) {
                    game.setActualEvent(value);
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
