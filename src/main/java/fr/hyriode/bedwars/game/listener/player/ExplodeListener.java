package fr.hyriode.bedwars.game.listener.player;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

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
        event.setCancelled(true);
        IHyrame.WORLD.get().playEffect(event.getEntity().getLocation(), Effect.EXPLOSION_HUGE, 1, 8);
        if(event.getEntity().getType() == EntityType.PRIMED_TNT || event.getEntity().getType() == EntityType.FIREBALL){
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
        }
    }
}
