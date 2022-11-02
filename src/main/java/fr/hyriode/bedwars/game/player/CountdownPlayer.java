package fr.hyriode.bedwars.game.player;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.utils.StringUtils;
import fr.hyriode.hyrame.actionbar.ActionBar;
import fr.hyriode.api.language.HyriLanguageMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class CountdownPlayer {

    private final String name;
    private final BWGamePlayer player;
    private BukkitRunnable task;
    private int time;

    public CountdownPlayer(BWGamePlayer player, String name) {
        this.name = name;
        this.player = player;
    }

    public CountdownPlayer start(HyriBedWars plugin, int timeFinal, String keyName) {
        this.time = timeFinal;
        Player p = this.player.getPlayer();

        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                if(keyName != null){
                    new ActionBar(HyriLanguageMessage.get(keyName).getValue(p) + ChatColor.DARK_AQUA + " - " + ChatColor.AQUA + StringUtils.formatTime(time / 20))
                            .send(p);
                }
                if(player.isSpectator() || player.isDead() || time < 0){
                    stop();
                }
                time--;
            }
        };
        this.task.runTaskTimerAsynchronously(plugin, 0L, 1L);
        return this;
    }

    public void stop() {
        if(this.task != null) this.task.cancel();
        this.player.removeCountdown(this.name);
    }

    public String getName() {
        return name;
    }

    public BukkitRunnable getTask() {
        return task;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
