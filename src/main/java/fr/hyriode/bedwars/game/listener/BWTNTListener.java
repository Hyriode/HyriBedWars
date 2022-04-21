package fr.hyriode.bedwars.game.listener;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import java.util.Collection;

public class BWTNTListener extends HyriListener<HyriBedWars> {

    private final double fireballExplosionSize;
    private final boolean fireballMakeFire;
    private final double fireballHorizontal;
    private final double fireballVertical;

    private final double damageSelf;
    private final double damageEnemy;
    private final double damageTeammates;

    public BWTNTListener(HyriBedWars plugin) {
        super(plugin);

        this.fireballExplosionSize = 3;
        this.fireballMakeFire = false;
        this.fireballHorizontal = 1.0 * -1;
        this.fireballVertical = 1.15;

        this.damageSelf = 2.0;
        this.damageEnemy = 2.0;
        this.damageTeammates = 0.0;
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e){
        if(!(e.getEntity() instanceof TNTPrimed))return;

        Location location = e.getEntity().getLocation();

        Vector vector = location.toVector();

        World world = location.getWorld();

        assert world != null;
        Collection<Entity> nearbyEntities = world
                .getNearbyEntities(location, fireballExplosionSize, fireballExplosionSize, fireballExplosionSize);
        for(Entity entity : nearbyEntities) {
            if(!(entity instanceof Player)) continue;
            Player player = (Player) entity;

            BWGamePlayer bwPlayer = this.plugin.getGame().getPlayer(player);
            if(bwPlayer != null && !bwPlayer.isDead() && !bwPlayer.isSpectator()) continue;

            Vector playerVector = player.getLocation().toVector();
            Vector normalizedVector = vector.subtract(playerVector).normalize();
            Vector horizontalVector = normalizedVector.multiply(fireballHorizontal);
            double y = normalizedVector.getY();
            if(y < 0 ) y += 1.5;
            if(y <= 0.5) {
                y = fireballVertical*1.5; // kb for not jumping
            } else {
                y = y*fireballVertical*1.5; // kb for jumping
            }
            player.setVelocity(horizontalVector.setY(y));
        }
    }

}
