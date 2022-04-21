package fr.hyriode.bedwars.game.listener;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.material.utility.entity.BedBugEntity;
import fr.hyriode.bedwars.game.material.utility.entity.Despawnable;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.hyrame.item.ItemNBT;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class BWBedBugBall extends HyriListener<HyriBedWars> {

    public BWBedBugBall(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent event){
        if(event.getEntity() instanceof Snowball) {
            Snowball sb = (Snowball) event.getEntity();
            if(!(sb.getShooter() instanceof Player)) return;
            Player player = (Player) sb.getShooter();
            boolean isBedBug = new ItemNBT(player.getItemInHand()).hasTag("BedBug");

            if (isBedBug) {
                sb.setMetadata("BedBug", new FixedMetadataValue(this.plugin, true));
            }
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event){
        if(event.getEntity() instanceof Snowball){
            Snowball sb = (Snowball) event.getEntity();
            Player player = (Player) sb.getShooter();
            boolean isBedBug = sb.hasMetadata("BedBug");

            if(!isBedBug) return;

            BWGameTeam team = (BWGameTeam) this.plugin.getGame().getPlayer(player.getUniqueId()).getTeam();

            BedBugEntity.spawn(event.getEntity().getLocation(), this.plugin, team);

        }
    }

}
