package fr.hyriode.bedwars.game.listener.world.explosion;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.config.BWConfiguration;
import fr.hyriode.bedwars.game.BWGame;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.listener.HyriListener;
import fr.hyriode.hyrame.utils.Area;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ExplodeListener extends HyriListener<HyriBedWars> {

    private final double fireballHorizontal;
    private final double fireballVertical;
    private final int fireballExplosionSize;
    private final boolean fireballMakeFire;

    public ExplodeListener(HyriBedWars plugin) {
        super(plugin);
        this.fireballHorizontal = 1.0 * -1;
        this.fireballVertical = 1.10;
        this.fireballExplosionSize = 3;
        this.fireballMakeFire = false;
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event){
        if(event.getEntity().getType() == EntityType.PRIMED_TNT || event.getEntity().getType() == EntityType.FIREBALL){
            event.setCancelled(true);
            IHyrame.WORLD.get().playEffect(event.getEntity().getLocation(), Effect.EXPLOSION_HUGE, 1, 8);
            Location location = event.getEntity().getLocation();
            Vector vector = location.toVector();

            List<Entity> nearbyEntities = event.getEntity().getNearbyEntities(fireballExplosionSize, fireballExplosionSize, fireballExplosionSize);
            for(BWGamePlayer player : nearbyEntities.stream().map(entity -> this.plugin.getGame().getPlayer(entity.getUniqueId())).collect(Collectors.toList())) {
                if(player == null || player.isDead() || player.isSpectator()) continue;
                Player p = player.getPlayer();

                Vector playerVector = p.getLocation().toVector();
                Vector normalizedVector = vector.subtract(playerVector).normalize();
                Vector horizontalVector = normalizedVector.multiply(fireballHorizontal);
                double y = fireballVertical*1.3;
                p.setVelocity(horizontalVector.setY(y));
            }

            for (Block block : event.blockList()) {
                if(block.hasMetadata(MetadataReferences.PLACEBYPLAYER)){
                    block.setType(Material.AIR);
                }
            }
            return;
        }

        BWConfiguration config = this.plugin.getConfiguration();
        List<Area> protectArea = new ArrayList<>();
        protectArea.addAll(config.getTeams().stream().map(BWConfiguration.Team::getBaseProtectArea).collect(Collectors.toList()));
        protectArea.addAll(config.getProtectionArea());

        for (Block block : event.blockList()) {
            for (Area area : protectArea) {
                if(area.isInArea(block.getLocation())){
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        Entity entity = event.getDamager();
        if(entity instanceof Fireball || entity instanceof TNTPrimed) {
            if(entity instanceof Projectile) {
                ProjectileSource shooter = ((Projectile) entity).getShooter();
                if(shooter instanceof Player) {
                    Player player = (Player) shooter;
                    if(player.getUniqueId().equals(event.getEntity().getUniqueId())) {
                        event.setDamage(2.0F);
                        return;
                    }
                    BWGame game = this.plugin.getGame();
                    BWGamePlayer bwPlayerShooter = game.getPlayer(player.getUniqueId());
                    BWGamePlayer victim = game.getPlayer(event.getDamager().getUniqueId());
                    if(bwPlayerShooter != null && victim != null) {
                        if(bwPlayerShooter.getTeam().equals(victim.getTeam())) {
                            event.setDamage(3.0F);
                            return;
                        }
                    }

                }
            }
        }
        event.setDamage(6.0F);
    }
}
