package fr.hyriode.bedwars.game.listener.world.entity;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.entity.models.BedBugEntity;
import fr.hyriode.bedwars.game.entity.models.DreamDefenderEntity;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.hyrame.listener.HyriListener;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Golem;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityListener extends HyriListener<HyriBedWars> {

    public EntityListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onSpawnCreature(CreatureSpawnEvent event){
        if(event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {
            event.setCancelled(false);
        }
    }

    @EventHandler()
    public void onAttackSelfEntity(EntityDamageByEntityEvent event){
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();

        if(damager instanceof Player && (entity instanceof Silverfish || entity instanceof Golem)){
            BWGamePlayer player = this.plugin.getGame().getPlayer((Player) damager);
            EntityLiving entityLiving = HyriBedWars.getEntityManager().getEntityByUUID(entity.getUniqueId());

            if(entityLiving == null) return;
            if(entityLiving instanceof BedBugEntity) {
                BedBugEntity bedBugEntity = (BedBugEntity) entityLiving;
                if(bedBugEntity.getTeam() == player.getTeam()) {
                    event.setCancelled(true);
                    return;
                }
            }

            if (entityLiving instanceof DreamDefenderEntity) {
                DreamDefenderEntity dreamDefenderEntity = (DreamDefenderEntity) entityLiving;
                if(dreamDefenderEntity.getTeam() == player.getTeam()) {
                    event.setCancelled(true);
                }
            }
        }

    }
}
