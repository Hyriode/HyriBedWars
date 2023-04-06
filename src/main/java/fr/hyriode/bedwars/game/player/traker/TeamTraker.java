package fr.hyriode.bedwars.game.player.traker;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.actionbar.ActionBar;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.stream.Collectors;

public class TeamTraker {

    private BWGameTeam trackedTeam;

    private final HyriBedWars plugin;
    private final BWGamePlayer player;

    public TeamTraker(HyriBedWars plugin, BWGamePlayer player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void start() {
        new BukkitRunnable(){
            @Override
            public void run() {
                if(player.isSpectator() || player.isDead() || trackedTeam == null){
                    player.getPlayer().setCompassTarget(new Location(IHyrame.WORLD.get(), 0, 0, 0));
                    this.cancel();
                    return;
                }

                Distance lastDistance = null;

                for (BWGamePlayer target : trackedTeam.getPlayersPlaying().stream().map(p -> (BWGamePlayer) p).collect(Collectors.toList())) {
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
                    String text = "Tracker : ?m";
                    BWGameTeam teamTarget = lastDistance.getPlayer().getBWTeam();
                    int distance = lastDistance.getDistance();
                    if (teamTarget != null) {
                        ChatColor color = teamTarget.getColor().getChatColor();
                        BWGamePlayer target = lastDistance.getTarget();

                        lastDistance.trackCompass();

                        text = ChatColor.AQUA + "Tracker" + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + " > " + ChatColor.WHITE + "Target: " + color + target.getPlayer().getName() + ChatColor.WHITE + " Distance: " + distance + "m";

                    }
                    new ActionBar(text).send(player.getPlayer());
                }else{
                    setTrackedTeam(null);
                }
            }
        }.runTaskTimer(this.plugin, 0L, 5L);
    }

    private Distance getDistance(BWGamePlayer target){
        return new Distance(this.player, target);
    }

    public void setTrackedTeam(BWGameTeam trakedTeam) {
        this.trackedTeam = trakedTeam;
    }

    public BWGameTeam getTrackedTeam() {
        return trackedTeam;
    }

    public BWGamePlayer getPlayer() {
        return player;
    }

    public boolean hasTeam() {
        return this.trackedTeam != null;
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
