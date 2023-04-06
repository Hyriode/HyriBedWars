package fr.hyriode.bedwars.game.listener.item.utility;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.entity.models.BedBugEntity;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.projectiles.ProjectileSource;

public class ItemBedBugListener extends HyriListener<HyriBedWars> {

    public ItemBedBugListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event){
        if(!(event.getEntity().getShooter() instanceof Player)) return;
        if(event.getEntity().hasMetadata(MetadataReferences.BEDBUG)){
            BedBugEntity.spawn(event.getEntity().getLocation(), this.plugin,
                    this.plugin.getGame().getPlayer((Player) event.getEntity().getShooter()).getBWTeam());
        }
    }

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent event){
        ProjectileSource shooter = event.getEntity().getShooter();
        if(!(shooter instanceof Player)) return;
        Player player = (Player) shooter;
        if(MetadataReferences.isMetaItem(MetadataReferences.BEDBUG, player.getItemInHand())) {
            event.getEntity().setMetadata(MetadataReferences.BEDBUG, new FixedMetadataValue(this.plugin, true));
        }
    }
}
