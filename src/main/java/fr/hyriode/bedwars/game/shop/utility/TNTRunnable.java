package fr.hyriode.bedwars.game.shop.utility;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

public class TNTRunnable extends BukkitRunnable {

    private final HyriBedWars plugin;
    private final BWGamePlayer player;

    public TNTRunnable(BWGamePlayer player) {
        this.player = player;
        this.plugin = player.getPlugin();
        this.runTaskTimer(plugin, 0L, 3L);
    }

    @Override
    public void run() {
        Player pl = this.player.getPlayer();
        PlayerInventory inv = pl.getInventory();

        if(inv.contains(Material.TNT) || pl.getItemOnCursor().getType() == Material.TNT) {
            new ParticleBuilder(ParticleEffect.REDSTONE, pl.getLocation().clone().add(0, 2.3, 0)).display(p -> !p.getUniqueId().equals(pl.getUniqueId()));
            return;
        }

        this.cancel();
    }
}
