package fr.hyriode.bedwars.game.player;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.actionbar.ActionBar;
import fr.hyriode.hyrame.game.HyriGamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BWTracker {

    private BWGameTeam team;
    private final HyriBedWars plugin;
    private final BWGamePlayer player;

    public BWTracker(HyriBedWars plugin, BWGamePlayer player){
        this.plugin = plugin;
        this.player = player;
    }

    public void setTeam(BWGameTeam team){
        this.team = team;
    }

    public BWGameTeam getTeam() {
        return team;
    }

    public boolean hasTeam(){
        return this.team != null;
    }

    public void start() {
        new BukkitRunnable(){
            @Override
            public void run() {
                if(player.isSpectator() || player.isDead() || team == null){
                    player.getPlayer().setCompassTarget(new Location(IHyrame.WORLD.get(), 0, 0, 0));
                    this.cancel();
                    return;
                }

                Distance lastDistance = null;

                for (BWGamePlayer target : team.getPlayersPlaying().stream().map(p -> (BWGamePlayer) p).collect(Collectors.toList())) {
                    Distance targetDistance = new Distance(player, target);
                    if(lastDistance == null){
                        lastDistance = targetDistance;
                    }else{
                        if(lastDistance.getDistance() > targetDistance.getDistance()){
                            lastDistance = targetDistance;
                        }
                    }
                }

                if(lastDistance != null) {
                    String text;
                    BWGameTeam teamTarget = lastDistance.getPlayer().getHyriTeam();
                    if (teamTarget.getPlayersPlaying().size() == 1) {
                        BWGamePlayer target = lastDistance.getTarget();
                        int distance = lastDistance.getDistance();

                        lastDistance.trackCompass();

                        text = "Target: " + target.getTeam().getColor().getChatColor() + target.getPlayer().getName() + ChatColor.RESET + " Distance: " + distance + "m";

                    } else {
                        ChatColor color = teamTarget.getColor().getChatColor();
                        text = "Target: " + color + lastDistance.getPlayer().getPlayer().getDisplayName() + ChatColor.RESET + " Distance: " + color + lastDistance.getPlayer() + "m";
                    }
                    new ActionBar(text).send(player.getPlayer());
                }else{
                    player.getTracker().setTeam(null);
                }
            }
        }.runTaskTimer(this.plugin, 0L, 5L);
    }

    public static class Distance{

        private final BWGamePlayer player;
        private final BWGamePlayer target;

        private Distance(BWGamePlayer player, BWGamePlayer target){
            this.player = player;
            this.target = target;
        }

        public BWGamePlayer getPlayer() {
            return player;
        }

        public BWGamePlayer getTarget() {
            return target;
        }

        public Location getLoc1() {
            return player.getPlayer().getLocation().clone();
        }

        public Location getLoc2() {
            return target.getPlayer().getLocation().clone();
        }

        public int getDistance() {
            Location l1 = this.player.getPlayer().getLocation().clone();
            l1.setPitch(0);
            l1.setYaw(0);
            l1.setY(0);

            Location l2 = this.target.getPlayer().getLocation().clone();
            l2.setY(0);
            l2.setPitch(0);
            l2.setYaw(0);

            return (int) l1.distance(l2);
        }

        public void trackCompass(){
            player.getPlayer().setCompassTarget(target.getPlayer().getLocation());
        }
    }
}
