package fr.hyriode.bedwars.game.listener;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import java.util.Collection;

public class BWFireballListener extends HyriListener<HyriBedWars> {

    private final double fireballExplosionSize;
    private final boolean fireballMakeFire;
    private final double fireballHorizontal;
    private final double fireballVertical;

    private final double damageSelf;
    private final double damageEnemy;
    private final double damageTeammates;

    public BWFireballListener(HyriBedWars plugin) {
        super(plugin);
        this.fireballExplosionSize = 3;
        this.fireballMakeFire = false;
        this.fireballHorizontal = 1.0 * -1;
        this.fireballVertical = 1.10;

        this.damageSelf = 2.0;
        this.damageEnemy = 2.0;
        this.damageTeammates = 0.0;
    }

    @EventHandler
    public void fireballHit(ProjectileHitEvent e) {
        if(!(e.getEntity() instanceof Fireball)) return;
        Location location = e.getEntity().getLocation();

        ProjectileSource projectileSource = e.getEntity().getShooter();
        if(!(projectileSource instanceof Player)) return;
        Player source = (Player) projectileSource;

        Vector vector = location.toVector();

        World world = location.getWorld();

        assert world != null;
        Collection<Entity> nearbyEntities = world
                .getNearbyEntities(location, fireballExplosionSize, fireballExplosionSize, fireballExplosionSize);
        for(Entity entity : nearbyEntities) {
            if(!(entity instanceof Player)) continue;
            Player player = (Player) entity;

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

//            LastHit lh = LastHit.getLastHit(player);
//            if (lh != null) {
//                lh.setDamager(source);
//                lh.setTime(System.currentTimeMillis());
//            } else {
//                new LastHit(player, source, System.currentTimeMillis());
//            }
//
//            if(player.equals(source)) {
//                if(damageSelf > 0) {
//                    player.damage(damageSelf); // damage shooter
//                }
//            } else if(arena.getTeam(player).equals(arena.getTeam(source))) {
//                if(damageTeammates > 0) {
//                    player.damage(damageTeammates); // damage teammates
//                }
//            } else {
//                if(damageEnemy > 0) {
//                    player.damage(damageEnemy); // damage enemies
//                }
//            }
    }


    @EventHandler
    public void fireballDirectHit(EntityDamageByEntityEvent e) {
        if(!(e.getDamager() instanceof Fireball)) return;
        if(!(e.getEntity() instanceof Player)) return;

        e.setCancelled(true);
    }
}
