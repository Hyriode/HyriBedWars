package fr.hyriode.bedwars.game.tasks;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.hyrame.game.HyriGamePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.stream.Collectors;

public class BWGameInventoryTask extends BukkitRunnable {

    private HyriBedWars plugin;

    public BWGameInventoryTask(HyriBedWars plugin){
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (BWGamePlayer bwPlayer : this.plugin.getGame().getPlayers()) {
            Player player = bwPlayer.getPlayer();
            if(player.getInventory().contains(Material.TNT)){
                ParticleBuilder particle = new ParticleBuilder(ParticleEffect.REDSTONE).setLocation(player.getLocation().add(0, 2.5, 0));

                particle.display(this.plugin.getGame().getPlayers().stream().map(HyriGamePlayer::getPlayer)
                        .filter(p -> p.getUniqueId() != player.getUniqueId()).collect(Collectors.toList()));
            }
        }
    }
}
